package io.aelf.portkey.behaviour.guardian;

import io.aelf.portkey.behaviour.global.GuardianObserver;
import io.aelf.portkey.behaviour.guardian.state.IGuardianState;
import io.aelf.portkey.behaviour.guardian.state.InitGuardianState;
import io.aelf.portkey.internal.model.guardian.GuardianDTO;
import io.aelf.utils.AElfException;
import org.jetbrains.annotations.NotNull;

public class GuardianBehaviourEntity extends GuardianStateStub implements IGuardianState {

    public GuardianBehaviourEntity(@NotNull GuardianDTO guardian, int operationType, @NotNull GuardianObserver observer) {
        super(observer, operationType, guardian);
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

    @Override
    protected @NotNull IGuardianState getInitialState() {
        return new InitGuardianState(this);
    }
}
