package io.aelf.portkey.internal.model.guardian;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GuardianListDTO {

    @JsonProperty("guardians")
    private GuardianDTO[] guardians;

    public GuardianDTO[] getGuardians() {
        return guardians;
    }

    public void setGuardians(GuardianDTO[] guardians) {
        this.guardians = guardians;
    }
}
