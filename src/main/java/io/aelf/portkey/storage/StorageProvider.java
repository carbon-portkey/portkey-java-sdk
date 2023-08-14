package io.aelf.portkey.storage;

import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.response.ResultCode;
import io.aelf.utils.AElfException;
import io.fastkv.FastKV;
import org.jetbrains.annotations.NotNull;

public class StorageProvider {
    private static IStorageBehaviour handler;

    /**
     * Init the storage config without encryptKey.
     * <p>
     * REMEMBER: this constructor will not encrypt your data, and if you have provided an encrypted key earlier,
     * this will end up with Exceptions.
     * <p>
     * DO NOT use this constructor and the other DefaultStorageHandler(String) constructor which accept an encryptKey
     * at the same time, you can only choose one of them from the very beginning of the universe and stick to it.
     * <p>
     * Mixing them up will end up with Errors.
     * <p>
     * If such unexpected issue happens, you can only use {@link StorageProvider#clear()} to erase the
     * data and start over.
     */
    public static void init() {
        handler = new DefaultStorageHandler(null, false);
    }

    /**
     * Init the storage config with encryptKey provided.
     * <p>
     * REMEMBER: once you have chosen to encrypt your data, you can not change it back.
     * <p>
     * And if you have provided an encrypted key that is not the same, this will end up with Exceptions.
     * <p>
     * DO NOT use this constructor and the other DefaultStorageHandler() constructor which do not accept an encryptKey
     * at the same time, you can only choose one of them from the very beginning of the universe and stick to it.
     * Mixing them up will end up with Errors.
     * <p>
     * If such unexpected issue happens, you can only use {@link StorageProvider#clear()} to erase the
     * data and start over.
     *
     * @param encryptKey the encryptKey to use.
     * @see StorageProvider#exportNewEncryptKey() to export a new encryptKey.
     */
    public static void init(@NotNull String encryptKey) {
        handler = new DefaultStorageHandler(encryptKey);
    }

    public static void init(@NotNull IStorageBehaviour storageHandler) {
        handler = storageHandler;
    }

    public static String exportNewEncryptKey() {
        return new AES256Encrypter().generateNewEncryptKey();
    }

    public static synchronized void clear() {
        FastKV kv = DefaultStorageHandler.getFastKVKit(null);
        kv.clear();
    }

    public static IStorageBehaviour getHandler() {
        AssertChecker.assertNotNull(handler, new AElfException(ResultCode.INTERNAL_ERROR, "StorageProvider has not been initialized yet."));
        return handler;
    }
}
