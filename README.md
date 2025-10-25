# TinyTasks - CRUD Activity

**TinyTasks** is a micro-application for task management, developed with **Spring Boot** and native frontend (**HTML + JavaScript + Bootstrap**). The project implements a layered architecture with in-memory data and complete unit testing with JUnit 5.

---

## 📋 Description

TinyTasks allows you to manage a basic task list with the following operations:
- ✅ List all tasks
- ➕ Create new tasks
- 🔄 Toggle status (completed/pending)
- 🗑️ Delete tasks

The system does **NOT** use a database: data lives in memory using concurrent data structures.

---

## 🛠️ Technologies Used

### Backend
- **Java 17**
- **Spring Boot 3.5.7**
  - Spring Web
  - Spring Boot DevTools
  - Lombok
- **JUnit 5** and **Mockito** for unit testing
- **Maven** as dependency manager

### Frontend
- **HTML5**
- **JavaScript (ES6+)**
- **Bootstrap 5.3**
- **Bootstrap Icons**

---

## 📁 Project Structure

```
tinytask/
├── src/
│   ├── main/
│   │   ├── java/com/ssd/tinytask/
│   │   │   ├── config/
│   │   │   │   └── CorsConfig.java
│   │   │   ├── controller/
│   │   │   │   └── TodoController.java
│   │   │   ├── dto/
│   │   │   │   ├── CreateTodoRequest.java
│   │   │   │   └── ErrorResponse.java
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── InvalidTodoException.java
│   │   │   │   └── TodoNotFoundException.java
│   │   │   ├── model/
│   │   │   │   └── Todo.java
│   │   │   ├── repository/
│   │   │   │   └── TodoRepository.java
│   │   │   ├── service/
│   │   │   │   └── TodoService.java
│   │   │   └── TinytaskApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── index.html
│   │       │   ├── app.js
│   │       │   └── style.css
│   │       └── application.properties
│   └── test/
│       └── java/com/ssd/tinytask/
│           ├── repository/
│           │   └── TodoRepositoryTest.java
│           └── service/
│               └── TodoServiceTest.java
├── pom.xml
└── README.md
```

---

## 🚀 Installation and Execution

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### 1. Clone the repository
```bash
git clone https://github.com/java-lovelace/Samuel-Serna-Tinytask.git
cd Samuel-Serna-Tinytask
```

### 2. Compile the project
```bash
mvn clean install
```

### 3. Run the tests
```bash
mvn test
```

### 4. Run the application
```bash
mvn spring-boot:run
```

The backend will be available at: **http://localhost:8080**

### 5. Access the Frontend
Open in your browser:
```
http://localhost:8080/index.html
```

Or use Live Server in VS Code to serve files from `src/main/resources/static/`

---

## 📡 REST API - Endpoints

### Base URL
```
http://localhost:8080/api/todos
```

### Available Endpoints

#### 1. **GET /api/todos**
Gets all tasks.

**Response 200:**
```json
[
  {
    "id": 1,
    "title": "Learn Spring Boot",
    "done": false
  }
]
```

---

#### 2. **POST /api/todos**
Creates a new task.

**Request Body:**
```json
{
  "title": "Learn Spring Boot"
}
```

**Validations:**
- `title` is required
- `title` must be at least 3 characters

**Response 201:**
```json
{
  "id": 1,
  "title": "Learn Spring Boot",
  "done": false
}
```

**Response 400 (Error):**
```json
{
  "error": "Title is required"
}
```

---

#### 3. **PUT /api/todos/{id}/toggle**
Toggles task status (done: true ↔ false).

**Response 200:**
```json
{
  "id": 1,
  "title": "Learn Spring Boot",
  "done": true
}
```

**Response 404:**
```json
{
  "error": "Not found"
}
```

---

#### 4. **DELETE /api/todos/{id}**
Deletes a task.

**Response 204:** No Content

**Response 404:**
```json
{
  "error": "Not found"
}
```

---

## ✅ User Stories

### US-01: List tasks
**As** a user  
**I want** to see all registered tasks  
**So that** I know what I have pending

**Acceptance criteria:**
- ✅ Frontend executes `GET /api/todos`
- ✅ A list with all current tasks is displayed
- ✅ Statistics are shown (total and completed)

---

### US-02: Create task
**As** a user  
**I want** to create a new task  
**So that** I can keep my tasks organized

**Acceptance criteria:**
- ✅ There is a text field to enter the title
- ✅ When pressing "Add", `POST /api/todos` is executed
- ✅ The new task appears in the list
- ✅ If the title is invalid, an error message is shown

---

### US-03: Toggle status
**As** a user  
**I want** to mark or unmark a task as completed  
**So that** I can visualize my progress

**Acceptance criteria:**
- ✅ When pressing "Complete/Undo", `PUT /api/todos/{id}/toggle` is executed
- ✅ The visual status changes (strikethrough/normal)
- ✅ If the task doesn't exist, a 404 error is shown

---

### US-04: Delete task
**As** a user  
**I want** to delete a task I no longer need

