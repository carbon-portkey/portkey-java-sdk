package io.aelf.portkey.storage;

import io.aelf.internal.AsyncResult;
import io.aelf.internal.ISuccessCallback;
import io.aelf.portkey.async.AsyncTaskCaller;
import io.aelf.portkey.utils.log.GlobalLogger;
import io.aelf.utils.AElfException;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;

import io.aelf.portkey.internal.GlobalConfig;
import io.aelf.portkey.storage.model.AbstractStorageHandler;
import io.fastkv.FastKV;
import org.jetbrains.annotations.Nullable;

public class DefaultStorageHandler extends AbstractStorageHandler {

    public DefaultStorageHandler() {
        super();
    }

    public DefaultStorageHandler(@Nullable String encryptKey) {
        super(null, encryptKey);
    }

    private final FastKV kvProvider = new FastKV.Builder(
            System.getProperty("user.dir"),
            GlobalConfig.NAME_PORTKEY_SDK).build();

    public String getValue(@NotNull String key) {
        return encrypter.decryptMsg(kvProvider.getString(key), encryptKey);
    }

    public void putValue(@NotNull String key, String value) {
        kvProvider.putString(key, encrypter.encryptMsg(value, encryptKey));
    }

    @Override
    public void putValueAsync(@NotNull String key, String value, @Nullable ISuccessCallback<Boolean> callback) {
        AsyncTaskCaller.getInstance().asyncCall(() -> {
            try {
                putValue(key, value);
                return new AsyncResult<>(Boolean.TRUE);
            } catch (Throwable e) {
                GlobalLogger.getLogger().e(
                        "put fail. key:"
                                .concat(key)
                                .concat(", value: ")
                                .concat(value),
                        new AElfException(e));
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
}
