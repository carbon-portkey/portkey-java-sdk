package io.aelf.portkey.utils.enums;

/**
 * Used to identify the platform of Google Recaptcha service.
 */
public enum GoogleRecaptchaPlatformEnum {
    WEB(0),
    ANDROID(1),
    IOS(2),
    ;

    private final int value;

    GoogleRecaptchaPlatformEnum( int value ) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
