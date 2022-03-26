package ru.itis.mfbotsapi.api.exceptions;

public class ServerWorkException extends RuntimeException {

    public ServerWorkException() {
    }

    public ServerWorkException(String message) {
        super(message);
    }

    public ServerWorkException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerWorkException(Throwable cause) {
        super(cause);
    }
}
