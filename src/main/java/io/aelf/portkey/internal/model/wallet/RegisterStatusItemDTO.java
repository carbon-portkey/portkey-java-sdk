package io.aelf.portkey.internal.model.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterStatusItemDTO {
    @JsonProperty("caAddress")
    private String caAddress;
    @JsonProperty("caHash")
    private String caHash;
    @JsonProperty("registerStatus")
    private String registerStatus;
    @JsonProperty("registerMessage")
    private String registerMessage;

    public String getCaAddress() {
        return caAddress;
    }

    public RegisterStatusItemDTO setCaAddress(String caAddress) {
        this.caAddress = caAddress;
        return this;
    }

    public String getCaHash() {
        return caHash;
    }

    public RegisterStatusItemDTO setCaHash(String caHash) {
        this.caHash = caHash;
        return this;
    }

    public String getRegisterStatus() {
        return registerStatus;
    }

    public RegisterStatusItemDTO setRegisterStatus(String registerStatus) {
        this.registerStatus = registerStatus;
        return this;
    }

    public String getRegisterMessage() {
        return registerMessage;
    }

    public RegisterStatusItemDTO setRegisterMessage(String registerMessage) {
        this.registerMessage = registerMessage;
        return this;
    }
}
