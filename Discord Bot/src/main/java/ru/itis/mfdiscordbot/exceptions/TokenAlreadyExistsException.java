package ru.itis.mfdiscordbot.exceptions;

public class TokenAlreadyExistsException extends RuntimeException{

    public TokenAlreadyExistsException() {
        super();
    }

    public TokenAlreadyExistsException(String message) {
        super(message);
    }

    public TokenAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    protected TokenAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
