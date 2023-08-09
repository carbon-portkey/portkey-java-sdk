package io.aelf.portkey.behaviour.login;

import io.aelf.portkey.behaviour.login.state.ILoginState;
import org.jetbrains.annotations.NotNull;

public class LoginBehaviourEntity extends LoginStateStub implements ILoginState {

    @Override
    protected @NotNull ILoginState getInitialState() {
        return null;
    }

    @Override
    public boolean isComplete() {
        return false;
    }
}
