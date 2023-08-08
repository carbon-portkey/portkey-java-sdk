package io.aelf.portkey.behaviour.global;

import io.aelf.response.ResultCode;
import io.aelf.utils.AElfException;

/**
 * It means that your operation is not finished.
 * <p>
 * Continue your operation and try again.
 */
public class OperationNotFinishedException extends AElfException {
    public OperationNotFinishedException() {
        super(ResultCode.INTERNAL_ERROR, "Operation not finished.");
    }
}
