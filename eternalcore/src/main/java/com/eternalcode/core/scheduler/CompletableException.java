package com.eternalcode.core.scheduler;

public class CompletableException extends RuntimeException {

    public CompletableException() {
    }

    public CompletableException(String message) {
        super(message);
    }

    public CompletableException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompletableException(Throwable cause) {
        super(cause);
    }

}
