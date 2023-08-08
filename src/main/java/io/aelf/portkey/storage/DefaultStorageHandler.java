package io.aelf.portkey.storage;

import io.aelf.internal.AsyncResult;
import io.aelf.internal.ISuccessCallback;
import io.aelf.portkey.async.AsyncTaskCaller;
import io.aelf.portkey.utils.log.GLogger;
import io.aelf.response.ResultCode;
import io.aelf.utils.AElfException;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;

import io.aelf.portkey.internal.behaviour.GlobalConfig;
import io.fastkv.FastKV;
import org.jetbrains.annotations.Nullable;

class DefaultStorageHandler extends AbstractStorageHandler {

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
     * If such unexpected things happened, you can only use {@link StorageProvider#clear()} to erase the
     * data and start over.
     */
    public DefaultStorageHandler() {
        this(null, null, false);
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
     * If such unexpected things happened, you can only use {@link StorageProvider#clear()} to erase the
     * data and start over.
     *
     * @param encryptKey the encryptKey to use.
     * @see StorageProvider#exportNewEncryptKey() to export a new encryptKey.
     */
    public DefaultStorageHandler(@Nullable String encryptKey) {
        this(encryptKey, null);
    }

    protected DefaultStorageHandler(@Nullable String storageBucketName, boolean useEncrypt) {
        this(null, storageBucketName, useEncrypt);
    }

    protected DefaultStorageHandler(@Nullable String encryptKey, @Nullable String storageBucketName) {
        this(encryptKey, storageBucketName, true);
    }

    protected DefaultStorageHandler(@Nullable String encryptKey, @Nullable String storageBucketName, boolean useEncrypt) {
        super(null, encryptKey, useEncrypt);
        this.kvProvider = getFastKVKit(storageBucketName);
        if (!isEncryptKeySame(encryptKey)) {
            GLogger.w("We detected that you were using a different encrypt key. This problem may be caused by:",
                    "1.You have provided an encrypt key before, but now you are using a different one. "
                            .concat("You can only use StorageProvider.clear() to erase the data and start over."),
                    "2.You have initialized the Storage Module with an encrypt key before, but now you are trying to use the Storage Module without an encrypt key. "
                            .concat("Try to find the original key and use it to initialize the Storage Module again."),
                    "3.You have initialized the Storage Module without an encrypt key before, but now you are trying to use the Storage Module with an encrypt key. "
                            .concat("Remove the encrypt key and try to initialize the Storage Module again."),
                    "REMEMBER not to mix them up, or you will only end up with Exceptions."
            );
            throw new AElfException(ResultCode.PARAM_ERROR, "The encrypt key is not the same as before.");
        }
        if (!contains(GlobalConfig.ENCRYPT_TEST_KEY_SET.getKey())) {
            putValue(GlobalConfig.ENCRYPT_TEST_KEY_SET.getKey(), GlobalConfig.ENCRYPT_TEST_KEY_SET.getValue());
        }
    }

    protected static FastKV getFastKVKit(String storageBucketName) {
        return new FastKV.Builder(
                System.getProperty("user.dir").concat(GlobalConfig.MAIN_STORAGE_PATH_PREFIX),
                TextUtils.isEmpty(storageBucketName) ? GlobalConfig.NAME_PORTKEY_SDK : storageBucketName)
                .build();
    }

    private final FastKV kvProvider;

    public String getValue(@NotNull String key) {
        return encrypter.decryptMsg(kvProvider.getString(key), encryptKey);
    }

    public void putValue(@NotNull String key, String value) {
        kvProvider.putString(key, encrypter.encryptMsg(value, encryptKey));
    }

    @Override
    public void putValueAsync(@NotNull final String key, final String value, @Nullable ISuccessCallback<Boolean> callback) {
        AsyncTaskCaller.getInstance().asyncCall(() -> {
            try {
                putValue(key, value);
                return new AsyncResult<>(Boolean.TRUE);
            } catch (Throwable e) {
                GLogger.e(
                        "put fail. key:"
                                .concat(key)
                                .concat(", value: ")
                                .concat(value),
                        new AElfException(e)
                );
                return new AsyncResult<>(Boolean.FALSE);
            }
        }, callback, null);
    }

    public boolean headValue(@NotNull String key, String value) {
        String res = encrypter.decryptMsg(kvProvider.getString(key), encryptKey);
        if (TextUtils.isEmpty(value)) {
            return TextUtils.isEmpty(res);
        }
        return value.equals(res);
    }

    @Override
    public void removeValue(@NotNull String key) {
        kvProvider.remove(key);
    }

    @Override
    public boolean contains(@NotNull String key) {
        return kvProvider.contains(key);
    }

    @Override
    public void clear() {
        kvProvider.clear();
    }

    @Override
    protected boolean isEncryptKeySame(String encryptKey) {
        String encrypted = this.kvProvider.getString(GlobalConfig.ENCRYPT_TEST_KEY_SET.getKey());
        // this is the first time to use encryptKey; there's no need to check the encrypted value.
        if (TextUtils.isEmpty(encrypted)) return true;
        if (this.encrypter.encryptMsg("test", encryptKey).equals("test")) {
            // the encrypter is NoneEncrypter, just need to check whether the tag is not encrypted.
            return GlobalConfig.ENCRYPT_TEST_KEY_SET.getValue().equals(encrypted);
        }
        if (!this.encrypter.isValidEncryptKey(encryptKey)) {
            return false;
        }
        try {
            assert GlobalConfig.ENCRYPT_TEST_KEY_SET.getValue()
                    .equals(encrypter.decryptMsg(encrypted, encryptKey));
        } catch (Throwable e) {
            return false;
        }
        return true;
    }
}
