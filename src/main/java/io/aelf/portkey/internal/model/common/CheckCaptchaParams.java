package io.aelf.portkey.internal.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.aelf.portkey.internal.behaviour.DataVerifyTools;

public class CheckCaptchaParams {

    /**
     * @see DataVerifyTools#verifyOperationType(int)
     */
    @JsonProperty("operationType")
    private int operationType;

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        DataVerifyTools.verifyOperationType(operationType);
        this.operationType = operationType;
    }

}
