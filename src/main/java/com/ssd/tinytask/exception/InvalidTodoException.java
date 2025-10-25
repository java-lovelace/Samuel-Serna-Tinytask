package com.ssd.tinytask.exception;

/**
 * Exception thrown when validation fails
 */
public class InvalidTodoException extends RuntimeException {
    public InvalidTodoException(String message) {
        super(message);
    }
}
