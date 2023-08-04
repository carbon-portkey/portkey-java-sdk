package io.aelf.portkey.internal.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;

public class CountryCodeInfoDTO {
    @Nullable
    @JsonProperty("data")
    private CountryCodeItemDTO[] data;
    @Nullable
    @JsonProperty("locateData")
    private CountryCodeItemDTO locateData;

    public @Nullable CountryCodeItemDTO[] getData() {
        return data;
    }

    public void setData(@Nullable CountryCodeItemDTO[] data) {
        this.data = data;
    }

    public @Nullable CountryCodeItemDTO getLocateData() {
        return locateData;
    }

    public void setLocateData(@Nullable CountryCodeItemDTO locateData) {
        this.locateData = locateData;
    }
}
