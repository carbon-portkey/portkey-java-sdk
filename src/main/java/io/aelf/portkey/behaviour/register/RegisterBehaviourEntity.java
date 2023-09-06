package io.aelf.portkey.behaviour.register;

import com.google.gson.Gson;
import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.portkey.behaviour.global.EntryCheckConfig;
import io.aelf.portkey.behaviour.global.GuardianObserver;
import io.aelf.portkey.behaviour.guardian.GuardianBehaviourEntity;
import io.aelf.portkey.behaviour.guardian.GuardianGenerator;
import io.aelf.portkey.behaviour.pin.IAfterVerifiedBehaviour;
import io.aelf.portkey.behaviour.pin.SetPinBehaviourEntity;
import io.aelf.portkey.internal.model.common.ContextDTO;
import io.aelf.portkey.internal.model.common.OperationScene;
import io.aelf.portkey.internal.model.common.RegisterOrRecoveryResultDTO;
import io.aelf.portkey.internal.model.extraInfo.DeviceExtraInfo;
import io.aelf.portkey.internal.model.extraInfo.ExtraInfoWrapper;
import io.aelf.portkey.internal.model.google.GoogleAccount;
import io.aelf.portkey.internal.model.guardian.GetRecommendGuardianResultDTO;
import io.aelf.portkey.internal.model.guardian.GetRecommendationVerifierParams;
import io.aelf.portkey.internal.model.guardian.GuardianDTO;
import io.aelf.portkey.internal.model.guardian.GuardianWrapper;
import io.aelf.portkey.internal.model.register.RequestRegisterParams;
import io.aelf.portkey.internal.model.wallet.WalletBuildConfig;
import io.aelf.portkey.internal.tools.DataVerifyTools;
import io.aelf.portkey.internal.tools.GlobalConfig;
import io.aelf.portkey.network.connecter.INetworkInterface;
import io.aelf.portkey.utils.enums.ExtraDataPlatformEnum;
import io.aelf.response.ResultCode;
import io.aelf.schemas.KeyPairInfo;
import io.aelf.utils.AElfException;
import org.jetbrains.annotations.Nullable;

public class RegisterBehaviourEntity implements GuardianObserver, IAfterVerifiedBehaviour {
    private final EntryCheckConfig config;
    private GuardianWrapper guardianWrapper;
    private final GoogleAccount googleAccount;
    protected String originalChainId;


    public RegisterBehaviourEntity(EntryCheckConfig config, @Nullable GoogleAccount googleAccount) {
        this.config = config;
        this.googleAccount = googleAccount;
        this.originalChainId = config.getOriginalChainId();
    }

    public GuardianBehaviourEntity getGuardian() {
        GetRecommendGuardianResultDTO resultDTO = INetworkInterface.getInstance().getRecommendGuardian(
                new GetRecommendationVerifierParams()
                        .setChainId(GlobalConfig.getCurrentChainId())
        );
        GuardianWrapper guardianWrapper = new GuardianWrapper(
                new GuardianDTO()
                        .setGuardianIdentifier(config.getAccountIdentifier())
                        .setLoginGuardian(true)
                        .setType(config.getAccountOriginalType().name())
                        .setVerifierId(resultDTO.getId())
                        .setId(resultDTO.getId())
                        .setName(resultDTO.getName())
                        .setImageUrl(resultDTO.getImageUrl()),
                googleAccount
        );
        this.guardianWrapper = guardianWrapper;
        return GuardianGenerator.getGuardianEntity(
                guardianWrapper,
                OperationScene.register,
                this,
                config.getAccountOriginalType(),
                googleAccount,
                config.getAccountIdentifier()
        );
    }

    public EntryCheckConfig getConfig() {
        return config;
    }

    public GuardianWrapper getGuardianWrapper() {
        return guardianWrapper;
    }

    @Override
    public SetPinBehaviourEntity afterVerified() {
        return new SetPinBehaviourEntity(this);
    }

    @Override
    public WalletBuildConfig buildConfig() throws AElfException {
        if (!isVerified()) throw new AElfException(ResultCode.INTERNAL_ERROR, "Guardian not verified");
        KeyPairInfo keyPairInfo = DataVerifyTools.generateKeyPairInfo();
        RegisterOrRecoveryResultDTO resultDTO = INetworkInterface.getInstance().requestRegister(
                new RequestRegisterParams()
                        .setLoginGuardianIdentifier(config.getAccountIdentifier())
                        .setVerificationDoc(guardianWrapper.getVerifiedData().getVerificationDoc())
                        .setSignature(guardianWrapper.getVerifiedData().getSignature())
                        .setContext(new ContextDTO().setClientId(keyPairInfo.getAddress()))
                        .setType(config.getAccountOriginalType().name())
                        .setChainId(GlobalConfig.getCurrentChainId())
                        .setManager(keyPairInfo.getAddress())
                        .setVerifierId(guardianWrapper.getOriginalData().getVerifierId())
                        .setExtraData(new Gson().toJson(new ExtraInfoWrapper(DeviceExtraInfo.fromPlatformEnum(ExtraDataPlatformEnum.OTHER))))
        );
        AssertChecker.assertNotNull(resultDTO.getSessionId(), new AElfException(ResultCode.INTERNAL_ERROR, "requestRecovery failed"));
        return new WalletBuildConfig()
                .setPrivKey(keyPairInfo.getPrivateKey())
                .setSessionId(resultDTO.getSessionId())
                .setAccountIdentifier(config.getAccountIdentifier())
                .setFromRegister(true)
                .setOriginalChainId(originalChainId)
                ;
    }


    public boolean isVerified() {
        return guardianWrapper != null && guardianWrapper.getVerifiedData() != null;
    }

    @Override
    public void informGuardianReady(GuardianWrapper guardianWrapper) {
        this.guardianWrapper = guardianWrapper;
    }
}
