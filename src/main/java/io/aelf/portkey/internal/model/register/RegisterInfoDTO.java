package io.aelf.portkey.internal.model.register;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.aelf.portkey.internal.tools.DataVerifyTools;

public class RegisterInfoDTO {

    /**
     * Actually, this field is the same as "chainId."
     *
     * @see DataVerifyTools#verifyChainId(String)
     */
    @JsonProperty("originChainId")
    private String originChainId;

    private boolean errCodeMatchNotRegistered = false;

    public String getOriginChainId() {
        return originChainId;
    }

    public void setOriginChainId(String originChainId) {
        DataVerifyTools.verifyChainId(originChainId);
        this.originChainId = originChainId;
    }

    public boolean isErrCodeMatchNotRegistered() {
        return errCodeMatchNotRegistered;
    }

    public RegisterInfoDTO setErrCodeMatchNotRegistered(boolean errCodeMatchNotRegistered) {
        this.errCodeMatchNotRegistered = errCodeMatchNotRegistered;
        return this;
    }
}
