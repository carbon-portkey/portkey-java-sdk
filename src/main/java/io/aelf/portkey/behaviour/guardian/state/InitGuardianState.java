package io.aelf.portkey.behaviour.guardian.state;

import io.aelf.portkey.behaviour.global.OperationNotFinishedException;
import io.aelf.portkey.behaviour.guardian.GuardianStateStub;
import io.aelf.portkey.internal.behaviour.GlobalConfig;
import io.aelf.portkey.internal.model.guardian.GuardianDTO;
import io.aelf.portkey.internal.model.verify.SendVerificationCodeParams;
import io.aelf.portkey.internal.model.verify.SendVerificationCodeResultDTO;
import io.aelf.portkey.network.connecter.NetworkService;
import io.aelf.utils.AElfException;

public class InitGuardianState extends AbstractGuardianState {
    public InitGuardianState(GuardianStateStub stub) {
        super(stub);
    }

    @Override
    public boolean sendVerificationCode() throws AElfException {
        GuardianDTO guardian = stub.getOriginalGuardianInfo();
        SendVerificationCodeParams params = new SendVerificationCodeParams()
                .setChainId(GlobalConfig.getCurrentChainId())
                .setOperationType(stub.getOperationType())
                .setGuardianIdentifier(guardian.getGuardianIdentifier())
                .setVerifierId(guardian.getVerifierId());
        SendVerificationCodeResultDTO resultDTO = NetworkService.getInstance().sendVerificationCode(params);
        if (resultDTO.isSuccess()) {
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
    public boolean isVerified() {
        return false;
    }

    @Override
    public void next() throws AElfException {
        throw new OperationNotFinishedException();
    }
}
