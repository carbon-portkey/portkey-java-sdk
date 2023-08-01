package io.aelf.portkey.storage;

import io.aelf.portkey.storage.model.AbstractStorageHandler;
import io.aelf.portkey.storage.model.IStorageBehaviour;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StorageProvider {
    private static IStorageBehaviour handler;

    public static void init() {
        init((IStorageBehaviour) null);
    }

    public static void init(@Nullable IStorageBehaviour storageHandler) {
        handler = storageHandler != null ? storageHandler : new DefaultStorageHandler();
    }

    public static void init(@NotNull String encryptKey) {
        handler = new DefaultStorageHandler(encryptKey);
    }

    public static IStorageBehaviour getHandler() {
        return handler;
    }
}
