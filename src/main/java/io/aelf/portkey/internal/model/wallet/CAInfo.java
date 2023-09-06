package io.aelf.portkey.internal.model.wallet;

public class CAInfo {
    private String caAddress;
    private String caHash;

    public String getCaAddress() {
        return caAddress;
    }

    public CAInfo setCaAddress(String caAddress) {
        this.caAddress = caAddress;
        return this;
    }

    public String getCaHash() {
        return caHash;
    }

    public CAInfo setCaHash(String caHash) {
        this.caHash = caHash;
        return this;
    }
}
