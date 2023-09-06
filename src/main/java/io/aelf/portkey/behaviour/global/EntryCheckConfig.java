package io.aelf.portkey.behaviour.global;

import io.aelf.portkey.internal.model.common.AccountOriginalType;

public class EntryCheckConfig {
    /**
     * @see AccountOriginalType
     */
    private AccountOriginalType accountOriginalType;
    private String accountIdentifier;
    protected String originalChainId;

    public AccountOriginalType getAccountOriginalType() {
        return accountOriginalType;
    }

    public EntryCheckConfig setAccountOriginalType(AccountOriginalType accountOriginalType) {
        this.accountOriginalType = accountOriginalType;
        return this;
    }

    public String getAccountIdentifier() {
        return accountIdentifier;
    }

    public EntryCheckConfig setAccountIdentifier(String accountIdentifier) {
        this.accountIdentifier = accountIdentifier;
        return this;
    }

    public String getOriginalChainId() {
        return originalChainId;
    }

    public EntryCheckConfig setOriginalChainId(String originalChainId) {
        this.originalChainId = originalChainId;
        return this;
    }
}
