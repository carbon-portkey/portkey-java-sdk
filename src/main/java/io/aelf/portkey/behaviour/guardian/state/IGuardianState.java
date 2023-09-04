package io.aelf.portkey.behaviour.guardian.state;

import io.aelf.portkey.behaviour.global.InvalidOperationException;
import io.aelf.portkey.internal.model.google.GoogleAccount;
import io.aelf.utils.AElfException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

public interface IGuardianState {
    /**
     * Send the verification code to the specified verification position.
     * <p>
     * Currently, we only support email verification.
     *
     * @return True if the verification code is sent.
     */
    default boolean sendVerificationCode() throws AElfException {
        throw new InvalidOperationException();
    }

    /**
     * Send the verification code to the specified verification position.
     * <p>
     * Currently, we only support email verification.
     *
     * @return True if the verification code is sent.
     */
    default boolean sendVerificationCode(@NonNull String recaptchaToken) throws AElfException {
        throw new InvalidOperationException();
    }

    /**
     * Verify the verification code.
     *
     * @return True if the verification is verified.
     */
    boolean verifyVerificationCode(String code) throws AElfException;

    /**
     * Check if the verification is verified.
     *
     * @return True if the verification is verified.
     */
    default boolean isVerified() {
        return this.getStage() == Stage.VERIFIED;
    }


    /**
     * Get the current stage of the guardian.
     * <p>
     * INIT: The guardian is initialized.
     * <p>
     * SENT: The verify code is sent.
     * <p>
     * VERIFIED: The verify code is verified, it is able to call next().
     *
     * @return The current stage of the guardian.
     * @see Stage
     */
    Stage getStage();

    enum Stage {
        INIT,
        SENT,
        VERIFIED
    }

}
