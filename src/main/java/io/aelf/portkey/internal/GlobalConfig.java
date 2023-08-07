package io.aelf.portkey.internal;

public class GlobalConfig {
    public static final String NAME_PORTKEY_SDK = "portkey-sdk";
    public static final String URL_SYMBOL_PORTKEY = "portkey";
    public static final String DO_NOT_OVERRIDE_HEADERS_SYMBOL = "do-not-override-headers";
    public static final String MAIN_STORAGE_PATH_PREFIX="/storage/";

    private static boolean underTestEnvironment = false;

    public static boolean isTestEnvironment() {
        return underTestEnvironment;
    }

    public static void setTestEnv(boolean testEnvironment) {
        underTestEnvironment = testEnvironment;
    }
}
