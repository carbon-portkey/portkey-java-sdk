package io.aelf.portkey.internal.model.common;

/**
 * Marks where the account comes from.
 * <p>
 * 0: email
 * <p>
 * 1: phone
 * <p>
 * 2: google - which means it comes from Google account guardian
 * <p>
 * 3: apple - which means it comes from iCloud account guardian
 */
public enum AccountOriginalType {
    Email(0),
    Phone(1),
    Google(2),
    Apple(3);

    private final int value;

    AccountOriginalType(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }
}
