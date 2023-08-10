package io.aelf.portkey.behaviour.entry;

import io.aelf.portkey.behaviour.register.RegisterBehaviourEntity;

@FunctionalInterface
public interface RegisterCallback {
    void onRegisterStep(RegisterBehaviourEntity entity);
}
