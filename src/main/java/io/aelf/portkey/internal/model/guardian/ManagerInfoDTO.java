package io.aelf.portkey.internal.model.guardian;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ManagerInfoDTO {

    @JsonProperty("address")
    private String address;
    @JsonProperty("extraData")
    private String extraData;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public @Nullable String getExtraData() {
        return extraData;
    }

    public void setExtraData(@Nullable String extraData) {
        this.extraData = extraData;
    }
}
