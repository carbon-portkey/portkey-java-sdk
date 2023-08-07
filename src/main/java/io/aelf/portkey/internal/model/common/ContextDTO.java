package io.aelf.portkey.internal.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContextDTO {
    @JsonProperty("clientId")
    private String clientId;
    @JsonProperty("requestId")
    private String requestId;

    public String getClientId() {
        return clientId;
    }

    public String getRequestId() {
        return requestId;
    }
}
