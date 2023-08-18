package io.aelf.portkey.behaviour.guardian.state;

import io.aelf.portkey.behaviour.global.AbstractStateSubject;
import io.aelf.portkey.behaviour.global.OperationNotFinishedException;
import io.aelf.portkey.behaviour.guardian.GuardianStub;
import io.aelf.portkey.internal.model.guardian.GuardianDTO;
import io.aelf.portkey.internal.model.register.RegisterHeader;
import io.aelf.portkey.internal.model.verify.SendVerificationCodeParams;
import io.aelf.portkey.internal.model.verify.SendVerificationCodeResultDTO;
import io.aelf.portkey.internal.tools.GlobalConfig;
import io.aelf.portkey.network.connecter.INetworkInterface;
import io.aelf.utils.AElfException;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InitGuardianState extends AbstractStateSubject<GuardianStub> implements IGuardianState {
    public InitGuardianState(GuardianStub stub) {
        super(stub);
    }

    @Override
    public boolean sendVerificationCode() throws AElfException {
        return innerSendVerificationCode(null);
    }

    @Override
    public boolean sendVerificationCode(@NotNull String recaptchaToken) throws AElfException {
        return innerSendVerificationCode(recaptchaToken);
    }

    private boolean innerSendVerificationCode(@Nullable String recaptchaToken) throws AElfException {
        GuardianDTO guardian = stub.getOriginalGuardianInfo();
        SendVerificationCodeParams params = new SendVerificationCodeParams()
                .setType(stub.getAccountOriginalType())
                .setChainId(GlobalConfig.getCurrentChainId())
                .setOperationType(stub.getOperationType())
                .setGuardianIdentifier(guardian.getGuardianIdentifier())
                .setVerifierId(guardian.getVerifierId());
        INetworkInterface networkInterface = INetworkInterface.getInstance();
        SendVerificationCodeResultDTO resultDTO =
                TextUtils.isEmpty(recaptchaToken)
                        ? networkInterface.sendVerificationCode(params)
                        : networkInterface.sendVerificationCode(params, new RegisterHeader().setReCaptchaToken(recaptchaToken));
        if (resultDTO!=null && resultDTO.isSuccess()) {
            stub.setNextState(new SentVerificationState(stub, resultDTO));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean verifyVerificationCode(String code) throws AElfException {
        throw new OperationNotFinishedException();
    }

    @Override
    public Stage getStage() {
        return Stage.INIT;
    }
}
