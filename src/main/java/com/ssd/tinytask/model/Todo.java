package com.ssd.tinytask.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Todo model representing a task.
 * This entity is stored in memory.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    private Long id;
    private String title;
    private boolean done;

    /**
     * Constructor for creating a new Todo without an ID
     * @param title the task title
     */
    public Todo(String title) {
        this.title = title;
        this.done = false;
    }
}
