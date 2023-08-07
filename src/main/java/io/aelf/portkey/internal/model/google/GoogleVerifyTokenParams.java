package io.aelf.portkey.internal.model.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.aelf.portkey.internal.model.common.TokenVerifyOriginType;

public class GoogleVerifyTokenParams extends TokenVerifyOriginType {
    @JsonProperty("accessToken")
    protected String token;

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }
}
