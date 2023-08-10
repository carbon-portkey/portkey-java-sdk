package io.aelf.portkey.behaviour.entry;

import io.aelf.portkey.behaviour.login.LoginBehaviourEntity;

@FunctionalInterface
public interface LoginCallback {
    void onLoginStep(LoginBehaviourEntity entity);
}
