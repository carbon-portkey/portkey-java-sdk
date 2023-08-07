package io.aelf.portkey.internal.model.verify;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SendVerificationCodeResultDTO {
    @JsonProperty("verifierSessionId")
    private String verifierSessionId;

    public String getVerifierSessionId() {
        return verifierSessionId;
    }

    public void setVerifierSessionId(String verifierSessionId) {
        this.verifierSessionId = verifierSessionId;
    }
}
