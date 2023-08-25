package io.aelf.portkey.behaviour.google_guardian;

import io.aelf.portkey.behaviour.global.GuardianObserver;
import io.aelf.portkey.behaviour.guardian.GuardianBehaviourEntity;
import io.aelf.portkey.internal.model.common.AccountOriginalType;
import io.aelf.portkey.internal.model.google.GoogleAccount;
import io.aelf.portkey.internal.model.guardian.GuardianDTO;
import org.jetbrains.annotations.NotNull;

public class GuardianGenerator {
    public static GuardianBehaviourEntity getGuardianEntity(
            @NotNull GuardianDTO guardian,
            int operationType,
            @NotNull GuardianObserver observer,
            AccountOriginalType accountOriginalType,
            GoogleAccount googleAccount
    ) {
        String type = guardian.getType();
        if (type.equals(AccountOriginalType.Google.name()) && googleAccount != null) {
            return new GoogleGuardianEntity(
                    guardian,
                    operationType,
                    observer,
                    accountOriginalType,
                    googleAccount
            );
        } else {
            return new GuardianBehaviourEntity(
                    guardian,
                    operationType,
                    observer,
                    accountOriginalType
            );
        }
    }
}
