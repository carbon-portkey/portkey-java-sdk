package io.aelf.portkey.internal.model.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RecoveryStatusItemDTO {

    @JsonProperty("caAddress")
    private String caAddress;
    @JsonProperty("caHash")
    private String caHash;
    @JsonProperty("recoveryStatus")
    private String recoveryStatus;
    @JsonProperty("recoveryMessage")
    private String recoveryMessage;

    public String getCaAddress() {
        return caAddress;
    }

    public RecoveryStatusItemDTO setCaAddress(String caAddress) {
        this.caAddress = caAddress;
        return this;
    }

    public String getCaHash() {
        return caHash;
    }

    public RecoveryStatusItemDTO setCaHash(String caHash) {
        this.caHash = caHash;
        return this;
    }

    public String getRecoveryStatus() {
        return recoveryStatus;
    }

    public RecoveryStatusItemDTO setRecoveryStatus(String recoveryStatus) {
        this.recoveryStatus = recoveryStatus;
        return this;
    }

    public String getRecoveryMessage() {
        return recoveryMessage;
    }

    public RecoveryStatusItemDTO setRecoveryMessage(String recoveryMessage) {
        this.recoveryMessage = recoveryMessage;
        return this;
    }
}
