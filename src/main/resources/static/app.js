// ========================================
// API CONFIGURATION
// ========================================

const API_URL = 'http://localhost:8080/api/todos';

// ========================================
// DOM ELEMENTS
// ========================================

const taskInput = document.getElementById('taskInput');
const addTaskForm = document.getElementById('addTaskForm');
const taskList = document.getElementById('taskList');
const errorMessage = document.getElementById('errorMessage');
const errorText = document.getElementById('errorText');
const successMessage = document.getElementById('successMessage');
const loading = document.getElementById('loading');
const emptyState = document.getElementById('emptyState');

// Header Stats
const headerTotalTasks = document.getElementById('headerTotalTasks');
const headerCompletedTasks = document.getElementById('headerCompletedTasks');
const headerPendingTasks = document.getElementById('headerPendingTasks');

// Stats
const taskCount = document.getElementById('taskCount');
const completionRate = document.getElementById('completionRate');
const progressPercentage = document.getElementById('progressPercentage');
const progressCircle = document.getElementById('progressCircle');

// Filter buttons
const filterButtons = document.querySelectorAll('.filter-btn');

// Toast
const successToast = document.getElementById('successToast');
const toastMessage = document.getElementById('toastMessage');

// ========================================
// STATE MANAGEMENT
// ========================================

let currentFilter = 'all';
let allTasks = [];

// ========================================
// INITIALIZATION
// ========================================

document.addEventListener('DOMContentLoaded', () => {
    loadTasks();
    setupEventListeners();
    setupProgressRingGradient();
});

// ========================================
// EVENT LISTENERS
// ========================================

function setupEventListeners() {
    addTaskForm.addEventListener('submit', handleAddTask);
    
    filterButtons.forEach(btn => {
        btn.addEventListener('click', (e) => {
            filterButtons.forEach(b => b.classList.remove('active'));
            e.target.classList.add('active');
            currentFilter = e.target.dataset.filter;
            renderFilteredTasks();
        });
    });
}

// ========================================
// SVG GRADIENT SETUP
// ========================================

function setupProgressRingGradient() {
    const svg = document.querySelector('.progress-ring svg');
    const defs = document.createElementNS('http://www.w3.org/2000/svg', 'defs');
    const gradient = document.createElementNS('http://www.w3.org/2000/svg', 'linearGradient');
    
    gradient.setAttribute('id', 'gradient');
    gradient.setAttribute('x1', '0%');
    gradient.setAttribute('y1', '0%');
    gradient.setAttribute('x2', '100%');
    gradient.setAttribute('y2', '100%');
    
    const stop1 = document.createElementNS('http://www.w3.org/2000/svg', 'stop');
    stop1.setAttribute('offset', '0%');
    stop1.setAttribute('style', 'stop-color:#6366f1;stop-opacity:1');
    
    const stop2 = document.createElementNS('http://www.w3.org/2000/svg', 'stop');
    stop2.setAttribute('offset', '100%');
    stop2.setAttribute('style', 'stop-color:#8b5cf6;stop-opacity:1');
    
    gradient.appendChild(stop1);
    gradient.appendChild(stop2);
    defs.appendChild(gradient);
    svg.insertBefore(defs, svg.firstChild);
}

// ========================================
// API FUNCTIONS
// ========================================

async function loadTasks() {
    try {
        showLoading(true);
        hideError();
        
        const response = await fetch(API_URL);
        
        if (!response.ok) {
            throw new Error('Failed to load tasks');
        }
        
        allTasks = await response.json();
        renderFilteredTasks();
        updateAllStats(allTasks);
        
    } catch (error) {
        console.error('Error loading tasks:', error);
        showError('Failed to load tasks. Please check if the backend is running.');
    } finally {
        showLoading(false);
    }
}

async function handleAddTask(e) {
    e.preventDefault();
    
    const title = taskInput.value.trim();
    
    if (title.length < 3) {
        showError('Title must be at least 3 characters');
        return;
    }
    
    try {
        hideError();
        hideSuccess();
        
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ title })
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.error || 'Failed to create task');
        }
        
        taskInput.value = '';
        showSuccess();
        showToast('Task added successfully!');
        await loadTasks();
        
    } catch (error) {
        console.error('Error creating task:', error);
        showError(error.message);
    }
}

