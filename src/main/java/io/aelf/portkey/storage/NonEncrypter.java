package io.aelf.portkey.storage;

import io.aelf.portkey.internal.behaviour.GlobalConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

 class NonEncrypter implements IEncrypter {

    /**
     * NonEncrypter will always return {@link GlobalConfig#NOT_VALID_ENCRYPT_KEY}.
     */
    @Override
    public String generateNewEncryptKey() {
        return GlobalConfig.NOT_VALID_ENCRYPT_KEY;
    }

    /**
     * Since this is a non-encrypter, this method will always return true.
     */
    @Override
    public boolean isValidEncryptKey(String encryptKey) {
        return true;
    }

    /**
     * NonEncrypter will ignore the encryption process.
     */
    @Override
    public String encryptMsg(@NotNull String msg, @Nullable String encryptKey) {
        return msg;
    }

    /**
     * NonEncrypter will ignore the decryption process.
     */
    @Override
    public String decryptMsg(@NotNull String msg, @NotNull String encryptKey) {
        return msg;
    }
}
