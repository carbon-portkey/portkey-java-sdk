package io.aelf.portkey.internal.model.verify;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.aelf.portkey.internal.model.common.AccountOriginalType;
import io.aelf.portkey.internal.tools.DataVerifyTools;
import io.aelf.portkey.utils.enums.GoogleRecaptchaPlatformEnum;

public class SendVerificationCodeParams {
    /**
     * @see AccountOriginalType
     */
    @JsonProperty("type")
    private String type;
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
     * @see DataVerifyTools#verifyOperationType(int)
     */
    @JsonProperty("operationType")
    private int operationType;
    @JsonProperty("platformType")
    private final int platformType = initPlatformType();

    protected int initPlatformType() {
        return GoogleRecaptchaPlatformEnum.ANDROID.getValue();
    }


    public String getType() {
        return type;
    }

    /**
     * @see AccountOriginalType
     */
    public SendVerificationCodeParams setType(AccountOriginalType type) {
        DataVerifyTools.verifyAccountOriginType(type.getValue());
        this.type = type.name();
        return this;
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
        DataVerifyTools.verifyChainId(chainId);
        this.chainId = chainId;
        return this;
    }

    public int getOperationType() {
        return operationType;
    }

    public SendVerificationCodeParams setOperationType(int operationType) {
        DataVerifyTools.verifyOperationType(operationType);
        this.operationType = operationType;
        return this;
    }

    public int getPlatformType() {
        return platformType;
    }


}
