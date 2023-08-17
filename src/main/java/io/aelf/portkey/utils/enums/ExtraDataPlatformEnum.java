package io.aelf.portkey.utils.enums;

public enum ExtraDataPlatformEnum {
    OTHER(0),
    MAC(1),
    IOS(2),
    WINDOWS(3),
    ANDROID(4),
    ;

    private final int value;

    ExtraDataPlatformEnum(int value) {
        this.value = value;
    }

    public String getName() {
        switch (this) {
            case MAC:
                return "macOS";
            case WINDOWS:
                return "windows";
            default:
                return "Other";
        }
    }

    public int getValue() {
        return value;
    }
}
