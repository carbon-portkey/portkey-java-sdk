package io.aelf.portkey.behaviour.google_guardian;

import io.aelf.portkey.behaviour.global.GuardianObserver;
import io.aelf.portkey.behaviour.guardian.GuardianBehaviourEntity;
import io.aelf.portkey.internal.model.common.AccountOriginalType;
import io.aelf.portkey.internal.model.google.GoogleAccount;
import io.aelf.portkey.internal.model.guardian.GuardianDTO;
import io.aelf.portkey.internal.model.guardian.GuardianWrapper;
import org.jetbrains.annotations.NotNull;

public class GuardianGenerator {
    public static GuardianBehaviourEntity getGuardianEntity(
            @NotNull GuardianWrapper guardian,
            int operationType,
            @NotNull GuardianObserver observer,
            AccountOriginalType accountOriginalType,
            GoogleAccount googleAccount
    ) {
        GuardianDTO original=guardian.getOriginalData();
        String type = original.getType();
        if (AccountOriginalType.Google.name().equals(type) && googleAccount != null) {
            return new GoogleGuardianEntity(
                    original,
                    operationType,
                    observer,
                    accountOriginalType,
                    guardian.isVerified(),
                    googleAccount
            );
        } else {
            return new GuardianBehaviourEntity(
                    original,
                    operationType,
                    observer,
                    accountOriginalType,
                    guardian.isVerified()
            );
        }
    }
}
