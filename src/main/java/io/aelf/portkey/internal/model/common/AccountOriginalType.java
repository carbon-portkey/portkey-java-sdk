package io.aelf.portkey.internal.model.common;

/**
 * Marks where the account comes from.
 * <p>
 * 0: email
 * <p>
 * 1: phone
 * <p>
 * 2: google - which means it comes from Google account login
 * <p>
 * 3: apple - which means it comes from iCloud account login
 */
public interface AccountOriginalType {
    int email = 0;
    int phone = 1;
    int google = 2;
    int apple = 3;
}
