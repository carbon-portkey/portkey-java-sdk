package io.aelf.portkey.internal.model.guardian;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

public class GetGuardianInfoParams {
    @JsonProperty("chainId")
    private String chainId;
    @Nullable
    @JsonProperty("caHash")
    private String caHash;
    @Nullable
    @JsonProperty("guardianIdentifier")
    private String guardianIdentifier;

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public @Nullable String getCaHash() {
        return caHash;
    }

    public void setCaHash(@Nullable String caHash) {
        this.caHash = caHash;
    }

    public @Nullable String getGuardianIdentifier() {
        return guardianIdentifier;
    }

    public void setGuardianIdentifier(@Nullable String guardianIdentifier) {
        this.guardianIdentifier = guardianIdentifier;
    }
}
