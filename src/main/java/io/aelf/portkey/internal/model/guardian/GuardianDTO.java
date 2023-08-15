package io.aelf.portkey.internal.model.guardian;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.aelf.portkey.internal.model.common.AccountOriginalType;

public class GuardianDTO {

    @JsonProperty("guardianIdentifier")
    private String guardianIdentifier;
    @JsonProperty("identifierHash")
    private String identifierHash;
    @JsonProperty("isLoginGuardian")
    private boolean isLoginGuardian;
    @JsonProperty("salt")
    private String salt;
    /**
     * @see AccountOriginalType
     */
    @JsonProperty("type")
    private String type;
    @JsonProperty("verifierId")
    private String verifierId;

    public String getGuardianIdentifier() {
        return guardianIdentifier;
    }

    public GuardianDTO setGuardianIdentifier(String guardianIdentifier) {
        this.guardianIdentifier = guardianIdentifier;
        return this;
    }

    public String getIdentifierHash() {
        return identifierHash;
    }

    public GuardianDTO setIdentifierHash(String identifierHash) {
        this.identifierHash = identifierHash;
        return this;
    }

    public boolean isLoginGuardian() {
        return isLoginGuardian;
    }

    public GuardianDTO setLoginGuardian(boolean loginGuardian) {
        isLoginGuardian = loginGuardian;
        return this;
    }

    public String getSalt() {
        return salt;
    }

    public GuardianDTO setSalt(String salt) {
        this.salt = salt;
        return this;
    }

    public String getType() {
        return type;
    }

    public GuardianDTO setType(String type) {
        this.type = type;
        return this;
    }

    public String getVerifierId() {
        return verifierId;
    }

    public GuardianDTO setVerifierId(String verifierId) {
        this.verifierId = verifierId;
        return this;
    }
}