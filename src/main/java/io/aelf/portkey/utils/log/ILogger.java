package io.aelf.portkey.utils.log;

import io.aelf.utils.AElfException;

import javax.annotation.Nonnull;

public interface ILogger {
    /**
     * Logs an error message with an exception.
     *
     * @param msg       The error message to log.
     * @param exception The exception to log.
     */
    void e(@Nonnull String msg, @Nonnull AElfException exception);

    /**
     * Logs an info message.
     *
     * @param msg The info message to log.
     */
    void i(@Nonnull String... msg);

    /**
     * Logs a warning message.
     *
     * @param msg The warning message to log.
     */
    void w(@Nonnull String... msg);
}