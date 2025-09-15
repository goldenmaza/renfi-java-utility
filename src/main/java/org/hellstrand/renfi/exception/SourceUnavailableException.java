package org.hellstrand.renfi.exception;

public class SourceUnavailableException extends ExpectedRuntimeException {
    public SourceUnavailableException() {
        super();
    }

    public SourceUnavailableException(String message) {
        super(message);
    }

    public SourceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public SourceUnavailableException(String message, Throwable cause, boolean enableSuppression, boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
