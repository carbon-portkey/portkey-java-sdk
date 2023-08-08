package io.aelf.portkey.behaviour.guardian.state;

import io.aelf.utils.AElfException;

public interface IGuardianState {
    /**
     * Send the verification code to the specified verification position.
     * <p>
     * Currently, we only support email verification.
     *
     * @return True if the verification code is sent.
     */
    boolean sendVerificationCode() throws AElfException;

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
    boolean isVerified();

    /**
     * Report the
     * @throws AElfException if not verified.
     */
    void next() throws AElfException;

}
