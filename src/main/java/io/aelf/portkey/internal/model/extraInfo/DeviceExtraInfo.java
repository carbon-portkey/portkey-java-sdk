package io.aelf.portkey.internal.model.extraInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.aelf.portkey.utils.enums.Platform;

public class DeviceExtraInfo {
    @JsonProperty("deviceName")
    private String deviceName;
    @JsonProperty("deviceType")
    private int deviceType;

    public static DeviceExtraInfo fromPlatformEnum(Platform platform) {
        return new DeviceExtraInfo()
                .setDeviceName(platform.getName())
                .setDeviceType(platform.getValue());
    }

    public String getDeviceName() {
        return deviceName;
    }

    public DeviceExtraInfo setDeviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public DeviceExtraInfo setDeviceType(int deviceType) {
        this.deviceType = deviceType;
        return this;
    }
}
