package org.hellstrand.renfi.exception;

public class MismatchingConversionHistoryException extends ExpectedRuntimeException {
    public MismatchingConversionHistoryException() {
        super();
    }

    public MismatchingConversionHistoryException(String message) {
        super(message);
    }

    public MismatchingConversionHistoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public MismatchingConversionHistoryException(String message, Throwable cause, boolean enableSuppression, boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