**Acceptance criteria:**
- ✅ When pressing "Delete", confirmation is shown
- ✅ `DELETE /api/todos/{id}` is executed
- ✅ The task disappears from the list
- ✅ If the id doesn't exist, 404 error is shown

---

### US-05: Unit tests
**As** a developer  
**I want** to validate business logic  
**So that** I can ensure the system works correctly

**Acceptance criteria:**
- ✅ Project configured with JUnit 5 and Mockito
- ✅ Repository tests pass successfully
- ✅ Service tests pass successfully
- ✅ Positive and negative scenarios are covered

---

## 🧪 Unit Tests

### Run all tests
```bash
mvn test
```

### Test Coverage

#### TodoRepositoryTest
- ✅ Auto-incremental unique ID generation
- ✅ Save and retrieve by ID (positive)
- ✅ Return empty Optional when ID doesn't exist (negative)
- ✅ Find all tasks
- ✅ Delete by ID (positive)
- ✅ Return false when deleting non-existent ID (negative)
- ✅ Check existence by ID
- ✅ Count tasks
- ✅ Delete all tasks
- ✅ Update existing task

#### TodoServiceTest
- ✅ Create task with valid title (positive)
- ✅ Throw exception with empty title (negative)
- ✅ Throw exception with null title (negative)
- ✅ Throw exception with too short title (negative)
- ✅ Throw exception with null todo (negative)
- ✅ Set done=false when creating
- ✅ Toggle from false to true (positive)
- ✅ Toggle from true to false (positive)
- ✅ Return empty when toggling non-existent ID (negative)
- ✅ Delete existing task (positive)
- ✅ Return false when deleting non-existent ID (negative)
- ✅ Get all tasks
- ✅ Get task by ID (positive)
- ✅ Throw exception when getting non-existent ID (negative)

---

## 🎨 Frontend Features

### User Interface
- 🎨 Modern design with Bootstrap 5
- 📱 Responsive (mobile, tablet, desktop)
- 🌈 Attractive gradient background
- ✨ Smooth animations
- 🔔 Friendly error messages

### Functionalities
- ➕ Add tasks with real-time validation
- ✅ Mark tasks as completed (with visual strikethrough)
- ♻️ Unmark completed tasks
- 🗑️ Delete tasks with custom modal confirmation
- 📊 Real-time statistics (total and completed)
- 🔄 Automatic list reload
- 🎯 Empty state when no tasks exist

---

## 🔧 Configuration

### CORS
The backend is configured to accept requests from:
- `http://localhost:5500` (Live Server)
- `http://localhost:5501`
- `http://localhost:3000`
- `http://127.0.0.1:5500`
- And variants

### Server Port
The server runs on port **8080** by default. Can be changed in `application.properties`:
```properties
server.port=8080
```

---

## 📝 Validations

### Backend
- Title required (not null, not empty)
- Minimum title 3 characters
- Error handling with appropriate HTTP responses

### Frontend
- Required field
- Minimum 3 characters
- Validation before submitting
- Confirmation before deleting

---

## 🏗️ Architecture

### Backend Layers

#### 1. **Controller Layer** (`TodoController`)
- Handles HTTP requests
- Validates input with `@Valid`
- Returns appropriate responses

#### 2. **Service Layer** (`TodoService`)
- Contains business logic
- Validates business rules
- Throws exceptions when necessary

#### 3. **Repository Layer** (`TodoRepository`)
- Manages in-memory storage
- Uses `ConcurrentHashMap` for thread-safety
- Implements basic CRUD operations

#### 4. **Model Layer** (`Todo`)
- Represents domain entity
- Uses Lombok to reduce boilerplate

#### 5. **Exception Handling** (`GlobalExceptionHandler`)
- Catches exceptions globally
- Returns consistent error responses
- Handles Bean Validation validations

---

## 🎯 Implemented Best Practices

- ✅ Separation of responsibilities (SRP)
- ✅ Dependency injection with constructor
- ✅ Use of DTOs for requests
- ✅ Centralized exception handling
- ✅ Multi-layer validation
- ✅ Thread-safety in repository
- ✅ Unit tests with complete coverage
- ✅ Clean and documented code
- ✅ Naming conventions in English
- ✅ Appropriate HTTP responses

---

## 🐛 Troubleshooting

### Backend doesn't start
```bash
# Check that port 8080 is not in use
netstat -ano | findstr :8080

# Change port in application.properties if necessary
server.port=8081
```

### CORS errors in frontend
- Verify that backend is running
- Confirm that frontend URL is in the allowed CORS list
- Check that you're using the correct port in `app.js`

### Tests fail
```bash
# Clean and recompile
mvn clean test

# View detailed logs
mvn test -X
```

---

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Bootstrap Documentation](https://getbootstrap.com/docs/5.3/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)

---

## 📄 License

This project is part of an academic activity for Crudzaso.

---

## 🎉 Ready to use!

1. Run the backend: `mvn spring-boot:run`
2. Open browser: `http://localhost:8080/index.html`
3. Start managing your tasks!

---

**Happy Coding! 🚀**
