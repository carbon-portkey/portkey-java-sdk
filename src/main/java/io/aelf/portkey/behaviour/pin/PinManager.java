package io.aelf.portkey.behaviour.pin;

import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.portkey.internal.model.wallet.WalletBuildConfig;
import io.aelf.portkey.internal.tools.GlobalConfig;
import io.aelf.portkey.storage.IStorageBehaviour;
import io.aelf.portkey.storage.StorageProvider;
import io.aelf.utils.AElfException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static io.aelf.portkey.internal.tools.GlobalConfig.StorageTags.*;

public class PinManager {

    public static synchronized boolean checkIfSessionExists() {
        IStorageBehaviour handler = StorageProvider.getHandler();
        boolean walletExists = handler.contains(TAG_PIN)
                && handler.contains(TAG_PRIV_KEY)
                && handler.contains(TAG_SESSION_ID);
        if (!walletExists) {
            clearStorage();
        }
        return walletExists;
    }

    public static synchronized void clearStorage() {
        IStorageBehaviour handler = StorageProvider.getHandler();
        handler.removeValue(TAG_PIN);
        handler.removeValue(TAG_PRIV_KEY);
        handler.removeValue(TAG_SESSION_ID);
    }

    public static synchronized void lock(@NotNull String pinValue, @NotNull WalletBuildConfig session) throws AElfException {
        verifyPin(pinValue);
        IStorageBehaviour handler = StorageProvider.getHandler();
        handler.putValue(TAG_PIN, pinValue);
        handler.putValue(TAG_SESSION_ID, ExtraStorageEncoder.encode(session.getSessionId(), pinValue));
        handler.putValue(TAG_PRIV_KEY, ExtraStorageEncoder.encode(session.getPrivKey(), pinValue));
    }

    public static synchronized @NotNull WalletBuildConfig unlock(@NotNull String pinValue) throws AElfException {
        verifyPin(pinValue);
        IStorageBehaviour handler = StorageProvider.getHandler();
        if (!checkIfSessionExists() || !handler.headValue(TAG_PIN, pinValue)) {
            throw new AElfException();
        }
        String sessionId = ExtraStorageEncoder.encode(handler.getValue(TAG_SESSION_ID), pinValue);
        String privateKey = ExtraStorageEncoder.encode(handler.getValue(TAG_PRIV_KEY), pinValue);
        return new WalletBuildConfig()
                .setSessionId(sessionId)
                .setPrivKey(privateKey);
    }

    protected static void verifyPin(@NotNull String pinValue) throws AElfException {
        AssertChecker.assertTrue(
                isValidPin(pinValue),
                "invalid pin pattern."
        );
    }

    @Contract(pure = true)
    public static boolean isValidPin(@NotNull String pinValue) {
        return pinValue.length() >= GlobalConfig.PinConfig.MIN_LENGTH
                && pinValue.length() <= GlobalConfig.PinConfig.MAX_LENGTH
                && pinValue.matches(GlobalConfig.PinConfig.REGEX);
    }

}
