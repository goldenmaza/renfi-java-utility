package org.hellstrand.renfi.exception;

public class InvalidUseException extends ExpectedRuntimeException {
    public InvalidUseException() {
        super();
    }

    public InvalidUseException(String message) {
        super(message);
    }

    public InvalidUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUseException(String message, Throwable cause, boolean enableSuppression, boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
