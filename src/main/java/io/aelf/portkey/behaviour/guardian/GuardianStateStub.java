package io.aelf.portkey.behaviour.guardian;

import io.aelf.portkey.behaviour.global.GuardianOperationObserver;
import io.aelf.portkey.behaviour.guardian.state.IGuardianState;
import io.aelf.portkey.internal.model.guardian.GuardianDTO;

public interface GuardianStateStub {
   void setNextState(IGuardianState state);
   GuardianOperationObserver getObserver();
   GuardianDTO getOriginalGuardianInfo();
   int getOperationType();
}
