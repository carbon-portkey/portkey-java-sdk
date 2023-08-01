package io.aelf.portkey.utils.log;

import javax.annotation.Nonnull;

public class GlobalLogger {
    private static volatile ILogger logger;

    public static synchronized void setLogger(@Nonnull ILogger logger) {
        GlobalLogger.logger = logger;
    }

    public static ILogger getLogger() {
        if (logger == null) {
            synchronized (GlobalLogger.class) {
                if (logger == null) {
                    logger = new SystemOutLogger();
                }
            }
        }
        return logger;
    }
}
