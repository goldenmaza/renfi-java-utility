package org.hellstrand.renfi.exception;

public class DirectoryUnavailableException extends ExpectedRuntimeException {
    public DirectoryUnavailableException() {
        super();
    }

    public DirectoryUnavailableException(String message) {
        super(message);
    }

    public DirectoryUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public DirectoryUnavailableException(String message, Throwable cause, boolean enableSuppression, boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
