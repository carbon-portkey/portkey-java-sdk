package io.aelf.portkey.storage.model;

public interface IStorageBehaviour {
    String getValue(String key);

    void putValue(String key, String value);

    void putValueAsync(String key, String value);

    boolean headValue(String key, String value);
}
