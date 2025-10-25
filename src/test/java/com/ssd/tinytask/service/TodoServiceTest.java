package com.ssd.tinytask.service;

import com.ssd.tinytask.exception.InvalidTodoException;
import com.ssd.tinytask.exception.TodoNotFoundException;
import com.ssd.tinytask.model.Todo;
import com.ssd.tinytask.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TodoService
 */
@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    private Todo sampleTodo;

    @BeforeEach
    void setUp() {
        sampleTodo = new Todo(1L, "Learn Spring Boot", false);
    }

    // ========== CREATE TODO TESTS ==========

    @Test
    @DisplayName("Should create todo with valid title - Positive scenario")
    void shouldCreateTodoWithValidTitle() {
        // Given
        Todo newTodo = new Todo("Valid task title");
        Todo savedTodo = new Todo(1L, "Valid task title", false);
        when(todoRepository.save(any(Todo.class))).thenReturn(savedTodo);

        // When
        Todo result = todoService.createTodo(newTodo);

        // Then
        assertNotNull(result);
        assertEquals("Valid task title", result.getTitle());
        assertFalse(result.isDone());
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    @DisplayName("Should throw exception when title is empty - Negative scenario")
    void shouldThrowExceptionWhenTitleIsEmpty() {
        // Given
        Todo invalidTodo = new Todo("");

        // When & Then
        InvalidTodoException exception = assertThrows(
                InvalidTodoException.class,
                () -> todoService.createTodo(invalidTodo)
        );
        assertEquals("Title is required", exception.getMessage());
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    @DisplayName("Should throw exception when title is null - Negative scenario")
    void shouldThrowExceptionWhenTitleIsNull() {
        // Given
        Todo invalidTodo = new Todo(null);

        // When & Then
        InvalidTodoException exception = assertThrows(
                InvalidTodoException.class,
                () -> todoService.createTodo(invalidTodo)
        );
        assertEquals("Title is required", exception.getMessage());
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    @DisplayName("Should throw exception when title is too short - Negative scenario")
    void shouldThrowExceptionWhenTitleIsTooShort() {
        // Given
        Todo invalidTodo = new Todo("AB"); // Only 2 characters

        // When & Then
        InvalidTodoException exception = assertThrows(
                InvalidTodoException.class,
                () -> todoService.createTodo(invalidTodo)
        );
        assertTrue(exception.getMessage().contains("at least 3 characters"));
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    @DisplayName("Should throw exception when todo is null - Negative scenario")
    void shouldThrowExceptionWhenTodoIsNull() {
        // When & Then
        InvalidTodoException exception = assertThrows(
                InvalidTodoException.class,
                () -> todoService.createTodo(null)
        );
        assertEquals("Todo cannot be null", exception.getMessage());
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    @DisplayName("Should set done to false when creating new todo")
    void shouldSetDoneToFalseWhenCreatingNewTodo() {
        // Given
        Todo newTodo = new Todo("New task");
        newTodo.setDone(true); // Try to set it to true
        when(todoRepository.save(any(Todo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Todo result = todoService.createTodo(newTodo);

        // Then
        assertFalse(result.isDone()); // Should be false regardless
    }

    // ========== TOGGLE TODO TESTS ==========

    @Test
    @DisplayName("Should toggle todo from false to true - Positive scenario")
    void shouldToggleTodoFromFalseToTrue() {
        // Given
        when(todoRepository.findById(1L)).thenReturn(Optional.of(sampleTodo));
        when(todoRepository.save(any(Todo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Optional<Todo> result = todoService.toggleTodo(1L);

        // Then
        assertTrue(result.isPresent());
        assertTrue(result.get().isDone());
        verify(todoRepository, times(1)).findById(1L);
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    @DisplayName("Should toggle todo from true to false - Positive scenario")
    void shouldToggleTodoFromTrueToFalse() {
        // Given
        sampleTodo.setDone(true);
        when(todoRepository.findById(1L)).thenReturn(Optional.of(sampleTodo));
        when(todoRepository.save(any(Todo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Optional<Todo> result = todoService.toggleTodo(1L);

        // Then
        assertTrue(result.isPresent());
        assertFalse(result.get().isDone());
    }

    @Test
    @DisplayName("Should return empty when toggling non-existent todo - Negative scenario")
    void shouldReturnEmptyWhenTogglingNonExistentTodo() {
        // Given
        when(todoRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Todo> result = todoService.toggleTodo(999L);

        // Then
        assertTrue(result.isEmpty());
        verify(todoRepository, times(1)).findById(999L);
        verify(todoRepository, never()).save(any(Todo.class));
    }

    // ========== DELETE TODO TESTS ==========

    @Test
    @DisplayName("Should delete existing todo - Positive scenario")
    void shouldDeleteExistingTodo() {
        // Given
        when(todoRepository.deleteById(1L)).thenReturn(true);

        // When
        boolean result = todoService.deleteTodo(1L);

        // Then
        assertTrue(result);
        verify(todoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should return false when deleting non-existent todo - Negative scenario")
    void shouldReturnFalseWhenDeletingNonExistentTodo() {
        // Given
        when(todoRepository.deleteById(999L)).thenReturn(false);

        // When
        boolean result = todoService.deleteTodo(999L);

        // Then
        assertFalse(result);
        verify(todoRepository, times(1)).deleteById(999L);
    }

    // ========== GET TODOS TESTS ==========

    @Test
    @DisplayName("Should get all todos")
    void shouldGetAllTodos() {
        // Given
        List<Todo> todos = Arrays.asList(
                new Todo(1L, "Task 1", false),
                new Todo(2L, "Task 2", true)
        );
        when(todoRepository.findAll()).thenReturn(todos);

        // When
        List<Todo> result = todoService.getAllTodos();

        // Then
        assertEquals(2, result.size());
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get todo by ID - Positive scenario")
    void shouldGetTodoById() {
        // Given
        when(todoRepository.findById(1L)).thenReturn(Optional.of(sampleTodo));

        // When
        Todo result = todoService.getTodoById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Learn Spring Boot", result.getTitle());
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when getting non-existent todo - Negative scenario")
    void shouldThrowExceptionWhenGettingNonExistentTodo() {
        // Given
        when(todoRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TodoNotFoundException.class, () -> todoService.getTodoById(999L));
        verify(todoRepository, times(1)).findById(999L);
    }
}
