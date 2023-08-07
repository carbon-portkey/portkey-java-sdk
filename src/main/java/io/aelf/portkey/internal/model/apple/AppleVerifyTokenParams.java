package io.aelf.portkey.internal.model.apple;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.aelf.portkey.internal.model.common.TokenVerifyOriginType;

public class AppleVerifyTokenParams extends TokenVerifyOriginType {
    @JsonProperty("identityToken")
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
