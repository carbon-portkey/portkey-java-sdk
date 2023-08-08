package io.aelf.portkey.behaviour.guardian.state;

import io.aelf.portkey.behaviour.guardian.GuardianStateStub;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeResultDTO;
import io.aelf.utils.AElfException;

public class VerifiedGuardianState extends AbstractGuardianState{
    private final HeadVerifyCodeResultDTO headVerifyCodeResultDTO;

    public VerifiedGuardianState(GuardianStateStub stub, HeadVerifyCodeResultDTO resultDTO) {
        super(stub);

        this.headVerifyCodeResultDTO = resultDTO;
    }

    @Override
    public boolean sendVerificationCode() throws AElfException {
       throw new UnsupportedOperationException();
    }

    @Override
    public boolean verifyVerificationCode(String code) throws AElfException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isVerified() {
        return true;
    }

    @Override
    public void next() throws AElfException {
        stub.getObserver().informGuardianReady(headVerifyCodeResultDTO, stub.getOriginalGuardianInfo());
    }
}
