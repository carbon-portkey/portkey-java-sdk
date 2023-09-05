package io.aelf.portkey.behaviour.login;

import com.google.gson.Gson;
import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.portkey.behaviour.global.EntryCheckConfig;
import io.aelf.portkey.behaviour.global.GuardianObserver;
import io.aelf.portkey.behaviour.guardian.GuardianGenerator;
import io.aelf.portkey.behaviour.guardian.GuardianBehaviourEntity;
import io.aelf.portkey.behaviour.pin.IAfterVerifiedBehaviour;
import io.aelf.portkey.behaviour.pin.SetPinBehaviourEntity;
import io.aelf.portkey.internal.model.common.*;
import io.aelf.portkey.internal.model.extraInfo.DeviceExtraInfo;
import io.aelf.portkey.internal.model.extraInfo.ExtraInfoWrapper;
import io.aelf.portkey.internal.model.google.GoogleAccount;
import io.aelf.portkey.internal.model.guardian.ApprovedGuardianDTO;
import io.aelf.portkey.internal.model.guardian.GuardianWrapper;
import io.aelf.portkey.internal.model.recovery.RequestRecoveryParams;
import io.aelf.portkey.internal.model.wallet.WalletBuildConfig;
import io.aelf.portkey.internal.tools.DataVerifyTools;
import io.aelf.portkey.internal.tools.GlobalConfig;
import io.aelf.portkey.network.connecter.INetworkInterface;
import io.aelf.portkey.utils.enums.ExtraDataPlatformEnum;
import io.aelf.portkey.utils.log.GLogger;
import io.aelf.response.ResultCode;
import io.aelf.schemas.KeyPairInfo;
import io.aelf.utils.AElfException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class LoginBehaviourEntity implements GuardianObserver, IAfterVerifiedBehaviour {
    private final List<GuardianWrapper> guardians;
    private final int guardianVerifyLimit;
    private final AccountOriginalType accountOriginalType;
    private final String accountIdentifier;
    private final GoogleAccount googleAccount;


    public LoginBehaviourEntity(@NotNull List<GuardianWrapper> guardians) {
        this(guardians, new EntryCheckConfig());
    }


    public LoginBehaviourEntity(@NotNull List<GuardianWrapper> guardians, EntryCheckConfig config) {
        this(guardians, config, null);
    }

    public LoginBehaviourEntity(@NotNull List<GuardianWrapper> guardians, EntryCheckConfig config, @Nullable GoogleAccount googleAccount) {
        this.guardians = guardians;
        this.guardianVerifyLimit = getGuardianVerifyLimit(guardians);
        this.accountOriginalType = config.getAccountOriginalType();
        this.accountIdentifier = config.getAccountIdentifier();
        this.googleAccount = googleAccount;
    }

    /**
     * the guardian's verify rule:
     * <p>
     * if length <= 3: verifyLimit = length
     * <p>
     * if length > 3: verifyLimit = Math.floor(guardians.size() * 0.6) + 1
     */
    @Contract(pure = true)
    public static int getGuardianVerifyLimit(@NotNull List<GuardianWrapper> guardians) {
        int length = guardians.size();
        if (length <= 3) return length;
        return (int) (guardians.size() * 0.6 + 1);
    }

    public AccountOriginalType getAccountOriginalType() {
        return accountOriginalType;
    }

    public String getAccountIdentifier() {
        return accountIdentifier;
    }

    public List<GuardianWrapper> getGuardians() {
        return guardians;
    }

    public GuardianBehaviourEntity getGuardianBehaviourEntity(int position) {
        AssertChecker.assertTrue(position >= 0 && position < guardians.size(), "position out of range");
        return getGuardianBehaviourEntity(guardians.get(position));
    }

    public synchronized GuardianBehaviourEntity getGuardianBehaviourEntity(@NotNull GuardianWrapper guardianWrapper) {
        return GuardianGenerator.getGuardianEntity(
                guardianWrapper,
                OperationScene.communityRecovery,
                this,
                accountOriginalType,
                googleAccount,
                accountIdentifier
        );
    }

    public synchronized Optional<GuardianBehaviourEntity> nextWaitingGuardian() {
        if (isFulfilled()) {
            GLogger.t("you have reached the verify limit, there's no need to call nextWaitingGuardian() now.");
            return Optional.empty();
        }
        GuardianWrapper wrapper = guardians.stream()
                .filter(guardian -> !guardian.isVerified())
                .findFirst()
                .orElse(null);
        if (wrapper == null) {
            throw new AElfException(ResultCode.INTERNAL_ERROR, "No waiting guardian found");
        }
        return Optional.ofNullable(getGuardianBehaviourEntity(wrapper));
    }

    protected synchronized void setGuardianVerified(GuardianWrapper guardianWrapper) {
        guardians.stream()
                .filter(guardian -> guardian.getOriginalData().equals(guardianWrapper.getOriginalData()))
                .findFirst()
                .ifPresent(guardian -> guardian.setVerifiedData(guardianWrapper.getVerifiedData()));
    }

    public int getGuardianVerifyLimit() {
        return guardianVerifyLimit;
    }

    public synchronized int getFullFilledGuardianCount() {
        return (int) guardians.stream().filter(GuardianWrapper::isVerified).count();
    }

    public boolean isFulfilled() {
        return getFullFilledGuardianCount() >= guardianVerifyLimit;
    }

    @Override
    public void informGuardianReady(GuardianWrapper wrapper) {
        setGuardianVerified(wrapper);
    }

    @Override
    public SetPinBehaviourEntity afterVerified() throws AElfException {
        return new SetPinBehaviourEntity(this);
    }

    @Override
    public WalletBuildConfig buildConfig() throws AElfException {
        if (!isFulfilled()) {
            throw new AElfException(ResultCode.INTERNAL_ERROR, "guardian verify not fulfilled");
        }
        KeyPairInfo keyPairInfo = DataVerifyTools.generateKeyPairInfo();
        RegisterOrRecoveryResultDTO resultDTO = INetworkInterface.getInstance().requestRecovery(
                new RequestRecoveryParams()
                        .setChainId(GlobalConfig.getCurrentChainId())
                        .setManager(keyPairInfo.getAddress())
                        .setExtraData(
                                new Gson().toJson(new ExtraInfoWrapper(DeviceExtraInfo.fromPlatformEnum(ExtraDataPlatformEnum.OTHER)))
                        )
                        .setChainId(GlobalConfig.getCurrentChainId())
                        .setLoginGuardianIdentifier(accountIdentifier)
                        .setGuardiansApproved(
                                guardians.stream()
                                        .filter(GuardianWrapper::isVerified)
                                        .map(this::toApprovedGuardianDTO)
                                        .toArray(ApprovedGuardianDTO[]::new)
                        )
                        .setContext(new ContextDTO().setClientId(keyPairInfo.getAddress()))
        );
        AssertChecker.assertNotNull(resultDTO.getSessionId(), new AElfException(ResultCode.INTERNAL_ERROR, "requestRecovery failed"));
        ChainInfoDTO chainInfoDTO = INetworkInterface.getInstance().getGlobalChainInfo();
        AtomicReference<String> endPoint = new AtomicReference<>();
        Stream.of(chainInfoDTO.getItems())
                .filter(item -> GlobalConfig.getCurrentChainId().equals(item.getChainName()))
                .findFirst()
                .ifPresentOrElse(
                        item -> endPoint.set(item.getEndPoint()),
                        () -> endPoint.set(chainInfoDTO.getItems()[0].getEndPoint())
                );
        return new WalletBuildConfig()
                .setAElfEndpoint(endPoint.get())
                .setPrivKey(keyPairInfo.getPrivateKey())
                .setSessionId(resultDTO.getSessionId());
    }

    protected ApprovedGuardianDTO toApprovedGuardianDTO(GuardianWrapper wrapper) {
        return new ApprovedGuardianDTO()
                .setIdentifier(accountIdentifier)
                .setSignature(wrapper.getVerifiedData().getSignature())
                .setType(accountOriginalType.getValue())
                .setVerificationDoc(wrapper.getVerifiedData().getVerificationDoc())
                .setVerifierId(wrapper.getOriginalData().getVerifierId());
    }
}
