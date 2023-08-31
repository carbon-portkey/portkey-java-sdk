package io.aelf.portkey.internal.model.google;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GoogleAuthResult {
    @JsonProperty("access_token")
    private String access_token;

    @JsonProperty("token_type")
    private String token_type;

    @JsonProperty("expires_in")
    private long expires_in;

    @JsonProperty("id_token")
    private String id_token;

    @JsonProperty("scope")
    private String scope;

    public String getAccess_token() {
        return access_token;
    }

    public GoogleAuthResult setAccess_token(String access_token) {
        this.access_token = access_token;
        return this;
    }

    public String getToken_type() {
        return token_type;
    }

    public GoogleAuthResult setToken_type(String token_type) {
        this.token_type = token_type;
        return this;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public GoogleAuthResult setExpires_in(long expires_in) {
        this.expires_in = expires_in;
        return this;
    }

    public String getId_token() {
        return id_token;
    }

    public GoogleAuthResult setId_token(String id_token) {
        this.id_token = id_token;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public GoogleAuthResult setScope(String scope) {
        this.scope = scope;
        return this;
    }
}
