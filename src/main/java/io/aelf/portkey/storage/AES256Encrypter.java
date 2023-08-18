package io.aelf.portkey.storage;

import io.aelf.portkey.utils.log.GLogger;
import io.aelf.utils.AElfException;
import org.apache.http.util.TextUtils;
import org.apache.logging.log4j.core.util.Assert;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static io.aelf.utils.ByteArrayHelper.bytesToHex;

public class AES256Encrypter implements IEncrypter {

    @Contract(pure = true)
    private static Cipher getCipher(@NotNull String key, int mode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(mode, secretKeySpec);
        return cipher;
    }

    public String generateNewEncryptKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128, new java.security.SecureRandom(String.valueOf(System.currentTimeMillis()).getBytes()));
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();
            return bytesToHex(keyBytes);
        } catch (NoSuchAlgorithmException e) {
            AElfException ex = new AElfException(e);
            GLogger.e("you are using wrong kind of algorithm.", ex);
            throw ex;
        }
    }

    public boolean isValidEncryptKey(String encryptKey) {
        if (TextUtils.isEmpty(encryptKey)) return false;
        try {
            Cipher cipher = getCipher(encryptKey, Cipher.ENCRYPT_MODE);
            return Assert.isNonEmpty(cipher);
        } catch (Exception e) {
            return false;
        }
    }

    public String encryptMsg(@NotNull String msg, @Nullable String encryptKey) {
        if (TextUtils.isEmpty(msg)) return msg;
        try {
            assert encryptKey != null;
            Cipher cipher = getCipher(encryptKey, Cipher.ENCRYPT_MODE);
            byte[] inputBytes = msg.getBytes(StandardCharsets.UTF_8);
            byte[] encrypted = cipher.doFinal(inputBytes);
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            AElfException ex = new AElfException(e);
            GLogger.e("encrypt fail:", ex);
            throw ex;
        }
    }

    public String decryptMsg(@NotNull String msg, @Nullable String encryptKey) {
        if (TextUtils.isEmpty(msg)) return msg;
        assert encryptKey != null;
        try {
            Cipher cipher = getCipher(encryptKey, Cipher.DECRYPT_MODE);
            byte[] encryptedBytes = Base64.getDecoder().decode(msg);
            byte[] decrypted = cipher.doFinal(encryptedBytes);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            AElfException ex = new AElfException(e);
            GLogger.e("encrypt fail:", ex);
            throw ex;
        }
    }
}
