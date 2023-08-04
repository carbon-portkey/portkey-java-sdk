package io.aelf.portkey.utils.encrypt;

import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;

public interface IEncrypter {
    /**
     * Generate a new encryption key.
     *
     * @return The generated encryption key.
     */
    String generateNewEncryptKey();

    /**
     * Check if the encryption key is valid.
     *
     * @param encryptKey The encryption key to check.
     * @return True if the encryption key is valid, false otherwise.
     */
    @Contract(pure = true)
    boolean isValidEncryptKey(String encryptKey);

    /**
     * Encrypt a message using the specified encryption key.
     *
     * @param msg        The message to encrypt.
     * @param encryptKey The encryption key.
     * @return The encrypted message.
     */
    @Contract(pure = true)
    String encryptMsg(@Nonnull String msg, @Nonnull String encryptKey);

    /**
     * Decrypt a message using the specified encryption key.
     *
     * @param msg        The message to decrypt.
     * @param encryptKey The encryption key.
     * @return The decrypted message.
     */
    @Contract(pure = true)
    String decryptMsg(@Nonnull String msg, @Nonnull String encryptKey);
}