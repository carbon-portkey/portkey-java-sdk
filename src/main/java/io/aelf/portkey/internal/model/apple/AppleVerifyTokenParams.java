package io.aelf.portkey.internal.model.apple;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.aelf.portkey.internal.tools.DataVerifyTools;

public class AppleVerifyTokenParams {
    @JsonProperty("identityToken")
    protected String identityToken;

    @JsonProperty("verifierId")
    private String verifierId;
    /**
     * @see DataVerifyTools#verifyChainId(String)
     */
    @JsonProperty("chainId")
    private String chainId;
    /**
     * @see DataVerifyTools#verifyAccountOriginType(int)
     */
    @JsonProperty("operationType")
    private int operationType;

    public String getToken() {
        return identityToken;
    }

    public AppleVerifyTokenParams setToken(String token) {
        this.identityToken = token;
        return this;
    }

    public String getVerifierId() {
        return verifierId;
    }

    public AppleVerifyTokenParams setVerifierId(String verifierId) {
        this.verifierId = verifierId;
        return this;
    }

    public String getChainId() {
        return chainId;
    }

    public AppleVerifyTokenParams setChainId(String chainId) {
        this.chainId = chainId;
        return this;
    }

    public int getOperationType() {
        return operationType;
    }

    public AppleVerifyTokenParams setOperationType(int operationType) {
        this.operationType = operationType;
        return this;
    }
}
