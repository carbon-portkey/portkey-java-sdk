package io.aelf.portkey.internal.model.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WalletBuildConfig {
    @JsonProperty("privKey")
    protected String privKey;
    @JsonProperty("sessionId")
    protected String sessionId;
    @JsonProperty("AElfEndpoint")
    protected String AElfEndpoint;
    @JsonProperty("originalChainId")
    protected String originalChainId;

    public String getAElfEndpoint() {
        return AElfEndpoint;
    }

    public WalletBuildConfig setAElfEndpoint(String AElfEndpoint) {
        this.AElfEndpoint = AElfEndpoint;
        return this;
    }

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

    public String getOriginalChainId() {
        return originalChainId;
    }

    public WalletBuildConfig setOriginalChainId(String originalChainId) {
        this.originalChainId = originalChainId;
        return this;
    }
}
