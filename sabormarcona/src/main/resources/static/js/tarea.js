document.addEventListener('DOMContentLoaded', function () {
    let selectedWorker = null;
    let selectedPriority = null;

    const taskForm = document.getElementById('taskForm');
    
    function showToast(title, message, type = 'success') {
        // Tu función de Toast sigue igual...
        const toastEl = document.getElementById('notificationToast');
        if (toastEl) {
            const toast = new bootstrap.Toast(toastEl);
            const toastHeader = toastEl.querySelector('.toast-header');
            document.getElementById('toastTitle').textContent = title;
            document.getElementById('toastMessage').textContent = message;
            toastHeader.className = 'toast-header text-white';
            toastHeader.classList.add(`bg-${type}`);
            toast.show();
        } else {
            alert(`${title}: ${message}`);
        }
    }

    function clearForm() {
        // Tu función de limpiar formulario sigue igual...
        if (taskForm) {
            taskForm.reset();
        }
        selectedPriority = null;
        selectedWorker = null;
        document.querySelectorAll('.worker-card').forEach(card => {
            card.classList.remove('active', 'bg-light');
        });
        document.querySelectorAll('.priority-badge').forEach(btn => {
            btn.classList.remove('active', 'btn-success', 'btn-warning', 'btn-danger');
            btn.classList.add('btn-outline-success', 'btn-outline-warning', 'btn-outline-danger');
        });
        const currentAssignment = document.getElementById('currentAssignment');
        if (currentAssignment) {
            currentAssignment.style.display = 'none';
        }
        const trabajadorField = document.getElementById('trabajadorId'); // Corregido a trabajadorId
        if (trabajadorField) trabajadorField.value = '';
        const prioridadField = document.getElementById('prioridad');
        if (prioridadField) prioridadField.value = '';
        showToast('Formulario limpiado', 'Puedes asignar una nueva tarea.', 'info');
    }

    // ***** CORRECCIÓN CLAVE #1 *****
    // Esta función ahora guarda el ID del trabajador en el campo oculto correcto.
    window.selectWorkerFromData = function(element) {
        const id = element.getAttribute('data-id');
        const name = element.getAttribute('data-nombre');
        const role = element.getAttribute('data-rol');

        document.querySelectorAll('.worker-card').forEach(card => {
            card.classList.remove('active', 'bg-light');
        });
        element.classList.add('active', 'bg-light');
        
        selectedWorker = { id, name, role };
        
        // --- LA LÍNEA MÁGICA ---
        // Pone el ID del trabajador en el campo oculto del formulario principal
        document.getElementById('trabajadorId').value = id; 
        
        const currentAssignment = document.getElementById('currentAssignment');
        const selectedWorkerInfo = document.getElementById('selectedWorkerInfo');
        if (currentAssignment) currentAssignment.style.display = 'block';
        if (selectedWorkerInfo) selectedWorkerInfo.textContent = `${name} (${role})`;
    };

    window.selectPriority = function(element, priority) {
        document.querySelectorAll('.priority-badge').forEach(btn => {
            btn.classList.remove('active', 'btn-success', 'btn-warning', 'btn-danger');
            btn.classList.add('btn-outline-success', 'btn-outline-warning', 'btn-outline-danger');
        });
        element.classList.add('active');
        element.classList.remove('btn-outline-success', 'btn-outline-warning', 'btn-outline-danger');
        if (priority === 'Baja') element.classList.add('btn-success');
        else if (priority === 'Media') element.classList.add('btn-warning');
        else if (priority === 'Alta') element.classList.add('btn-danger');
        selectedPriority = priority;
        document.getElementById('prioridad').value = priority;
    };

    // Validación del formulario (sin cambios)
    if (taskForm) {
        taskForm.addEventListener('submit', function(e) {
            if (!document.getElementById('trabajadorId').value) { // Validamos el campo oculto
                e.preventDefault();
                showToast('Error', 'Debes seleccionar un trabajador primero.', 'danger');
                return false;
            }
            if (!selectedPriority) {
                e.preventDefault();
                showToast('Error', 'Debes seleccionar una prioridad.', 'danger');
                return false;
            }
        });
    }
    
    // ***** CORRECCIÓN CLAVE #2 *****
    // La función para editar ahora también maneja el ID del trabajador correctamente.
    window.editarTarea = async function(id) {
        try {
            const response = await fetch(`/tareas/obtener/${id}`);
            if (!response.ok) throw new Error('No se pudo obtener la tarea');
            const tarea = await response.json();
            
            document.getElementById('editId').value = tarea.id;
            document.getElementById('editTitulo').value = tarea.titulo;
            document.getElementById('editDescripcion').value = tarea.descripcion;

            // --- LA LÍNEA MÁGICA PARA EDITAR ---
            // Pone el ID del trabajador en el campo oculto del modal de edición
            if (tarea.trabajadorAsignado) {
                 document.getElementById('editTrabajadorId').value = tarea.trabajadorAsignado.id;
                 document.getElementById('editWorkerInfo').textContent = `${tarea.trabajadorAsignado.nombre} (${tarea.trabajadorAsignado.rol})`;
            }

            let fechaISO = tarea.fechaLimite ? tarea.fechaLimite.substring(0, 16) : '';
            document.getElementById('editFechaLimiteStr').value = fechaISO;

            document.querySelectorAll('.priority-badge-edit').forEach(btn => {
                if (btn.textContent.trim() === tarea.prioridad) {
                    window.selectPriorityEdit(btn, tarea.prioridad);
                }
            });

            document.getElementById('editEstado').value = tarea.estado;
            
            const editModal = new bootstrap.Modal(document.getElementById('modalEditarTarea'));
            editModal.show();

        } catch (error) {
            console.error('Error al editar tarea:', error);
            showToast('Error', 'No se pudo cargar la tarea para edición.', 'danger');
        }
    };
    
    // Función de ayuda para la prioridad en el modal (sin cambios)
    window.selectPriorityEdit = function(element, priority) {
        document.querySelectorAll('.priority-badge-edit').forEach(btn => {
            btn.classList.remove('active', 'btn-success', 'btn-warning', 'btn-danger');
            btn.classList.add('btn-outline-success', 'btn-outline-warning', 'btn-outline-danger');
        });
        element.classList.add('active');
        element.classList.remove('btn-outline-success', 'btn-outline-warning', 'btn-outline-danger');
        if (priority === 'Baja') element.classList.add('btn-success');
        else if (priority === 'Media') element.classList.add('btn-warning');
        else if (priority === 'Alta') element.classList.add('btn-danger');
        document.getElementById('editPrioridad').value = priority;
    };

    window.clearForm = clearForm;
});