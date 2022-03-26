package ru.itis.mfbotsapi.api.exceptions;

public class ClientWorkException extends RuntimeException {
    public ClientWorkException() {
    }

    public ClientWorkException(String s) {
        super(s);
    }

    public ClientWorkException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientWorkException(Throwable cause) {
        super(cause);
    }
}
