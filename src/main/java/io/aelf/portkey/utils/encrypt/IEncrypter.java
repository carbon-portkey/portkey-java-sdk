package io.aelf.portkey.utils.encrypt;

import javax.annotation.Nonnull;

public interface IEncrypter {
    String generateNewEncryptKey();
    boolean isValidEncryptKey(String encryptKey);
    String encryptMsg(@Nonnull String msg, @Nonnull String encryptKey);

    String decryptMsg(@Nonnull String msg, @Nonnull String encryptKey);
}
