package io.aelf.portkey.internal.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CountryCodeItemDTO {
    @JsonProperty("country")
    private String country;
    @JsonProperty("code")
    private String code;
    @JsonProperty("iso")
    private String iso;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }
}