package io.aelf.portkey.storage.model;

import io.aelf.portkey.utils.encrypt.AES256Encrypter;
import io.aelf.portkey.utils.encrypt.IEncrypter;
import io.aelf.response.ResultCode;
import io.aelf.utils.AElfException;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractStorageHandler implements IStorageBehaviour {
    protected final String encryptKey;
    protected final IEncrypter encrypter;

    public AbstractStorageHandler() {
        this(null, null);
    }

    public AbstractStorageHandler(@Nullable IEncrypter encrypter, @Nullable String encryptKey) {
        this.encrypter = encrypter != null ? encrypter : new AES256Encrypter();
        if (TextUtils.isEmpty(encryptKey)) {
            this.encryptKey = this.encrypter.generateNewEncryptKey();
        } else {
            if (!this.encrypter.isValidEncryptKey(encryptKey))
                throw new AElfException(ResultCode.PARAM_ERROR, "you are providing an invalid encrypt key.");
            this.encryptKey = encryptKey;
        }
    }

    public String exportEncryptKey() {
        return this.encryptKey;
    }

}
