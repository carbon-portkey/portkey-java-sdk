package io.aelf.portkey.behaviour.guardian;

import io.aelf.portkey.behaviour.global.GuardianObserver;
import io.aelf.portkey.behaviour.global.IStateEntity;
import io.aelf.portkey.behaviour.guardian.state.IGuardianState;
import io.aelf.portkey.internal.model.guardian.GuardianDTO;

public abstract class GuardianStateStub extends IStateEntity<IGuardianState> {
    private final GuardianObserver observer;
    private final int operationType;
    private final GuardianDTO guardian;

    protected GuardianStateStub(GuardianObserver observer, int operationType, GuardianDTO guardian) {
        super();
        this.observer = observer;
        this.operationType = operationType;
        this.guardian = guardian;
    }

    public GuardianObserver getObserver() {
        return this.observer;
    }

    public GuardianDTO getOriginalGuardianInfo() {
        return this.guardian;
    }

    public int getOperationType() {
        return this.operationType;
    }
}
