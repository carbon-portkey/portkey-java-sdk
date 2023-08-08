package io.aelf.portkey.storage;

import io.aelf.response.ResultCode;
import io.aelf.utils.AElfException;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractStorageHandler implements IStorageBehaviour {
    protected final String encryptKey;
    protected final IEncrypter encrypter;

    public AbstractStorageHandler() {
        this(null, null, false);
    }

    public AbstractStorageHandler(@Nullable String encryptKey) {
        this(null, encryptKey, true);
    }

    public AbstractStorageHandler(@Nullable IEncrypter encrypter) {
        this(encrypter, null, true);
    }

    protected AbstractStorageHandler(@Nullable IEncrypter encrypter, @Nullable String encryptKey, boolean useEncrypt) {
        this.encrypter =
                encrypter != null
                        ? encrypter
                        : (
                        useEncrypt
                                ? new AES256Encrypter()
                                : new NonEncrypter()
                );
        if (encrypter == null && TextUtils.isEmpty(encryptKey) && useEncrypt) {
            throw new AElfException(ResultCode.PARAM_ERROR,
                    "you are trying to use encryption but not providing an acceptable encrypt key. "
                            .concat("Use the exportEncryptKey() method to get the encrypt key first ")
                            .concat("and save it at somewhere safe. Losing it will cause you to lose access to your data.")
            );
        } else {
            this.encryptKey = encryptKey;
        }
    }

    public String exportEncryptKey() {
        return this.encryptKey;
    }

    /**
     * Check if the encrypted key is the same as before.
     *
     * @param encryptKey the encrypted key to check.
     * @return true if the encrypted key is the same as before.
     */
    protected abstract boolean isEncryptKeySame(String encryptKey);

}
