package io.aelf.portkey.behaviour.google_guardian;

import io.aelf.portkey.behaviour.global.GuardianObserver;
import io.aelf.portkey.behaviour.guardian.GuardianBehaviourEntity;
import io.aelf.portkey.internal.model.common.AccountOriginalType;
import io.aelf.portkey.internal.model.google.GoogleAccount;
import io.aelf.portkey.internal.model.google.GoogleVerifyTokenParams;
import io.aelf.portkey.internal.model.guardian.GuardianDTO;
import io.aelf.portkey.internal.model.guardian.GuardianWrapper;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeResultDTO;
import io.aelf.portkey.internal.tools.GlobalConfig;
import io.aelf.portkey.network.connecter.INetworkInterface;
import io.aelf.portkey.utils.log.GLogger;
import io.aelf.utils.AElfException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public class GoogleGuardianEntity extends GuardianBehaviourEntity {
    private final GoogleAccount googleAccount;

    private boolean isVerified = false;

    public GoogleGuardianEntity(
            @NotNull GuardianDTO guardian,
            int operationType,
            @NotNull GuardianObserver observer,
            AccountOriginalType accountOriginalType,
            GoogleAccount googleAccount
    ) {
        super(guardian, operationType, observer, accountOriginalType);
        this.googleAccount = googleAccount;
    }

    // Google account verifies guardian doesn't need reCaptcha
    @Override
    public boolean checkForReCaptcha() {
        return false;
    }

    // Google account verifies guardian doesn't need send verification code
    @Override
    public boolean sendVerificationCode() throws AElfException {
        return true;
    }

    @Override
    public boolean sendVerificationCode(@NonNull String recaptchaToken) throws AElfException {
        return true;
    }

    @Override
    public boolean verifyVerificationCode(String code) throws AElfException {
        if (isVerified) return true;
        try {
            GoogleVerifyTokenParams params = (GoogleVerifyTokenParams) new GoogleVerifyTokenParams()
                    .setVerifierId(this.getOriginalGuardianInfo().getVerifierId())
                    .setChainId(GlobalConfig.getCurrentChainId())
                    .setOperationType(this.getOperationType());
            params.setToken(this.googleAccount.getIdToken());
            HeadVerifyCodeResultDTO result = INetworkInterface.getInstance().verifyGoogleToken(params);
            if (result != null) {
                this.getObserver().informGuardianReady(
                        new GuardianWrapper(
                                this.getOriginalGuardianInfo(),
                                result,
                                this.googleAccount
                        )
                );
                this.isVerified = true;
            }
        } catch (Throwable e) {
            GLogger.e("GoogleGuardianEntity verifyVerificationCode error: ", new AElfException(e));
        }
        return this.isVerified;
    }

    @Override
    public boolean isVerified() {
        return this.isVerified;
    }
}
