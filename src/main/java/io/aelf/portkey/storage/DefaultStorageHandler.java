package io.aelf.portkey.storage;

import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;

import io.aelf.portkey.internal.GlobalConfig;
import io.aelf.portkey.storage.model.AbstractStorageHandler;
import io.fastkv.FastKV;
import org.jetbrains.annotations.Nullable;

public class DefaultStorageHandler extends AbstractStorageHandler {

    public DefaultStorageHandler(){
       super();
    }

    public DefaultStorageHandler(@Nullable String encryptKey){
        super(null,encryptKey);
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

    public void putValueAsync(@NotNull String key, String value) {
        // TODO finish the putValueAsync function
        putValue(key,value);
    }

    public boolean headValue(@NotNull String key, String value) {
        String res = encrypter.decryptMsg(kvProvider.getString(key), encryptKey);
        if (TextUtils.isEmpty(value)) {
            return TextUtils.isEmpty(res);
        }
        return value.equals(res);
    }
}
