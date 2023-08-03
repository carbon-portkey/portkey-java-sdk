package io.aelf.portkey.utils.log;

import io.aelf.utils.AElfException;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class GLogger {
    private static volatile ILogger logger;

    public static synchronized void setLogger(@Nonnull ILogger logger) {
        GLogger.logger = logger;
    }

    public static ILogger getLogger() {
        if (logger == null) {
            synchronized (GLogger.class) {
                if (logger == null) {
                    logger = new SystemOutLogger();
                }
            }
        }
        return logger;
    }

    public static void e(@NotNull String msg, @NotNull AElfException exception) {
        getLogger().e(msg, exception);
    }

    public static void i(@NotNull String... msg) {
        getLogger().i(msg);
    }

    public static void w(@NotNull String... msg) {
        getLogger().i(msg);
    }
}
