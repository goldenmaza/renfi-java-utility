package org.hellstrand.renfi.exception;

public class ExpectedRuntimeException extends RuntimeException {
    public ExpectedRuntimeException() {}

    public ExpectedRuntimeException(String message) {
        super(message);
    }

    public ExpectedRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpectedRuntimeException(
            String message, Throwable cause, boolean enableSuppression, boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
