package com.ssd.tinytask.repository;

import com.ssd.tinytask.model.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TodoRepository
 */
class TodoRepositoryTest {

    private TodoRepository todoRepository;

    @BeforeEach
    void setUp() {
        todoRepository = new TodoRepository();
    }

    @Test
    @DisplayName("Should generate unique auto-incremented IDs")
    void shouldGenerateUniqueAutoIncrementedIds() {
        // Given
        Todo todo1 = new Todo("First task");
        Todo todo2 = new Todo("Second task");

        // When
        Todo saved1 = todoRepository.save(todo1);
        Todo saved2 = todoRepository.save(todo2);

        // Then
        assertNotNull(saved1.getId());
        assertNotNull(saved2.getId());
        assertNotEquals(saved1.getId(), saved2.getId());
        assertTrue(saved2.getId() > saved1.getId());
    }

    @Test
    @DisplayName("Should save and retrieve todo by ID - Positive scenario")
    void shouldSaveAndRetrieveTodoById() {
        // Given
        Todo todo = new Todo("Learn Spring Boot");

        // When
        Todo saved = todoRepository.save(todo);
        Optional<Todo> retrieved = todoRepository.findById(saved.getId());

        // Then
        assertTrue(retrieved.isPresent());
        assertEquals(saved.getId(), retrieved.get().getId());
        assertEquals("Learn Spring Boot", retrieved.get().getTitle());
        assertFalse(retrieved.get().isDone());
    }

    @Test
    @DisplayName("Should return empty Optional when ID doesn't exist - Negative scenario")
    void shouldReturnEmptyOptionalWhenIdDoesNotExist() {
        // When
        Optional<Todo> result = todoRepository.findById(999L);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should find all todos")
    void shouldFindAllTodos() {
        // Given
        todoRepository.save(new Todo("Task 1"));
        todoRepository.save(new Todo("Task 2"));
        todoRepository.save(new Todo("Task 3"));

        // When
        List<Todo> todos = todoRepository.findAll();

        // Then
        assertEquals(3, todos.size());
    }

    @Test
    @DisplayName("Should delete todo by ID - Positive scenario")
    void shouldDeleteTodoById() {
        // Given
        Todo todo = new Todo("Task to delete");
        Todo saved = todoRepository.save(todo);

        // When
        boolean deleted = todoRepository.deleteById(saved.getId());

        // Then
        assertTrue(deleted);
        assertFalse(todoRepository.existsById(saved.getId()));
        assertEquals(0, todoRepository.count());
    }

    @Test
    @DisplayName("Should return false when deleting non-existent ID - Negative scenario")
    void shouldReturnFalseWhenDeletingNonExistentId() {
        // When
        boolean deleted = todoRepository.deleteById(999L);

        // Then
        assertFalse(deleted);
    }

    @Test
    @DisplayName("Should check if todo exists by ID")
    void shouldCheckIfTodoExistsById() {
        // Given
        Todo todo = new Todo("Existing task");
        Todo saved = todoRepository.save(todo);

        // Then
        assertTrue(todoRepository.existsById(saved.getId()));
        assertFalse(todoRepository.existsById(999L));
    }

    @Test
    @DisplayName("Should count todos correctly")
    void shouldCountTodos() {
        // Given
        todoRepository.save(new Todo("Task 1"));
        todoRepository.save(new Todo("Task 2"));

        // Then
        assertEquals(2, todoRepository.count());
    }

    @Test
    @DisplayName("Should delete all todos")
    void shouldDeleteAllTodos() {
        // Given
        todoRepository.save(new Todo("Task 1"));
        todoRepository.save(new Todo("Task 2"));

        // When
        todoRepository.deleteAll();

        // Then
        assertEquals(0, todoRepository.count());
        assertTrue(todoRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("Should update existing todo")
    void shouldUpdateExistingTodo() {
        // Given
        Todo todo = new Todo("Original title");
        Todo saved = todoRepository.save(todo);
        Long id = saved.getId();

        // When
        saved.setTitle("Updated title");
        saved.setDone(true);
        todoRepository.save(saved);

        // Then
        Optional<Todo> updated = todoRepository.findById(id);
        assertTrue(updated.isPresent());
        assertEquals("Updated title", updated.get().getTitle());
        assertTrue(updated.get().isDone());
    }
}
