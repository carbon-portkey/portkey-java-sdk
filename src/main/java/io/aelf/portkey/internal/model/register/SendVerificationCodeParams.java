package io.aelf.portkey.internal.model.register;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SendVerificationCodeParams {
    @JsonProperty("type")
    private final String type;
    @JsonProperty("guardianIdentifier")
    private final String guardianIdentifier;
    @JsonProperty("verifierId")
    private final String verifierId;
    @JsonProperty("chainId")
    private final String chainId;

    @JsonProperty("operationType")
    private int operationType;

    public SendVerificationCodeParams(String type, String guardianIdentifier, String verifierId, String chainId) {
        this.type = type;
        this.guardianIdentifier = guardianIdentifier;
        this.verifierId = verifierId;
        this.chainId = chainId;
    }

    public String getType() {
        return type;
    }

    public String getGuardianIdentifier() {
        return guardianIdentifier;
    }

    public String getVerifierId() {
        return verifierId;
    }

    public String getChainId() {
        return chainId;
    }

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }
}
