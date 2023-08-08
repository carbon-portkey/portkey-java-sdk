package io.aelf.portkey.behaviour.guardian.state;

import io.aelf.portkey.behaviour.global.InvalidOperationException;
import io.aelf.portkey.behaviour.guardian.GuardianStateStub;
import io.aelf.portkey.internal.behaviour.GlobalConfig;
import io.aelf.portkey.internal.model.guardian.GuardianDTO;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeParams;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeResultDTO;
import io.aelf.portkey.internal.model.verify.SendVerificationCodeResultDTO;
import io.aelf.portkey.network.connecter.NetworkService;
import io.aelf.utils.AElfException;

public class SentVerificationState extends AbstractGuardianState {
    private final SendVerificationCodeResultDTO sendVerificationCodeResult;

    public SentVerificationState(GuardianStateStub stub, SendVerificationCodeResultDTO resultDTO){
        super(stub);
        this.sendVerificationCodeResult = resultDTO;
    }

    @Override
    public boolean sendVerificationCode() throws AElfException {
        throw new InvalidOperationException();
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
        HeadVerifyCodeResultDTO resultDTO=NetworkService.getInstance().checkVerificationCode(config);
        if (resultDTO.isSuccess()) {
            stub.setNextState(new VerifiedGuardianState(stub, resultDTO));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isVerified() {
        return false;
    }

    @Override
    public void next() throws AElfException {

    }
}
