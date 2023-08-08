package io.aelf.portkey.behaviour.guardian;

import io.aelf.portkey.behaviour.global.GuardianOperationObserver;
import io.aelf.portkey.behaviour.guardian.state.IGuardianState;
import io.aelf.portkey.behaviour.guardian.state.InitGuardianState;
import io.aelf.portkey.internal.model.guardian.GuardianDTO;
import io.aelf.utils.AElfException;
import org.jetbrains.annotations.NotNull;

public class GuardianBehaviourEntity implements GuardianStateStub, IGuardianState {
    private final GuardianOperationObserver observer;
    private final GuardianDTO guardian;
    private final int operationType;
    private IGuardianState state;

    public GuardianBehaviourEntity(@NotNull GuardianDTO guardian,int operationType ,@NotNull GuardianOperationObserver observer) {
        this.observer = observer;
        this.guardian = guardian;
        this.operationType = operationType;
        this.state = new InitGuardianState(this);
    }

    @Override
    public void setNextState(IGuardianState state) {
        this.state = state;
    }

    @Override
    public GuardianOperationObserver getObserver() {
        return observer;
    }

    @Override
    public GuardianDTO getOriginalGuardianInfo() {
        return guardian;
    }

    @Override
    public int getOperationType() {
        return operationType;
    }

    @Override
    public boolean sendVerificationCode() throws AElfException {
        return state.sendVerificationCode();
    }

    @Override
    public boolean verifyVerificationCode(String code) throws AElfException {
        return state.verifyVerificationCode(code);
    }

    @Override
    public boolean isVerified() {
        return state.isVerified();
    }

    @Override
    public void next() throws AElfException {
        state.next();
    }
}
