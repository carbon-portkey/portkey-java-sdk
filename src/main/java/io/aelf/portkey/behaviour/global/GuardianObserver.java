package io.aelf.portkey.behaviour.global;

import io.aelf.portkey.internal.model.guardian.GuardianWrapper;

@FunctionalInterface
public interface GuardianObserver {
    /**
     * Inform the target guardian is verified, so the observer can do further operations.
     *
     * @param guardianWrapper an object with original guardian info and verified data.
     */
    void informGuardianReady(GuardianWrapper guardianWrapper);
}
