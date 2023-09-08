package io.aelf.portkey.internal.model.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RecoveryStatusDTO {
        @JsonProperty("totalCount")
        private int totalCount;
        @JsonProperty("items")
        private RecoveryStatusItemDTO[] items;

        public int getTotalCount() {
            return totalCount;
        }

        public RecoveryStatusDTO setTotalCount(int totalCount) {
            this.totalCount = totalCount;
            return this;
        }

        public RecoveryStatusItemDTO[] getItems() {
            return items;
        }

        public RecoveryStatusDTO setItems(RecoveryStatusItemDTO[] items) {
            this.items = items;
            return this;
        }
}
