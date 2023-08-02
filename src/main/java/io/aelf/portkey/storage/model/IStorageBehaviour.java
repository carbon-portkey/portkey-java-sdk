package io.aelf.portkey.storage.model;

import io.aelf.internal.ISuccessCallback;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;

/**
 * This is an interface for storage behavior.
 */
public interface IStorageBehaviour {
    /**
     * Get the value associated with the specified key.
     *
     * @param key The key to retrieve the value.
     * @return The value associated with the key.
     */
    String getValue(String key);

    /**
     * Store the specified key-value pair.
     *
     * @param key   The key to store.
     * @param value The value to store.
     */
    void putValue(String key, String value);

    /**
     * Asynchronously store the specified key-value pair.
     * Remember: since it is an async function, you'd better put your further
     * operation in its callback function.
     *
     * @param key      The key to store.
     * @param value    The value to store.
     * @param callback a callback method to take further operations.
     */
    void putValueAsync(String key, String value,
            @Nullable ISuccessCallback<Boolean> callback);

    /**
     * Check if the value associated with the specified key matches the given value.
     *
     * @param key   The key to check.
     * @param value The value to compare.
     * @return True if the value matches, false otherwise.
     */
    boolean headValue(String key, String value);

    /**
     * Remove the value associated with the specified key.
     *
     * @param key The key to remove.
     */
    void removeValue(String key);

    /**
     * Detect if the specified key exists.
     * 
     * @param key The key to check.
     */
    boolean contains(String key);

}