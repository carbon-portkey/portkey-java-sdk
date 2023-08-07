package io.aelf.portkey.internal.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.aelf.portkey.internal.behaviour.DataVerifyTools;

public abstract class TokenVerifyOriginType {
    @JsonProperty("verifierId")
    private String verifierId;
    /**
     * @see DataVerifyTools#verifyChainIdParams(String)
     */
    @JsonProperty("chainId")
    private String chainId;
    /**
     * @see DataVerifyTools#verifyAccountOriginType(int)
     */
    @JsonProperty("operationType")
    private int operationType;

    public abstract String getToken();

    public abstract void setToken(String token);

    public String getVerifierId() {
        return verifierId;
    }

    public TokenVerifyOriginType setVerifierId(String verifierId) {
        this.verifierId = verifierId;
        return this;
    }

    public String getChainId() {
        return chainId;
    }

    public TokenVerifyOriginType setChainId(String chainId) {
        DataVerifyTools.verifyChainIdParams(chainId);
        this.chainId = chainId;
        return this;
    }

    public int getOperationType() {
        return operationType;
    }

    public TokenVerifyOriginType setOperationType(int operationType) {
        DataVerifyTools.verifyAccountOriginType(operationType);
        this.operationType = operationType;
        return this;
    }

    /*
      See its subclass's annotation info for more information.
      <P>
      To avoid the problem of jackson's annotation inheritance, this field is not annotated.
     */
//    Protected String token;
}
