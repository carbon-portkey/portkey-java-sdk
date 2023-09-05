package io.aelf.portkey.behaviour.guardian;

import io.aelf.portkey.behaviour.global.GuardianObserver;
import io.aelf.portkey.behaviour.global.IStateEntity;
import io.aelf.portkey.behaviour.guardian.state.IGuardianState;
import io.aelf.portkey.internal.model.common.AccountOriginalType;
import io.aelf.portkey.internal.model.guardian.GuardianDTO;

public abstract class GuardianStub extends IStateEntity<IGuardianState> {
    private final GuardianObserver observer;
    private final int operationType;
    private final GuardianDTO guardian;
    private final AccountOriginalType accountOriginalType;
    protected final boolean isAlreadyVerified;
    protected final String accountIdentifier;

    protected GuardianStub(
            GuardianObserver observer,
            int operationType,
            GuardianDTO guardian,
            AccountOriginalType accountOriginalType,
            boolean isAlreadyVerified,
            String accountIdentifier
    ) {
        super();
        this.observer = observer;
        this.operationType = operationType;
        this.guardian = guardian;
        this.accountOriginalType = accountOriginalType;
        this.isAlreadyVerified = isAlreadyVerified;
        this.accountIdentifier = accountIdentifier;
    }

    public GuardianObserver getObserver() {
        return this.observer;
    }

    public GuardianDTO getOriginalGuardianInfo() {
        return this.guardian;
    }

    public int getOperationType() {
        return this.operationType;
    }

    public AccountOriginalType getAccountOriginalType() {
        return accountOriginalType;
    }

    public String getAccountIdentifier() {
        return accountIdentifier;
    }
}
