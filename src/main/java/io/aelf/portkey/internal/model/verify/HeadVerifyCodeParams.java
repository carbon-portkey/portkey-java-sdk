package io.aelf.portkey.internal.model.verify;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.aelf.portkey.internal.model.common.OperationScene;
import io.aelf.portkey.internal.tools.DataVerifyTools;

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
     * @see DataVerifyTools#verifyChainId(String)
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

    public HeadVerifyCodeParams setVerifierSessionId(String verifierSessionId) {
        this.verifierSessionId = verifierSessionId;
        return this;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public HeadVerifyCodeParams setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
        return this;
    }

    public String getGuardianIdentifier() {
        return guardianIdentifier;
    }

    public HeadVerifyCodeParams setGuardianIdentifier(String guardianIdentifier) {
        this.guardianIdentifier = guardianIdentifier;
        return this;
    }

    public String getVerifierId() {
        return verifierId;
    }

    public HeadVerifyCodeParams setVerifierId(String verifierId) {
        this.verifierId = verifierId;
        return this;
    }

    public String getChainId() {
        return chainId;
    }

    public HeadVerifyCodeParams setChainId(String chainId) {
        DataVerifyTools.verifyChainId(chainId);
        this.chainId = chainId;
        return this;
    }

    public int getOperationType() {
        return operationType;
    }

    public HeadVerifyCodeParams setOperationType(int operationType) {
        DataVerifyTools.verifyOperationType(operationType);
        this.operationType = operationType;
        return this;
    }
}