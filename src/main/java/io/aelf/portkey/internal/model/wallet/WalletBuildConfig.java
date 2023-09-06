package io.aelf.portkey.internal.model.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WalletBuildConfig {
    @JsonProperty("privKey")
    protected String privKey;
    @JsonProperty("sessionId")
    protected String sessionId;
    @JsonProperty("accountIdentifier")
    protected String accountIdentifier;
    @JsonProperty("fromRegister")
    protected boolean fromRegister;
    @JsonProperty("originalChainId")
    protected String originalChainId;

    public String getPrivKey() {
        return privKey;
    }

    public WalletBuildConfig setPrivKey(String privKey) {
        this.privKey = privKey;
        return this;
    }

    public String getSessionId() {
        return sessionId;
    }

    public WalletBuildConfig setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public String getAccountIdentifier() {
        return accountIdentifier;
    }

    public WalletBuildConfig setAccountIdentifier(String accountIdentifier) {
        this.accountIdentifier = accountIdentifier;
        return this;
    }

    public boolean isFromRegister() {
        return fromRegister;
    }

    public WalletBuildConfig setFromRegister(boolean fromRegister) {
        this.fromRegister = fromRegister;
        return this;
    }

    public String getOriginalChainId() {
        return originalChainId;
    }

    public WalletBuildConfig setOriginalChainId(String originalChainId) {
        this.originalChainId = originalChainId;
        return this;
    }
}
