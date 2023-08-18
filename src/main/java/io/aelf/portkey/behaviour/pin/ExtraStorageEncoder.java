package io.aelf.portkey.behaviour.pin;

import io.aelf.portkey.internal.tools.GlobalConfig;
import io.aelf.portkey.storage.AES256Encrypter;
import io.aelf.response.ResultCode;
import io.aelf.utils.AElfException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ExtraStorageEncoder {
    @Contract(pure = true)
    public static String encode(@NotNull String input, @NotNull String key) {
        assert !input.isEmpty() && !key.isEmpty();
        return new AES256Encrypter().encryptMsg(input, parseEncryptKey(key));
    }

    @Contract(pure = true)
    public static String decode(@NotNull String input, @NotNull String key) {
        assert !input.isEmpty() && !key.isEmpty();
        return new AES256Encrypter().decryptMsg(input, parseEncryptKey(key));
    }

    protected static String parseEncryptKey(@NotNull String input) {
        if (input.length() < GlobalConfig.PinConfig.MIN_LENGTH) {
            throw new AElfException(ResultCode.INTERNAL_ERROR, "invalid encrypt key pattern.");
        }
        String key = input;
        while (key.length() < 16) {
            key = key.concat(key);
        }
        return key.substring(0, 16);
    }

}
