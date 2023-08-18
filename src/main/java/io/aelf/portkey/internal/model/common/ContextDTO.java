package io.aelf.portkey.internal.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class ContextDTO {
    @JsonProperty("requestId")
    private final String requestId = UUID.randomUUID().toString().replaceAll("-", "");
    @JsonProperty("ClientId")
    private String clientId;

    public String getClientId() {
        return clientId;
    }

    public ContextDTO setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getRequestId() {
        return requestId;
    }
}
