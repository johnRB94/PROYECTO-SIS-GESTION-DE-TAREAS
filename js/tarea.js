document.addEventListener('DOMContentLoaded', function () {
    // Variables globales
    let selectedWorker = null;
    let selectedPriority = null;
    let rowToDelete = null; 
    let rowToEdit = null; // NUEVO: Para saber qué fila editar

    // Selectores de elementos del DOM
    const taskForm = document.getElementById('taskForm');
    const taskList = document.getElementById('taskList');
    
    // Selectores para el modal de eliminación
    const deleteModalEl = document.getElementById('deleteTaskModal');
    const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');

    // NUEVO: Selectores para el modal de edición
    const editModalEl = document.getElementById('editTaskModal');
    const saveChangesBtn = document.getElementById('saveChangesBtn');
    const editTaskForm = document.getElementById('editTaskForm');
    const editPriorityButtons = document.getElementById('editPriorityButtons');

    // --- FUNCIONES PRINCIPALES ---

    // Mostrar notificación (Toast)
    function showToast(title, message, type = 'success') {
        const toastEl = document.getElementById('notificationToast');
        const toast = new bootstrap.Toast(toastEl);
        const toastHeader = toastEl.querySelector('.toast-header');
        
        document.getElementById('toastTitle').textContent = title;
        document.getElementById('toastMessage').textContent = message;
        
        toastHeader.className = 'toast-header text-white'; // Reset class
        toastHeader.classList.add(`bg-${type}`);
        
        toast.show();
    }

    // Limpiar formulario principal
    function clearForm() {
        taskForm.reset();
        selectedPriority = null;
        document.querySelectorAll('#taskForm .priority-badge').forEach(btn => btn.classList.remove('active'));
        document.getElementById('taskPriority').value = '';
        showToast('Formulario limpiado', 'Puedes asignar una nueva tarea.', 'info');
    }

    // Agregar una nueva tarea a la tabla
    function addTaskToTable(task) {
        const priorityClass = {
            'Alta': 'badge-priority-high', 'Media': 'badge-priority-medium', 'Baja': 'badge-priority-low'
        };

        const newRow = taskList.insertRow(0); // Inserta la fila al principio de la tabla
        newRow.innerHTML = `
            <td>
                <p class="fw-bold mb-1 task-title">${task.title}</p>
                <p class="text-muted mb-0 small task-description">${task.description}</p>
            </td>
            <td class="task-worker">${task.worker.name}</td>
            <td class="task-priority"><span class="badge rounded-pill text-white ${priorityClass[task.priority]}">${task.priority}</span></td>
            <td class="task-due-time">${new Date(task.dueTime).toLocaleString('es-PE')}</td>
            <td>
                <button class="btn btn-outline-primary btn-sm edit-task-btn"><i class="fas fa-pencil-alt"></i></button>
                <button class="btn btn-outline-danger btn-sm delete-task-btn"><i class="fas fa-trash"></i></button>
            </td>
        `;
    }

    // --- LÓGICA DE EVENTOS ---

    // Evento para el envío del formulario de NUEVA TAREA
    taskForm.addEventListener('submit', function(event) {
        event.preventDefault();
        if (!selectedWorker) {
            showToast('Error', 'Debes seleccionar un trabajador primero.', 'danger'); return;
        }
        if (!document.getElementById('taskPriority').value) {
            showToast('Error', 'Debes seleccionar una prioridad.', 'danger'); return;
        }

        const taskData = {
            title: document.getElementById('taskTitle').value,
            description: document.getElementById('taskDescription').value,
            worker: selectedWorker,
            priority: document.getElementById('taskPriority').value,
            dueTime: document.getElementById('dueTime').value
        };
        
        addTaskToTable(taskData);
        showToast('¡Éxito!', `Tarea "${taskData.title}" asignada.`, 'success');
        clearForm();
    });

    // Delegación de eventos para los botones de la tabla (Editar y Eliminar)
    taskList.addEventListener('click', function(event) {
        const target = event.target;
        const deleteButton = target.closest('.delete-task-btn');
        const editButton = target.closest('.edit-task-btn');

        if (deleteButton) {
            rowToDelete = deleteButton.closest('tr');
            const modal = new bootstrap.Modal(deleteModalEl);
            modal.show();
        }

        if (editButton) {
            rowToEdit = editButton.closest('tr');
            
            // 1. Extraer datos de la fila seleccionada
            const title = rowToEdit.querySelector('.task-title').textContent;
            const description = rowToEdit.querySelector('.task-description').textContent;
            const priority = rowToEdit.querySelector('.task-priority span').textContent;
            // Para la fecha, necesitamos convertirla al formato que el input[datetime-local] entiende
            const dueTimeText = rowToEdit.querySelector('.task-due-time').textContent;
            const [datePart, timePart] = dueTimeText.split(', ');
            const [day, month, year] = datePart.split('/');
            const formattedDate = `${year}-${month}-${day}T${timePart}`;
            
            // 2. Poblar el formulario del modal con esos datos
            document.getElementById('editTaskTitle').value = title;
            document.getElementById('editTaskDescription').value = description;
            document.getElementById('editDueTime').value = formattedDate;
            document.getElementById('editTaskPriority').value = priority;

            // 3. Marcar el botón de prioridad correcto
            editPriorityButtons.querySelectorAll('.edit-priority-badge').forEach(btn => {
                btn.classList.remove('active');
                if (btn.dataset.priority === priority) {
                    btn.classList.add('active');
                }
            });

            // 4. Mostrar el modal de edición
            const modal = new bootstrap.Modal(editModalEl);
            modal.show();
        }
    });

    // Evento para los botones de prioridad DENTRO del modal de edición
    editPriorityButtons.addEventListener('click', function(event) {
        const target = event.target.closest('.edit-priority-badge');
        if (target) {
            editPriorityButtons.querySelectorAll('.edit-priority-badge').forEach(btn => btn.classList.remove('active'));
            target.classList.add('active');
            document.getElementById('editTaskPriority').value = target.dataset.priority;
        }
    });

    // Evento para guardar los cambios del modal de edición
    saveChangesBtn.addEventListener('click', function() {
        // 1. Leer los nuevos valores del formulario del modal
        const newTitle = document.getElementById('editTaskTitle').value;
        const newDescription = document.getElementById('editTaskDescription').value;
        const newPriority = document.getElementById('editTaskPriority').value;
        const newDueTime = document.getElementById('editDueTime').value;

        const priorityClass = {
            'Alta': 'badge-priority-high', 'Media': 'badge-priority-medium', 'Baja': 'badge-priority-low'
        };

        // 2. Actualizar el contenido de la fila en la tabla
        if (rowToEdit) {
            rowToEdit.querySelector('.task-title').textContent = newTitle;
            rowToEdit.querySelector('.task-description').textContent = newDescription;
            rowToEdit.querySelector('.task-priority').innerHTML = `<span class="badge rounded-pill text-white ${priorityClass[newPriority]}">${newPriority}</span>`;
            rowToEdit.querySelector('.task-due-time').textContent = new Date(newDueTime).toLocaleString('es-PE');
        }

        // 3. Ocultar el modal y mostrar notificación
        const modal = bootstrap.Modal.getInstance(editModalEl);
        modal.hide();
        showToast('Tarea Actualizada', 'Los cambios se guardaron correctamente.', 'success');
    });

    // Evento para confirmar la eliminación de una tarea
    confirmDeleteBtn.addEventListener('click', function () {
        if (rowToDelete) {
            rowToDelete.remove();
            showToast('Tarea Eliminada', 'La tarea ha sido eliminada.', 'info');
            const modal = bootstrap.Modal.getInstance(deleteModalEl);
            modal.hide();
            rowToDelete = null;
        }
    });

    // --- FUNCIONES AUXILIARES --- (Las que ya tenías)
    window.selectWorker = function(element, name, role) {
        document.querySelectorAll('.worker-card').forEach(card => card.classList.remove('active', 'bg-light'));
        element.classList.add('active', 'bg-light');
        selectedWorker = { name, role };
        document.getElementById('currentAssignment').style.display = 'block';
        document.getElementById('selectedWorkerInfo').textContent = `${name} (${role})`;
    };
    
    window.selectPriority = function(element, priority) {
        selectedPriority = priority;
        document.getElementById('taskPriority').value = priority;
        document.querySelectorAll('#taskForm .priority-badge').forEach(btn => btn.classList.remove('active'));
        element.classList.add('active');
    };

    window.logout = function() {
        showToast('Cerrando Sesión', 'Redirigiendo...', 'warning');
        setTimeout(() => { window.location.href = '#'; }, 1500);
    };

    window.clearForm = clearForm; // Hacer la función accesible globalmente
});