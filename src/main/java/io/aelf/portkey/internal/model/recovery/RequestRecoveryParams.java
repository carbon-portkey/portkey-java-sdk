package io.aelf.portkey.internal.model.recovery;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.aelf.portkey.internal.model.common.ContextDTO;
import io.aelf.portkey.internal.model.guardian.ApprovedGuardianDTO;

public class RequestRecoveryParams {
    @JsonProperty("loginGuardianIdentifier")
    private String loginGuardianIdentifier;
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

    public void setLoginGuardianIdentifier(String loginGuardianIdentifier) {
        this.loginGuardianIdentifier = loginGuardianIdentifier;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public ApprovedGuardianDTO[] getGuardiansApproved() {
        return guardiansApproved;
    }

    public void setGuardiansApproved(ApprovedGuardianDTO[] guardiansApproved) {
        this.guardiansApproved = guardiansApproved;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public ContextDTO getContext() {
        return context;
    }

    public void setContext(ContextDTO context) {
        this.context = context;
    }
}

