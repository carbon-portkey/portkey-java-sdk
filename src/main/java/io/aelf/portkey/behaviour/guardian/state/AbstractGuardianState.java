package io.aelf.portkey.behaviour.guardian.state;

import io.aelf.portkey.behaviour.guardian.GuardianStateStub;

public abstract class AbstractGuardianState implements IGuardianState{
    protected final GuardianStateStub stub;

    public AbstractGuardianState(GuardianStateStub stub) {
        this.stub = stub;
    }

}
