package io.aelf.portkey.internal.model.register;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterHeader {

    @JsonProperty("reCaptchaToken")
    private String reCaptchaToken;

    public String getReCaptchaToken() {
        return reCaptchaToken;
    }

    public RegisterHeader setReCaptchaToken(String reCaptchaToken) {
        this.reCaptchaToken = reCaptchaToken;
        return this;
    }

}