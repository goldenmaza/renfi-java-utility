package org.hellstrand.renfi.exception;

public class InvalidBoundaryException extends ExpectedRuntimeException {
    public InvalidBoundaryException() {
        super();
    }

    public InvalidBoundaryException(String message) {
        super(message);
    }

    public InvalidBoundaryException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidBoundaryException(String message, Throwable cause, boolean enableSuppression, boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
