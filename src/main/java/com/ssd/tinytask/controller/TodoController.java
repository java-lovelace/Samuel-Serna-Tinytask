package com.ssd.tinytask.controller;

import com.ssd.tinytask.dto.CreateTodoRequest;
import com.ssd.tinytask.exception.TodoNotFoundException;
import com.ssd.tinytask.model.Todo;
import com.ssd.tinytask.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST Controller for Todo operations
 */
@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    /**
     * GET /api/todos - Retrieves all todos
     * @return list of all todos
     */
    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos() {
        List<Todo> todos = todoService.getAllTodos();
        return ResponseEntity.ok(todos);
    }

    /**
     * GET /api/todos/{id} - Retrieves a specific todo
     * @param id the todo ID
     * @return the todo if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Todo todo = todoService.getTodoById(id);
        return ResponseEntity.ok(todo);
    }

    /**
     * POST /api/todos - Creates a new todo
     * @param request the create todo request
     * @return the created todo
     */
    @PostMapping
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody CreateTodoRequest request) {
        Todo todo = new Todo(request.getTitle());
        Todo createdTodo = todoService.createTodo(todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
    }

    /**
     * PUT /api/todos/{id}/toggle - Toggles the done status of a todo
     * @param id the todo ID
     * @return the updated todo
     */
    @PutMapping("/{id}/toggle")
    public ResponseEntity<Todo> toggleTodo(@PathVariable Long id) {
        return todoService.toggleTodo(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }

    /**
     * DELETE /api/todos/{id} - Deletes a todo
     * @param id the todo ID
     * @return 204 No Content if successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        boolean deleted = todoService.deleteTodo(id);
        if (!deleted) {
            throw new TodoNotFoundException(id);
        }
        return ResponseEntity.noContent().build();
    }
}
