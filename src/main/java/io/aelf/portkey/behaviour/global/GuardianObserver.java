package io.aelf.portkey.behaviour.global;

import io.aelf.portkey.internal.model.guardian.GuardianDTO;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeResultDTO;

@FunctionalInterface
public interface GuardianObserver {
    /**
     * Inform the target guardian is verified, so the observer can do further operations.
     *
     * @param resultDTO            The result of the verification.
     * @param originalGuardianInfo The original guardian info to mark which guardian is verified.
     */
    void informGuardianReady(HeadVerifyCodeResultDTO resultDTO, GuardianDTO originalGuardianInfo);
}
