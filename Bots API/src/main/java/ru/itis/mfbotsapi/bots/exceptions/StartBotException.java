package ru.itis.mfbotsapi.bots.exceptions;

public class StartBotException extends RuntimeException {

    public StartBotException() {
        super();
    }

    public StartBotException(String message) {
        super(message);
    }

    public StartBotException(String message, Throwable cause) {
        super(message, cause);
    }

    public StartBotException(Throwable cause) {
        super(cause);
    }

    protected StartBotException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
