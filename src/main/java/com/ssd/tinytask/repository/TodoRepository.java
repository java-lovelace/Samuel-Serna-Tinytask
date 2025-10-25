package com.ssd.tinytask.repository;

import com.ssd.tinytask.model.Todo;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TodoRepository manages in-memory storage of Todo items.
 * Uses ConcurrentHashMap for thread-safety and AtomicLong for auto-incrementing IDs.
 */
@Repository
public class TodoRepository {

    private final Map<Long, Todo> todos = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * Retrieves all todos
     * @return list of all todos
     */
    public List<Todo> findAll() {
        return new ArrayList<>(todos.values());
    }

    /**
     * Finds a todo by ID
     * @param id the todo ID
     * @return Optional containing the todo if found, empty otherwise
     */
    public Optional<Todo> findById(Long id) {
        return Optional.ofNullable(todos.get(id));
    }

    /**
     * Saves a new todo or updates an existing one
     * @param todo the todo to save
     * @return the saved todo with generated ID
     */
    public Todo save(Todo todo) {
        if (todo.getId() == null) {
            todo.setId(idGenerator.getAndIncrement());
        }
        todos.put(todo.getId(), todo);
        return todo;
    }

    /**
     * Deletes a todo by ID
     * @param id the todo ID
     * @return true if deleted, false if not found
     */
    public boolean deleteById(Long id) {
        return todos.remove(id) != null;
    }

    /**
     * Checks if a todo exists by ID
     * @param id the todo ID
     * @return true if exists, false otherwise
     */
    public boolean existsById(Long id) {
        return todos.containsKey(id);
    }

    /**
     * Returns the total count of todos
     * @return the number of todos
     */
    public long count() {
        return todos.size();
    }

    /**
     * Clears all todos (useful for testing)
     */
    public void deleteAll() {
        todos.clear();
    }
}
