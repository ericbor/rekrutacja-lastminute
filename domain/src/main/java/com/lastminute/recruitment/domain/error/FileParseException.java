package com.lastminute.recruitment.domain.error;

public class FileParseException extends RuntimeException {
    public FileParseException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public FileParseException(String message) {
        super(message);
    }
}
