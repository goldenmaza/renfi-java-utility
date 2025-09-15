package org.hellstrand.renfi.exception;

public class ResourcesUnavailableException extends ExpectedRuntimeException {
    public ResourcesUnavailableException() {
        super();
    }

    public ResourcesUnavailableException(String message) {
        super(message);
    }

    public ResourcesUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourcesUnavailableException(String message, Throwable cause, boolean enableSuppression, boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
