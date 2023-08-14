package io.aelf.portkey.internal.model.extraInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

public class ExtraInfoWrapper {
    @JsonProperty("transactionTime")
    private final long transactionTime = System.currentTimeMillis();
    @JsonProperty("deviceInfo")
    private final String deviceInfo;

    public ExtraInfoWrapper(DeviceExtraInfo info) {
        this.deviceInfo = new Gson().toJson(info);
    }

    public long getTransactionTime() {
        return transactionTime;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public String getVersion() {
        return "2.0.0";
    }
}
