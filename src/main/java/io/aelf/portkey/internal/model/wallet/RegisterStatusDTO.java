package io.aelf.portkey.internal.model.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterStatusDTO {
    @JsonProperty("totalCount")
    private int totalCount;
    @JsonProperty("items")
    private RegisterStatusItemDTO[] items;

    public int getTotalCount() {
        return totalCount;
    }

    public RegisterStatusDTO setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public RegisterStatusItemDTO[] getItems() {
        return items;
    }

    public RegisterStatusDTO setItems(RegisterStatusItemDTO[] items) {
        this.items = items;
        return this;
    }
}
