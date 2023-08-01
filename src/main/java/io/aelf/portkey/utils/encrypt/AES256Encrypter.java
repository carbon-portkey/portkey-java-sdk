package io.aelf.portkey.utils.encrypt;

import io.aelf.utils.AElfException;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;

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

    public String generateNewEncryptKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();
            return bytesToHex(keyBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isValidEncryptKey(String encryptKey) {
        if (TextUtils.isEmpty(encryptKey)) return false;
        try {
            getCipher(encryptKey);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String encryptMsg(@NotNull String msg, @NotNull String encryptKey) {
        if (TextUtils.isEmpty(msg)) return msg;
        try {
            Cipher cipher = getCipher(encryptKey);
            byte[] inputBytes = msg.getBytes(StandardCharsets.UTF_8);
            byte[] encrypted = cipher.doFinal(inputBytes);
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new AElfException(e);
        }
    }

    private Cipher getCipher(@NotNull String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        return cipher;
    }

    public String decryptMsg(@NotNull String msg, @NotNull String encryptKey) {
        if (TextUtils.isEmpty(msg)) return msg;
        try{
            byte[] keyBytes = encryptKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] encryptedBytes = Base64.getDecoder().decode(msg);
            byte[] decrypted = cipher.doFinal(encryptedBytes);
            return new String(decrypted, StandardCharsets.UTF_8);
        }catch (Exception e){
            throw new AElfException(e);
        }
    }
}
