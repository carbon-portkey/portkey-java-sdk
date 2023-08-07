package io.aelf.portkey.internal.model.guardian;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.aelf.portkey.internal.behaviour.DataVerifyTools;
import io.aelf.portkey.internal.model.common.AccountOriginalType;

public class ApprovedGuardianDTO {
    /**
     * @see AccountOriginalType
     */
    @JsonProperty("type")
    private int type;
    @JsonProperty("identifier")
    private String identifier;
    @JsonProperty("verifierId")
    private String verifierId;
    @JsonProperty("verificationDoc")
    private String verificationDoc;
    @JsonProperty("signature")
    private String signature;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        DataVerifyTools.verifyAccountOriginType(type);
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getVerifierId() {
        return verifierId;
    }

    public void setVerifierId(String verifierId) {
        this.verifierId = verifierId;
    }

    public String getVerificationDoc() {
        return verificationDoc;
    }

    public void setVerificationDoc(String verificationDoc) {
        this.verificationDoc = verificationDoc;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
