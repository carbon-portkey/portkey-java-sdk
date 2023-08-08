package io.aelf.portkey.behaviour.global;

import io.aelf.portkey.internal.model.guardian.GuardianDTO;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeResultDTO;

public interface GuardianOperationObserver {
    void informGuardianReady(HeadVerifyCodeResultDTO resultDTO, GuardianDTO originalGuardianInfo);
}
