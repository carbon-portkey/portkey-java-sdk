package io.aelf.portkey.behaviour.guardian.google_guardian;

import io.aelf.portkey.behaviour.global.GuardianObserver;
import io.aelf.portkey.behaviour.global.InvalidOperationException;
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
import io.aelf.response.ResultCode;
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
            boolean isAlreadyVerified,
            GoogleAccount googleAccount,
            String accountIdentifier
    ) {
        super(guardian, operationType, observer, accountOriginalType, isAlreadyVerified, accountIdentifier);
        this.googleAccount = googleAccount;
    }

    // Google account verifies guardian doesn't need reCaptcha
    @Override
    public boolean checkForReCaptcha() {
        return false;
    }

    // Google account verifies guardian doesn't need to send verification code
    @Override
    public boolean sendVerificationCode() throws AElfException {
        return true;
    }

    @Override
    public boolean sendVerificationCode(@NonNull String recaptchaToken) throws AElfException {
        return true;
    }

    @Override
    public boolean requireOutsideGoogleAccount() {
        return googleAccount == null
                || googleAccount.getEmail() == null
                || !googleAccount.getEmail().equals(getOriginalGuardianInfo().getThirdPartyEmail());
    }

    @Override
    public boolean verifyVerificationCode(String code) throws AElfException {
        GLogger.e("It's google's guardian, better check it with verifyVerificationCodeWithGoogle() first.");
        throw new InvalidOperationException();
    }

    @Override
    public boolean verifyVerificationCodeWithGoogle() throws AElfException {
        return this.verifyVerificationCodeWithGoogle(this.googleAccount);
    }

    private boolean isGoogleAccountMatch(GoogleAccount account) {
        return account != null
                && account.getEmail() != null
                && account.getEmail().equals(getOriginalGuardianInfo().getThirdPartyEmail());
    }

    @Override
    public boolean verifyVerificationCodeWithGoogle(@NotNull GoogleAccount account) throws AElfException {
        if (isVerified()) return true;
        if (!isGoogleAccountMatch(account)) {
            throw new AElfException(ResultCode.PARAM_ERROR, "Google account doesn't match.");
        }
        try {
            GoogleVerifyTokenParams params = new GoogleVerifyTokenParams()
                    .setVerifierId(this.getOriginalGuardianInfo().getVerifierId())
                    .setChainId(GlobalConfig.getCurrentChainId())
                    .setOperationType(this.getOperationType())
                    .setToken(account.getAccessToken());
            HeadVerifyCodeResultDTO result = INetworkInterface.getInstance().verifyGoogleToken(params);
            if (result.isSuccess()) {
                this.getObserver().informGuardianReady(
                        new GuardianWrapper(
                                this.getOriginalGuardianInfo(),
                                result,
                                account
                        )
                );
                this.isVerified = true;
            } else {
                return false;
            }
        } catch (Throwable e) {
            throw new AElfException(e);
        }
        return isVerified();
    }

    @Override
    public boolean isVerified() {
        return this.isVerified || super.isVerified();
    }
}
