package io.aelf.portkey.utils.log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.aelf.portkey.internal.tools.GlobalConfig;
import io.aelf.utils.AElfException;
import org.jetbrains.annotations.NotNull;

public class GLogger {
    private static volatile ILogger logger;

    private static volatile Gson gson;

    public static @NotNull ILogger getLogger() {
        if (logger == null) {
            synchronized (GLogger.class) {
                if (logger == null) {
                    logger = new SystemOutLogger();
                }
            }
        }
        return logger;
    }

    public static synchronized void setLogger(@NotNull ILogger logger) {
        GLogger.logger = logger;
    }

    protected static Gson getGson() {
        if (gson == null) {
            synchronized (GLogger.class) {
                if (gson == null) {
                    gson = new GsonBuilder().setPrettyPrinting().create();
                }
            }
        }
        return gson;
    }

    public static <T> String prettyJSON(@NotNull T anything) {
        try {
            return prettyJSON(getGson().toJson(anything));
        } catch (Throwable e) {
            return anything.toString();
        }
    }

    public static String prettyJSON(@NotNull String jsonStr) {
        return prettyJSON(new JsonParser().parse(jsonStr));
    }

    public static String prettyJSON(@NotNull JsonElement json) {
        return getGson().toJson(json);
    }

    public static synchronized void e(@NotNull String msg) {
        getLogger().e(msg);
    }

    public static synchronized void e(@NotNull String msg, @NotNull AElfException exception) {
        getLogger().e(msg, exception);
    }

    public static synchronized void t(@NotNull String... msg) {
        if (!GlobalConfig.isTestEnvironment()) return;
        getLogger().i(msg);
    }

    public static synchronized void i(@NotNull String... msg) {
        getLogger().i(msg);
    }

    public static synchronized void w(@NotNull String... msg) {
        getLogger().w(msg);
    }
}
