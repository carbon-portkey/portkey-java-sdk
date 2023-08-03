package io.aelf.portkey.internal.model.guardian;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GuardianInfoDTO {

    @JsonProperty("caAddress")
    private String caAddress;
    @JsonProperty("caHash")
    private String caHash;
    @JsonProperty("guardianList")
    private GuardianListDTO guardianList;
    @JsonProperty("managerInfos")
    private ManagerInfoDTO[] managerInfos;

    public String getCaAddress() {
        return caAddress;
    }

    public void setCaAddress(String caAddress) {
        this.caAddress = caAddress;
    }

    public String getCaHash() {
        return caHash;
    }

    public void setCaHash(String caHash) {
        this.caHash = caHash;
    }

    public GuardianListDTO getGuardianList() {
        return guardianList;
    }

    public void setGuardianList(GuardianListDTO guardianList) {
        this.guardianList = guardianList;
    }

    public ManagerInfoDTO[] getManagerInfos() {
        return managerInfos;
    }

    public void setManagerInfos(ManagerInfoDTO[] managerInfos) {
        this.managerInfos = managerInfos;
    }
}