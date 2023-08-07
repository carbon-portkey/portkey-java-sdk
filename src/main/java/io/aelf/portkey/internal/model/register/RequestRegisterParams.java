package io.aelf.portkey.internal.model.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.aelf.portkey.internal.behaviour.DataVerifyTools;
import io.aelf.portkey.internal.model.common.ContextDTO;

public class RequestRegisterParams {
    /**
     * @see DataVerifyTools#verifyChainIdParams(String)
     */
    @JsonProperty("chainId")
    private String chainId;
    @JsonProperty("caHash")
    private String caHash;
    @JsonProperty("loginGuardianIdentifier")
    private String loginGuardianIdentifier;
    @JsonProperty("verifierId")
    private String verifierId;
    @JsonProperty("verificationDoc")
    private String verificationDoc;
    @JsonProperty("signature")
    private String signature;
    @JsonProperty("context")
    private ContextDTO context;

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        DataVerifyTools.verifyChainIdParams(chainId);
        this.chainId = chainId;
    }

    public String getCaHash() {
        return caHash;
    }

    public void setCaHash(String caHash) {
        this.caHash = caHash;
    }

    public String getLoginGuardianIdentifier() {
        return loginGuardianIdentifier;
    }

    public void setLoginGuardianIdentifier(String loginGuardianIdentifier) {
        this.loginGuardianIdentifier = loginGuardianIdentifier;
    }

    public String getVerifierId() {
        return verifierId;
    }

    public void setVerifierId(String verifierId) {
        this.verifierId = verifierId;
    }

    public String getVerificationDoc() {
        return verificationDoc;
    }

    public void setVerificationDoc(String verificationDoc) {
        this.verificationDoc = verificationDoc;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public ContextDTO getContext() {
        return context;
    }

    public void setContext(ContextDTO context) {
        this.context = context;
    }
}

