package io.aelf.portkey.internal.model.google;

public class GoogleAccount {
    private String id;
    private String idToken;
    private String email;
    private String accessToken;

    public String getId() {
        return id;
    }

    public GoogleAccount setId(String id) {
        this.id = id;
        return this;
    }

    public String getIdToken() {
        return idToken;
    }

    public GoogleAccount setIdToken(String idToken) {
        this.idToken = idToken;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public GoogleAccount setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public GoogleAccount setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }
}
