package io.aelf.portkey.utils.log;

import io.aelf.utils.AElfException;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("CallToPrintStackTrace")
public class SystemOutLogger implements ILogger {
    @Override
    public void e(@NotNull String msg) {
        System.err.println("error: ".concat(msg));
    }

    @Override
    public void e(@NotNull String msg, @NotNull AElfException exception) {
        e(msg);
        exception.printStackTrace();
    }

    @Override
    public void i(@NotNull String @NotNull ... msg) {
        for (String s : msg) {
            System.out.println("info: ".concat(s));
        }
    }

    @Override
    public void w(@NotNull String @NotNull ... msg) {
        for (String s : msg) {
            System.err.println("warn: ".concat(s));
        }
    }
}
