package io.aelf.portkey.internal.model.register;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VerifyCodeResultDTO {
    @JsonProperty("verificationDoc")
    private String verificationDoc;
    @JsonProperty("signature")
    private String signature;

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

/**
 * export type VerifyVerificationCodeResult = {
 *   verificationDoc: string;
 *   signature: string;
 * };
 */