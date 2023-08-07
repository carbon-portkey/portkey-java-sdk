package io.aelf.portkey.internal.model.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.aelf.portkey.internal.behaviour.DataVerifyTools;

public class RegisterInfoDTO {

    /**
     * Actually, this field is the same as "chainId."
     * @see DataVerifyTools#verifyChainIdParams(String)
     */
        @JsonProperty("originChainId")
        private String originChainId;

        public String getOriginChainId() {
            return originChainId;
        }

        public void setOriginChainId(String originChainId) {
            this.originChainId = originChainId;
        }
}
