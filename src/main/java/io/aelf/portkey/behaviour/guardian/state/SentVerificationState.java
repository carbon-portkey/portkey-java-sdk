package io.aelf.portkey.behaviour.guardian.state;

import io.aelf.portkey.behaviour.global.AbstractStateSubject;
import io.aelf.portkey.behaviour.guardian.GuardianStub;
import io.aelf.portkey.internal.model.guardian.GuardianDTO;
import io.aelf.portkey.internal.model.register.RegisterHeader;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeParams;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeResultDTO;
import io.aelf.portkey.internal.model.verify.SendVerificationCodeParams;
import io.aelf.portkey.internal.model.verify.SendVerificationCodeResultDTO;
import io.aelf.portkey.internal.tools.GlobalConfig;
import io.aelf.portkey.network.connecter.INetworkInterface;
import io.aelf.response.ResultCode;
import io.aelf.utils.AElfException;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SentVerificationState extends AbstractStateSubject<GuardianStub> implements IGuardianState {
    private final SendVerificationCodeResultDTO sendVerificationCodeResult;

    public SentVerificationState(GuardianStub stub, SendVerificationCodeResultDTO resultDTO) {
        super(stub);
        this.sendVerificationCodeResult = resultDTO;
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
        if (resultDTO != null && resultDTO.isSuccess()) {
            stub.setNextState(new SentVerificationState(stub, resultDTO));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean verifyVerificationCode(String code) throws AElfException {
        GuardianDTO guardian = stub.getOriginalGuardianInfo();
        HeadVerifyCodeParams config = new HeadVerifyCodeParams()
                .setChainId(GlobalConfig.getCurrentChainId())
                .setOperationType(stub.getOperationType())
                .setGuardianIdentifier(guardian.getGuardianIdentifier())
                .setVerifierId(guardian.getVerifierId())
                .setVerifierSessionId(sendVerificationCodeResult.getVerifierSessionId())
                .setVerificationCode(code);
        try{
            HeadVerifyCodeResultDTO resultDTO = INetworkInterface.getInstance().checkVerificationCode(config);
            if (resultDTO != null && resultDTO.isSuccess()) {
                stub.setNextState(new VerifiedGuardianState(stub, resultDTO));
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            throw new AElfException(ResultCode.INTERNAL_ERROR, e.getMessage());
        }
    }

    @Override
    public Stage getStage() {
        return Stage.SENT;
    }
}
