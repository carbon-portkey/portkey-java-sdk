package io.aelf.portkey.internal.model.register;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterInfoDTO {

        @JsonProperty("originChainId")
        private String originChainId;

        public String getOriginChainId() {
            return originChainId;
        }

        public void setOriginChainId(String originChainId) {
            this.originChainId = originChainId;
        }
}
