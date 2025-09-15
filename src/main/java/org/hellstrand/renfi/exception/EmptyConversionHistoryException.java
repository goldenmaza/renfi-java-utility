package org.hellstrand.renfi.exception;

public class EmptyConversionHistoryException extends ExpectedRuntimeException {
    public EmptyConversionHistoryException() {
        super();
    }

    public EmptyConversionHistoryException(String message) {
        super(message);
    }

    public EmptyConversionHistoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyConversionHistoryException(String message, Throwable cause, boolean enableSuppression, boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
