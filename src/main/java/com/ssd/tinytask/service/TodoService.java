package com.ssd.tinytask.service;

import com.ssd.tinytask.exception.InvalidTodoException;
import com.ssd.tinytask.exception.TodoNotFoundException;
import com.ssd.tinytask.model.Todo;
import com.ssd.tinytask.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * TodoService contains the business logic for managing todos
 */
@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    private static final int MIN_TITLE_LENGTH = 3;

    /**
     * Retrieves all todos
     * @return list of all todos
     */
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    /**
     * Retrieves a todo by ID
     * @param id the todo ID
     * @return the todo if found
     * @throws TodoNotFoundException if todo doesn't exist
     */
    public Todo getTodoById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }

    /**
     * Creates a new todo
     * @param todo the todo to create
     * @return the created todo
     * @throws InvalidTodoException if validation fails
     */
    public Todo createTodo(Todo todo) {
        validateTodo(todo);
        todo.setId(null); // Ensure it's a new todo
        todo.setDone(false); // New todos are not done
        return todoRepository.save(todo);
    }

    /**
     * Toggles the done status of a todo
     * @param id the todo ID
     * @return the updated todo
     * @throws TodoNotFoundException if todo doesn't exist
     */
    public Optional<Todo> toggleTodo(Long id) {
        Optional<Todo> todoOpt = todoRepository.findById(id);
        if (todoOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Todo todo = todoOpt.get();
        todo.setDone(!todo.isDone());
        return Optional.of(todoRepository.save(todo));
    }

    /**
     * Deletes a todo by ID
     * @param id the todo ID
     * @return true if deleted, false if not found
     */
    public boolean deleteTodo(Long id) {
        return todoRepository.deleteById(id);
    }

    /**
     * Validates a todo
     * @param todo the todo to validate
     * @throws InvalidTodoException if validation fails
     */
    private void validateTodo(Todo todo) {
        if (todo == null) {
            throw new InvalidTodoException("Todo cannot be null");
        }
        if (todo.getTitle() == null || todo.getTitle().trim().isEmpty()) {
            throw new InvalidTodoException("Title is required");
        }
        if (todo.getTitle().trim().length() < MIN_TITLE_LENGTH) {
            throw new InvalidTodoException("Title must be at least " + MIN_TITLE_LENGTH + " characters");
        }
    }
}
