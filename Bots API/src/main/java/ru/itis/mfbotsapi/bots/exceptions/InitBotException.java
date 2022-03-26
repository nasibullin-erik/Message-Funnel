package ru.itis.mfbotsapi.bots.exceptions;

public class InitBotException extends RuntimeException{

    public InitBotException() {
        super();
    }

    public InitBotException(String message) {
        super(message);
    }

    public InitBotException(String message, Throwable cause) {
        super(message, cause);
    }

    public InitBotException(Throwable cause) {
        super(cause);
    }

    protected InitBotException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
