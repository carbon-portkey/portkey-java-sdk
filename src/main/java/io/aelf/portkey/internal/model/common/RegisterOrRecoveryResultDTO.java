package io.aelf.portkey.internal.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterOrRecoveryResultDTO {
    @JsonProperty("sessionId")
    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}