async function handleToggleTask(id) {
    try {
        const response = await fetch(`${API_URL}/${id}/toggle`, {
            method: 'PUT'
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.error || 'Failed to toggle task');
        }
        
        showToast('Task status updated!');
        await loadTasks();
        
    } catch (error) {
        console.error('Error toggling task:', error);
        showError(error.message);
    }
}

async function handleDeleteTask(id, taskTitle) {
    // Show custom modal instead of browser confirm
    showDeleteModal(id, taskTitle);
}

async function confirmDeleteTask(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.error || 'Failed to delete task');
        }
        
        showToast('Task deleted successfully!');
        await loadTasks();
        
    } catch (error) {
        console.error('Error deleting task:', error);
        showError(error.message);
    }
}

// ========================================
// RENDER FUNCTIONS
// ========================================

function renderFilteredTasks() {
    let filteredTasks = allTasks;
    
    if (currentFilter === 'completed') {
        filteredTasks = allTasks.filter(task => task.done);
    } else if (currentFilter === 'pending') {
        filteredTasks = allTasks.filter(task => !task.done);
    }
    
    renderTasks(filteredTasks);
}

function renderTasks(tasks) {
    taskList.innerHTML = '';
    
    if (tasks.length === 0) {
        emptyState.classList.remove('d-none');
        taskList.classList.add('d-none');
    } else {
        emptyState.classList.add('d-none');
        taskList.classList.remove('d-none');
        
        tasks.forEach((task, index) => {
            const taskElement = createTaskElement(task, index);
            taskList.appendChild(taskElement);
        });
    }
    
    taskCount.textContent = tasks.length;
}

function createTaskElement(task, index) {
    const div = document.createElement('div');
    div.className = `task-item ${task.done ? 'completed' : ''}`;
    div.dataset.id = task.id;
    div.style.animationDelay = `${index * 0.05}s`;
    
    // Checkbox
    const checkbox = document.createElement('div');
    checkbox.className = `task-checkbox ${task.done ? 'checked' : ''}`;
    checkbox.onclick = () => handleToggleTask(task.id);
    
    if (task.done) {
        checkbox.innerHTML = '<i class="bi bi-check-lg"></i>';
    }
    
    // Content
    const content = document.createElement('div');
    content.className = 'task-content';
    
    const title = document.createElement('div');
    title.className = 'task-title';
    title.textContent = task.title;
    
    content.appendChild(title);
    
    // Actions
    const actions = document.createElement('div');
    actions.className = 'task-actions';
    
    const toggleBtn = document.createElement('button');
    toggleBtn.className = 'action-btn action-btn-toggle';
    toggleBtn.innerHTML = task.done 
        ? '<i class="bi bi-arrow-counterclockwise"></i>' 
        : '<i class="bi bi-check-lg"></i>';
    toggleBtn.title = task.done ? 'Mark as pending' : 'Mark as completed';
    toggleBtn.onclick = () => handleToggleTask(task.id);
    
    const deleteBtn = document.createElement('button');
    deleteBtn.className = 'action-btn action-btn-delete';
    deleteBtn.innerHTML = '<i class="bi bi-trash"></i>';
    deleteBtn.title = 'Delete task';
    deleteBtn.onclick = () => handleDeleteTask(task.id, task.title);
    
    actions.appendChild(toggleBtn);
    actions.appendChild(deleteBtn);
    
    // Assemble
    div.appendChild(checkbox);
    div.appendChild(content);
    div.appendChild(actions);
    
    return div;
}

// ========================================
// STATS FUNCTIONS
// ========================================

