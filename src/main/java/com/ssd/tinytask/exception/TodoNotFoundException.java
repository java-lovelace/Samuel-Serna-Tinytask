package com.ssd.tinytask.exception;

/**
 * Exception thrown when a Todo is not found
 */
public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(Long id) {
        super("Todo not found with id: " + id);
    }
}
