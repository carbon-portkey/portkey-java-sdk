package io.aelf.portkey.behaviour.guardian.state;

import io.aelf.portkey.behaviour.global.AbstractStateSubject;
import io.aelf.portkey.behaviour.guardian.GuardianStub;
import io.aelf.portkey.internal.model.guardian.GuardianDTO;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeParams;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeResultDTO;
import io.aelf.portkey.internal.model.verify.SendVerificationCodeResultDTO;
import io.aelf.portkey.internal.tools.GlobalConfig;
import io.aelf.portkey.network.connecter.INetworkInterface;
import io.aelf.utils.AElfException;

public class SentVerificationState extends AbstractStateSubject<GuardianStub> implements IGuardianState {
    private final SendVerificationCodeResultDTO sendVerificationCodeResult;

    public SentVerificationState(GuardianStub stub, SendVerificationCodeResultDTO resultDTO) {
        super(stub);
        this.sendVerificationCodeResult = resultDTO;
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
        HeadVerifyCodeResultDTO resultDTO = INetworkInterface.getInstance().checkVerificationCode(config);
        if (resultDTO.isSuccess()) {
            stub.setNextState(new VerifiedGuardianState(stub, resultDTO));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Stage getStage() {
        return Stage.SENT;
    }
}
