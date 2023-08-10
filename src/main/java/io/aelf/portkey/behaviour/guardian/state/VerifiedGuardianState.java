package io.aelf.portkey.behaviour.guardian.state;

import io.aelf.portkey.behaviour.global.AbstractStateSubject;
import io.aelf.portkey.behaviour.guardian.GuardianStub;
import io.aelf.portkey.internal.model.guardian.GuardianWrapper;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeResultDTO;
import io.aelf.utils.AElfException;

public class VerifiedGuardianState extends AbstractStateSubject<GuardianStub> implements IGuardianState {
    private final HeadVerifyCodeResultDTO headVerifyCodeResultDTO;

    public VerifiedGuardianState(GuardianStub stub, HeadVerifyCodeResultDTO resultDTO) {
        super(stub);
        this.headVerifyCodeResultDTO = resultDTO;
        this.next();
    }

    @Override
    public boolean verifyVerificationCode(String code) throws AElfException {
        throw new UnsupportedOperationException();
    }

    public void next() throws AElfException {
        stub.getObserver().informGuardianReady(
                new GuardianWrapper(
                        stub.getOriginalGuardianInfo(),
                        headVerifyCodeResultDTO
                )
        );
    }

    @Override
    public Stage getStage() {
        return Stage.VERIFIED;
    }
}
