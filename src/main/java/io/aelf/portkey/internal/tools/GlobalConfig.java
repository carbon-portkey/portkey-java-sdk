package io.aelf.portkey.internal.tools;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class GlobalConfig {
    public static final String NAME_PORTKEY_SDK = "portkey-sdk";
    public static final String URL_SYMBOL_PORTKEY = "portkey";
    public static final String DO_NOT_OVERRIDE_HEADERS_SYMBOL = "do-not-override-headers";
    public static final String MAIN_STORAGE_PATH_PREFIX = "/storage/";
    public static final String GOOGLE_HOST = "https://www.googleapis.com";
    public static final String NOT_VALID_ENCRYPT_KEY = "not-valid-encrypt-key";
    public static final @NotNull Map.Entry<String, String>
            ENCRYPT_TEST_KEY_SET = Map.entry("encryptTestKey", "encryptTestValue");
    private static boolean underTestEnvironment = false;
    private static String currentChainId = ChainIds.MAINNET_CHAIN_ID;

    public static boolean isTestEnvironment() {
        return underTestEnvironment;
    }

    public static void setTestEnv(boolean testEnvironment) {
        underTestEnvironment = testEnvironment;
    }

    public static String getCurrentChainId() {
        return currentChainId;
    }

    public static void setCurrentChainId(String currentChainId) {
        DataVerifyTools.verifyChainId(currentChainId);
        GlobalConfig.currentChainId = currentChainId;
    }

    public interface ChainIds {
        String MAINNET_CHAIN_ID = "AELF";
        String TESTNET_CHAIN_ID = "tDVV";
        String TESTNET_CHAIN_ID_ALTERNATIVE = "tDVW";
    }

    public interface StorageTags {
        String TAG_SESSION_ID = "session_id";
    }
}
