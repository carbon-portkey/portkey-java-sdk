package io.aelf.portkey.internal.model.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.aelf.portkey.internal.model.common.ContextDTO;
import io.aelf.portkey.internal.tools.DataVerifyTools;

public class RequestRegisterParams {
    /**
     * @see DataVerifyTools#verifyChainId(String)
     */
    @JsonProperty("chainId")
    private String chainId;
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
    @JsonProperty("type")
    private String type;
    @JsonProperty("manager")
    private String manager;
    @JsonProperty("extraData")
    private String extraData;

    public String getChainId() {
        return chainId;
    }

    public RequestRegisterParams setChainId(String chainId) {
        this.chainId = chainId;
        return this;
    }

    public String getLoginGuardianIdentifier() {
        return loginGuardianIdentifier;
    }

    public RequestRegisterParams setLoginGuardianIdentifier(String loginGuardianIdentifier) {
        this.loginGuardianIdentifier = loginGuardianIdentifier;
        return this;
    }

    public String getVerifierId() {
        return verifierId;
    }

    public RequestRegisterParams setVerifierId(String verifierId) {
        this.verifierId = verifierId;
        return this;
    }

    public String getVerificationDoc() {
        return verificationDoc;
    }

    public RequestRegisterParams setVerificationDoc(String verificationDoc) {
        this.verificationDoc = verificationDoc;
        return this;
    }

    public String getSignature() {
        return signature;
    }

    public RequestRegisterParams setSignature(String signature) {
        this.signature = signature;
        return this;
    }

    public ContextDTO getContext() {
        return context;
    }

    public RequestRegisterParams setContext(ContextDTO context) {
        this.context = context;
        return this;
    }

    public String getType() {
        return type;
    }

    public RequestRegisterParams setType(String type) {
        this.type = type;
        return this;
    }

    public String getManager() {
        return manager;
    }

    public RequestRegisterParams setManager(String manager) {
        this.manager = manager;
        return this;
    }

    public String getExtraData() {
        return extraData;
    }

    public RequestRegisterParams setExtraData(String extraData) {
        this.extraData = extraData;
        return this;
    }
}

