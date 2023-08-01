package io.aelf.portkey.storage;

import io.aelf.portkey.storage.model.AbstractStorageHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StorageProvider {
    private static AbstractStorageHandler handler;

    public static void init(){
        init((AbstractStorageHandler) null);
    }

    public static void init(@Nullable AbstractStorageHandler storageHandler) {
        handler = storageHandler != null ? storageHandler : new DefaultStorageHandler();
    }

    public static void init(@NotNull String encryptKey){
        handler=new DefaultStorageHandler(encryptKey);
    }

    public static AbstractStorageHandler getHandler() {
        return handler;
    }
}
