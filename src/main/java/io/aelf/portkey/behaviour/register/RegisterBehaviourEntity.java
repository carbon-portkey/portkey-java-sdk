package io.aelf.portkey.behaviour.register;

import com.google.gson.Gson;
import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.portkey.behaviour.global.EntryCheckConfig;
import io.aelf.portkey.behaviour.global.GuardianObserver;
import io.aelf.portkey.behaviour.guardian.GuardianBehaviourEntity;
import io.aelf.portkey.behaviour.pin.IAfterVerifiedBehaviour;
import io.aelf.portkey.behaviour.pin.SetPinBehaviourEntity;
import io.aelf.portkey.internal.model.common.ChainInfoDTO;
import io.aelf.portkey.internal.model.common.ContextDTO;
import io.aelf.portkey.internal.model.common.OperationScene;
import io.aelf.portkey.internal.model.common.RegisterOrRecoveryResultDTO;
import io.aelf.portkey.internal.model.extraInfo.DeviceExtraInfo;
import io.aelf.portkey.internal.model.extraInfo.ExtraInfoWrapper;
import io.aelf.portkey.internal.model.guardian.GetRecommendGuardianResultDTO;
import io.aelf.portkey.internal.model.guardian.GetRecommendationVerifierParams;
import io.aelf.portkey.internal.model.guardian.GuardianDTO;
import io.aelf.portkey.internal.model.guardian.GuardianWrapper;
import io.aelf.portkey.internal.model.register.RequestRegisterParams;
import io.aelf.portkey.internal.model.wallet.WalletBuildConfig;
import io.aelf.portkey.internal.tools.GlobalConfig;
import io.aelf.portkey.network.connecter.INetworkInterface;
import io.aelf.portkey.utils.enums.Platform;
import io.aelf.response.ResultCode;
import io.aelf.schemas.KeyPairInfo;
import io.aelf.utils.AElfException;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class RegisterBehaviourEntity implements GuardianObserver, IAfterVerifiedBehaviour {
    private final EntryCheckConfig config;
    private GuardianWrapper guardianWrapper;

    public RegisterBehaviourEntity(EntryCheckConfig config) {
        this.config = config;
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
        );
        return new GuardianBehaviourEntity(
                guardianWrapper.getOriginalData(),
                OperationScene.register,
                this,
                config.getAccountOriginalType()
        );
    }

    @Override
    public SetPinBehaviourEntity afterVerified() {
        if (!isVerified()) throw new AElfException(ResultCode.INTERNAL_ERROR, "Guardian not verified");
        KeyPairInfo keyPairInfo = new KeyPairInfo();
        RegisterOrRecoveryResultDTO resultDTO = INetworkInterface.getInstance().requestRegister(
                new RequestRegisterParams()
                        .setLoginGuardianIdentifier(config.getAccountIdentifier())
                        .setVerificationDoc(guardianWrapper.getVerifiedData().getVerificationDoc())
                        .setSignature(guardianWrapper.getVerifiedData().getSignature())
                        .setContext(new ContextDTO().setClientId(keyPairInfo.getAddress()))
                        .setType(config.getAccountOriginalType().name())
                        .setManager(keyPairInfo.getAddress())
                        .setVerifierId(guardianWrapper.getOriginalData().getVerifierId())
                        .setExtraData(new Gson().toJson(new ExtraInfoWrapper(DeviceExtraInfo.fromPlatformEnum(Platform.OTHER))))
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
        return new SetPinBehaviourEntity(
                new WalletBuildConfig()
                        .setAElfEndpoint(endPoint.get())
                        .setPrivKey(keyPairInfo.getPrivateKey())
                        .setSessionId(resultDTO.getSessionId())
        );
    }

    public boolean isVerified() {
        return guardianWrapper.getVerifiedData() != null;
    }

    @Override
    public void informGuardianReady(GuardianWrapper guardianWrapper) {
        this.guardianWrapper = guardianWrapper;
    }
}
