package io.aelf.portkey.internal.model.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.aelf.portkey.internal.tools.DataVerifyTools;

public class GoogleVerifyTokenParams {
    @JsonProperty("accessToken")
    protected String accessToken;
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
        return accessToken;
    }

    public GoogleVerifyTokenParams setToken(String token) {
        this.accessToken = token;
        return this;
    }

    public String getVerifierId() {
        return verifierId;
    }

    public GoogleVerifyTokenParams setVerifierId(String verifierId) {
        this.verifierId = verifierId;
        return this;
    }

    public String getChainId() {
        return chainId;
    }

    public GoogleVerifyTokenParams setChainId(String chainId) {
        this.chainId = chainId;
        return this;
    }

    public int getOperationType() {
        return operationType;
    }

    public GoogleVerifyTokenParams setOperationType(int operationType) {
        this.operationType = operationType;
        return this;
    }
}
