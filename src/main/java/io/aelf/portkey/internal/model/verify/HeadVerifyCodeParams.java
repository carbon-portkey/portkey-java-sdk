package io.aelf.portkey.internal.model.verify;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.aelf.portkey.internal.behaviour.DataVerifyTools;
import io.aelf.portkey.internal.model.common.OperationScene;

public class HeadVerifyCodeParams {

    @JsonProperty("verifierSessionId")
    private String verifierSessionId;
    @JsonProperty("verificationCode")
    private String verificationCode;
    @JsonProperty("guardianIdentifier")
    private String guardianIdentifier;
    @JsonProperty("verifierId")
    private String verifierId;
    /**
     * @see DataVerifyTools#verifyChainIdParams(String)
     */
    @JsonProperty("chainId")
    private String chainId;
    /**
     * @see OperationScene
     */
    @JsonProperty("operationType")
    private int operationType;

    public String getVerifierSessionId() {
        return verifierSessionId;
    }

    public void setVerifierSessionId(String verifierSessionId) {
        this.verifierSessionId = verifierSessionId;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public void setGuardianIdentifier(String guardianIdentifier) {
        this.guardianIdentifier = guardianIdentifier;
    }

    public String getGuardianIdentifier() {
        return guardianIdentifier;
    }

    public void setVerifierId(String verifierId) {
        this.verifierId = verifierId;
    }

    public String getVerifierId() {
        return verifierId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public String getChainId() {
        DataVerifyTools.verifyChainIdParams(chainId);
        return chainId;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }

    public int getOperationType() {
        return operationType;
    }
}