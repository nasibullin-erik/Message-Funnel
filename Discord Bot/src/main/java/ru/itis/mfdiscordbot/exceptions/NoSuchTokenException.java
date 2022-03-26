package ru.itis.mfdiscordbot.exceptions;

public class NoSuchTokenException extends RuntimeException{

    public NoSuchTokenException() {
        super();
    }

    public NoSuchTokenException(String message) {
        super(message);
    }

    public NoSuchTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchTokenException(Throwable cause) {
        super(cause);
    }

    protected NoSuchTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
