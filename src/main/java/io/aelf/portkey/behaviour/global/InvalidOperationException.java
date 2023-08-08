package io.aelf.portkey.behaviour.global;

import io.aelf.response.ResultCode;
import io.aelf.utils.AElfException;

public class InvalidOperationException extends AElfException {
    public InvalidOperationException() {
        super(ResultCode.INTERNAL_ERROR, "Current state prevents this operation.");
    }
}
