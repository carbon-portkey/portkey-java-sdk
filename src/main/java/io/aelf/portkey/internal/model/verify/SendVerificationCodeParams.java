package io.aelf.portkey.internal.model.verify;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.aelf.portkey.internal.behaviour.DataVerifyTools;
import io.aelf.portkey.internal.model.common.AccountOriginalType;

public class SendVerificationCodeParams {
    /**
     * @see AccountOriginalType
     */
    @JsonProperty("type")
    private int type;
    @JsonProperty("guardianIdentifier")
    private String guardianIdentifier;
    @JsonProperty("verifierId")
    private String verifierId;
    @JsonProperty("chainId")
    private String chainId;

    @JsonProperty("operationType")
    private int operationType;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        DataVerifyTools.verifyAccountOriginType(type);
        this.type = type;
    }

    public String getGuardianIdentifier() {
        return guardianIdentifier;
    }

    public SendVerificationCodeParams setGuardianIdentifier(String guardianIdentifier) {
        this.guardianIdentifier = guardianIdentifier;
        return this;
    }

    public String getVerifierId() {
        return verifierId;
    }

    public SendVerificationCodeParams setVerifierId(String verifierId) {
        this.verifierId = verifierId;
        return this;
    }

    public String getChainId() {
        return chainId;
    }

    public SendVerificationCodeParams setChainId(String chainId) {
        this.chainId = chainId;
        return this;
    }

    public int getOperationType() {
        return operationType;
    }

    public SendVerificationCodeParams setOperationType(int operationType) {
        this.operationType = operationType;
        return this;
    }
}
