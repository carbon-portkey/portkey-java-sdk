package io.aelf.portkey.internal.model.guardian;

import io.aelf.portkey.internal.model.google.GoogleAccount;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeResultDTO;
import org.jetbrains.annotations.Nullable;

public class GuardianWrapper {
    private final GuardianDTO originalData;
    private HeadVerifyCodeResultDTO verifiedData;
    private final GoogleAccount googleAccount;

    public GuardianWrapper(GuardianDTO originalData) {
        this(originalData, (GoogleAccount) null);
    }

    public GuardianWrapper(GuardianDTO originalData, @Nullable GoogleAccount googleAccount) {
        this.originalData = originalData;
        this.googleAccount = googleAccount;
    }


    public GuardianWrapper(GuardianDTO originalData, HeadVerifyCodeResultDTO verifiedData) {
        this(originalData, (GoogleAccount) null);
        this.verifiedData = verifiedData;
    }

    public GuardianWrapper(GuardianDTO originalData, HeadVerifyCodeResultDTO verifiedData, GoogleAccount googleAccount) {
        this(originalData, googleAccount);
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

    public GoogleAccount getGoogleAccount() {
        return googleAccount;
    }
}
