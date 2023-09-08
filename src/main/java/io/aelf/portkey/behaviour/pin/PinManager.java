package io.aelf.portkey.behaviour.pin;

import com.google.gson.Gson;
import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.portkey.internal.model.wallet.WalletBuildConfig;
import io.aelf.portkey.internal.tools.GlobalConfig;
import io.aelf.portkey.internal.tools.GsonProvider;
import io.aelf.portkey.storage.IStorageBehaviour;
import io.aelf.portkey.storage.StorageProvider;
import io.aelf.utils.AElfException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static io.aelf.portkey.internal.tools.GlobalConfig.StorageTags.TAG_PIN;
import static io.aelf.portkey.internal.tools.GlobalConfig.StorageTags.TAG_WALLET_CONFIG;

public class PinManager {

    public static synchronized boolean checkIfSessionExists() {
        IStorageBehaviour handler = StorageProvider.getHandler();
        boolean walletExists = handler.contains(TAG_PIN)
                && handler.contains(TAG_WALLET_CONFIG);
        if (!walletExists) {
            clearStorage();
        }
        return walletExists;
    }

    static synchronized void clearStorage() {
        IStorageBehaviour handler = StorageProvider.getHandler();
        handler.removeValue(TAG_PIN);
        handler.removeValue(TAG_WALLET_CONFIG);
    }

    protected static synchronized void lock(@NotNull String pinValue, @NotNull WalletBuildConfig buildConfig) throws AElfException {
        verifyPin(pinValue);
        IStorageBehaviour handler = StorageProvider.getHandler();
        handler.putValue(TAG_PIN, pinValue);
        handler.putValue(TAG_WALLET_CONFIG, ExtraStorageEncoder.encode(GsonProvider.getGson().toJson(buildConfig), pinValue));
        setCurrentChainId(buildConfig.getOriginalChainId());
    }

    protected static synchronized @NotNull WalletBuildConfig unlock(@NotNull String pinValue) throws AElfException {
        verifyPin(pinValue);
        IStorageBehaviour handler = StorageProvider.getHandler();
        if (!checkIfSessionExists() || !handler.headValue(TAG_PIN, pinValue)) {
            throw new AElfException();
        }
        String walletConfig = ExtraStorageEncoder.decode(handler.getValue(TAG_WALLET_CONFIG), pinValue);
        WalletBuildConfig config = new Gson().fromJson(walletConfig, WalletBuildConfig.class);
        if (walletConfig != null) {
            setCurrentChainId(config.getOriginalChainId());
        }
        return config;
    }

    protected static void setCurrentChainId(String chainId) {
        GlobalConfig.setCurrentChainId(chainId);
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

    public static boolean headPin(String pinValue) {
        IStorageBehaviour handler = StorageProvider.getHandler();
        return handler.headValue(TAG_PIN, pinValue);
    }
}
