package io.aelf.portkey.internal.model.recovery;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.aelf.portkey.internal.model.common.ContextDTO;
import io.aelf.portkey.internal.model.guardian.ApprovedGuardianDTO;

public class RequestRecoveryParams {
    @JsonProperty("loginGuardianIdentifier")
    private String loginGuardianIdentifier;
    /**
     * Actually, it's the keypair address that is about to sign.
     */
    @JsonProperty("manager")
    private String manager;
    @JsonProperty("guardiansApproved")
    private ApprovedGuardianDTO[] guardiansApproved;
    @JsonProperty("extraData")
    private String extraData;
    @JsonProperty("chainId")
    private String chainId;
    @JsonProperty("context")
    private ContextDTO context;

    public String getLoginGuardianIdentifier() {
        return loginGuardianIdentifier;
    }

    public RequestRecoveryParams setLoginGuardianIdentifier(String loginGuardianIdentifier) {
        this.loginGuardianIdentifier = loginGuardianIdentifier;
        return this;
    }

    public String getManager() {
        return manager;
    }

    public RequestRecoveryParams setManager(String manager) {
        this.manager = manager;
        return this;
    }

    public ApprovedGuardianDTO[] getGuardiansApproved() {
        return guardiansApproved;
    }

    public RequestRecoveryParams setGuardiansApproved(ApprovedGuardianDTO[] guardiansApproved) {
        this.guardiansApproved = guardiansApproved;
        return this;
    }

    public String getExtraData() {
        return extraData;
    }

    public RequestRecoveryParams setExtraData(String extraData) {
        this.extraData = extraData;
        return this;
    }

    public String getChainId() {
        return chainId;
    }

    public RequestRecoveryParams setChainId(String chainId) {
        this.chainId = chainId;
        return this;
    }

    public ContextDTO getContext() {
        return context;
    }

    public RequestRecoveryParams setContext(ContextDTO context) {
        this.context = context;
        return this;
    }
}

