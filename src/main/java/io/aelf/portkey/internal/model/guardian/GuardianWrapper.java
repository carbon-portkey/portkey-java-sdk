package io.aelf.portkey.internal.model.guardian;

import io.aelf.portkey.internal.model.verify.HeadVerifyCodeResultDTO;

public class GuardianWrapper {
    private final GuardianDTO originalData;
    private HeadVerifyCodeResultDTO verifiedData;

    public GuardianWrapper(GuardianDTO originalData) {
        this.originalData = originalData;
    }

    public GuardianWrapper(GuardianDTO originalData, HeadVerifyCodeResultDTO verifiedData) {
        this(originalData);
        this.verifiedData = verifiedData;
    }

    public boolean isVerified() {
        return verifiedData != null;
    }

    public GuardianDTO getOriginalData() {
        return originalData;
    }

    public HeadVerifyCodeResultDTO getVerifiedData() {
        return verifiedData;
    }

    public void setVerifiedData(HeadVerifyCodeResultDTO verifiedData) {
        this.verifiedData = verifiedData;
    }
}
