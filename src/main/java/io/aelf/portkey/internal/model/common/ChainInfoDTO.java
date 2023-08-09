package io.aelf.portkey.internal.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChainInfoDTO {
    @JsonProperty("totalCount")
    private int totalCount;
    @JsonProperty("items")
    private ChainInfoItemDTO[] items;

    public int getTotalCount() {
        return totalCount;
    }

    public ChainInfoDTO setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public ChainInfoItemDTO[] getItems() {
        return items;
    }

    public ChainInfoDTO setItems(ChainInfoItemDTO[] items) {
        this.items = items;
        return this;
    }
}