function updateAllStats(tasks) {
    const total = tasks.length;
    const completed = tasks.filter(task => task.done).length;
    const pending = total - completed;
    const percentage = total > 0 ? Math.round((completed / total) * 100) : 0;
    
    // Header stats - Update directly without animation to avoid issues
    headerTotalTasks.textContent = total;
    headerCompletedTasks.textContent = completed;
    headerPendingTasks.textContent = pending;
    
    // Completion rate
    completionRate.textContent = `${percentage}%`;
    progressPercentage.textContent = `${percentage}%`;
    
    // Update progress circle
    updateProgressCircle(percentage);
}

function updateProgressCircle(percentage) {
    const circumference = 2 * Math.PI * 32; // radius = 32
    const offset = circumference - (percentage / 100) * circumference;
    progressCircle.style.strokeDashoffset = offset;
}

function animateValue(element, start, end, duration) {
    const range = end - start;
    const increment = range / (duration / 16);
    let current = start;
    
    const timer = setInterval(() => {
        current += increment;
        if ((increment > 0 && current >= end) || (increment < 0 && current <= end)) {
            current = end;
            clearInterval(timer);
        }
        element.textContent = Math.round(current);
    }, 16);
}

// ========================================
// UI HELPER FUNCTIONS
// ========================================

function showLoading(show) {
    if (show) {
        loading.classList.remove('d-none');
        taskList.classList.add('d-none');
        emptyState.classList.add('d-none');
    } else {
        loading.classList.add('d-none');
    }
}

function showError(message) {
    errorText.textContent = message;
    errorMessage.classList.remove('d-none');
    
    // Auto-hide after 5 seconds
    setTimeout(() => {
        hideError();
    }, 5000);
}

function hideError() {
    errorMessage.classList.add('d-none');
}

function showSuccess() {
    successMessage.classList.remove('d-none');
    
    setTimeout(() => {
        hideSuccess();
    }, 3000);
}

function hideSuccess() {
    successMessage.classList.add('d-none');
}

function showToast(message) {
    toastMessage.textContent = message;
    const toast = new bootstrap.Toast(successToast);
    toast.show();
}

// ========================================
// UTILITY FUNCTIONS
// ========================================

// Add keyboard shortcuts
document.addEventListener('keydown', (e) => {
    // Ctrl/Cmd + K to focus input
    if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
        e.preventDefault();
        taskInput.focus();
    }
});

// Add input validation feedback
taskInput.addEventListener('input', (e) => {
    const value = e.target.value.trim();
    
    if (value.length > 0 && value.length < 3) {
        taskInput.style.borderColor = '#ef4444';
    } else if (value.length >= 3) {
        taskInput.style.borderColor = '#10b981';
    } else {
        taskInput.style.borderColor = '';
    }
});

// Clear validation on focus out
taskInput.addEventListener('blur', () => {
    taskInput.style.borderColor = '';
});

// ========================================
// DELETE MODAL FUNCTIONS
// ========================================

function showDeleteModal(taskId, taskTitle) {
    const modal = document.getElementById('deleteModal');
    const modalTitle = document.getElementById('deleteModalTaskTitle');
    const confirmBtn = document.getElementById('confirmDeleteBtn');
    
    modalTitle.textContent = taskTitle;
    
    // Remove previous event listeners
    const newConfirmBtn = confirmBtn.cloneNode(true);
    confirmBtn.parentNode.replaceChild(newConfirmBtn, confirmBtn);
    
    // Add new event listener
    newConfirmBtn.addEventListener('click', () => {
        hideDeleteModal();
        confirmDeleteTask(taskId);
    });
    
    // Show modal with animation
    modal.classList.add('show');
    document.body.style.overflow = 'hidden';
}

function hideDeleteModal() {
    const modal = document.getElementById('deleteModal');
    modal.classList.remove('show');
    document.body.style.overflow = '';
}

// Close modal when clicking outside
document.addEventListener('click', (e) => {
    const modal = document.getElementById('deleteModal');
    if (e.target === modal) {
        hideDeleteModal();
    }
});

// Close modal with Escape key
document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
        const modal = document.getElementById('deleteModal');
        if (modal.classList.contains('show')) {
            hideDeleteModal();
        }
    }
});
