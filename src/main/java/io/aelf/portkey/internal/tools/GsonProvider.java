package io.aelf.portkey.internal.tools;

import com.google.gson.Gson;

public class GsonProvider {
    private static volatile Gson gson;
    public static Gson getGson() {
        if (gson == null) {
            synchronized (GsonProvider.class) {
                if (gson == null) {
                    gson = new Gson();
                }
            }
        }
        return gson;
    }
}
