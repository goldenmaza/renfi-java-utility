package org.hellstrand.renfi.exception;

public class InvalidBranchException extends ExpectedRuntimeException {
    public InvalidBranchException() {
        super();
    }

    public InvalidBranchException(String message) {
        super(message);
    }

    public InvalidBranchException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidBranchException(String message, Throwable cause, boolean enableSuppression, boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
