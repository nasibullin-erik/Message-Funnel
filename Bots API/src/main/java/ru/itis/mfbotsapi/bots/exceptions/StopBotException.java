package ru.itis.mfbotsapi.bots.exceptions;

public class StopBotException extends RuntimeException{

    public StopBotException() {
        super();
    }

    public StopBotException(String message) {
        super(message);
    }

    public StopBotException(String message, Throwable cause) {
        super(message, cause);
    }

    public StopBotException(Throwable cause) {
        super(cause);
    }

    protected StopBotException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
