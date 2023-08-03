package io.aelf.portkey.internal.model.register;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterHeader {

    public String getReCaptchaToken() {
        return reCaptchaToken;
    }

    public void setReCaptchaToken(String reCaptchaToken) {
        this.reCaptchaToken = reCaptchaToken;
    }

    @JsonProperty("reCaptchaToken")
    private String reCaptchaToken;

}