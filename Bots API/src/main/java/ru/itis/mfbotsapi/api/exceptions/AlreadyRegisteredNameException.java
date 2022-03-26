package ru.itis.mfbotsapi.api.exceptions;

public class AlreadyRegisteredNameException extends RuntimeException {

    public AlreadyRegisteredNameException() {
    }

    public AlreadyRegisteredNameException(String message) {
        super(message);
    }

    public AlreadyRegisteredNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyRegisteredNameException(Throwable cause) {
        super(cause);
    }

    public AlreadyRegisteredNameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
