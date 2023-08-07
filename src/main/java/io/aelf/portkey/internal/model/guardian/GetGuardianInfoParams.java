package io.aelf.portkey.internal.model.guardian;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.aelf.portkey.internal.behaviour.DataVerifyTools;
import org.jetbrains.annotations.Nullable;

public class GetGuardianInfoParams {
    /**
     * @see DataVerifyTools#verifyChainIdParams(String)
     */
    @JsonProperty("chainId")
    private String chainId;
    @JsonProperty("caHash")
    private @Nullable String caHash;
    @JsonProperty("guardianIdentifier")
    private @Nullable String guardianIdentifier;

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        DataVerifyTools.verifyChainIdParams(chainId);
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
