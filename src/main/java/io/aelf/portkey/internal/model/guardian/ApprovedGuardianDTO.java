package io.aelf.portkey.internal.model.guardian;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    public ApprovedGuardianDTO setType(int type) {
        this.type = type;
        return this;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ApprovedGuardianDTO setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public String getVerifierId() {
        return verifierId;
    }

    public ApprovedGuardianDTO setVerifierId(String verifierId) {
        this.verifierId = verifierId;
        return this;
    }

    public String getVerificationDoc() {
        return verificationDoc;
    }

    public ApprovedGuardianDTO setVerificationDoc(String verificationDoc) {
        this.verificationDoc = verificationDoc;
        return this;
    }

    public String getSignature() {
        return signature;
    }

    public ApprovedGuardianDTO setSignature(String signature) {
        this.signature = signature;
        return this;
    }
}
