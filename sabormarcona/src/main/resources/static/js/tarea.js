document.addEventListener('DOMContentLoaded', function () {
    // Variables globales
    let selectedWorker = null;
    let selectedPriority = null;
    let rowToDelete = null; 
    let rowToEdit = null;

    // Selectores de elementos del DOM
    const taskForm = document.getElementById('taskForm');
    
    // Mostrar notificación (Toast)
    function showToast(title, message, type = 'success') {
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
            // Fallback si no hay toast
            alert(`${title}: ${message}`);
        }
    }

    // Limpiar formulario principal
    function clearForm() {
        if (taskForm) {
            taskForm.reset();
        }
        selectedPriority = null;
        selectedWorker = null;
        
        // Limpiar selección de trabajadores
        document.querySelectorAll('.worker-card').forEach(card => {
            card.classList.remove('active', 'bg-light');
        });
        
        // Limpiar selección de prioridad
        document.querySelectorAll('.priority-badge').forEach(btn => {
            btn.classList.remove('active', 'btn-success', 'btn-warning', 'btn-danger');
            btn.classList.add('btn-outline-success', 'btn-outline-warning', 'btn-outline-danger');
        });
        
        // Ocultar información de asignación
        const currentAssignment = document.getElementById('currentAssignment');
        if (currentAssignment) {
            currentAssignment.style.display = 'none';
        }
        
        // Limpiar campos ocultos
        const trabajadorField = document.getElementById('trabajador');
        const rolField = document.getElementById('rol');
        const prioridadField = document.getElementById('prioridad');
        
        if (trabajadorField) trabajadorField.value = '';
        if (rolField) rolField.value = '';
        if (prioridadField) prioridadField.value = '';
        
        showToast('Formulario limpiado', 'Puedes asignar una nueva tarea.', 'info');
    }

    // --- FUNCIONES PARA TRABAJADORES ---
    window.selectWorker = function(element, name, role) {
        // Limpiar selección anterior
        document.querySelectorAll('.worker-card').forEach(card => {
            card.classList.remove('active', 'bg-light');
        });
        
        // Marcar como seleccionado
        element.classList.add('active', 'bg-light');
        
        // Guardar trabajador seleccionado
        selectedWorker = { name, role };
        
        // Actualizar campos ocultos del formulario
        const trabajadorField = document.getElementById('trabajador');
        const rolField = document.getElementById('rol');
        
        if (trabajadorField) trabajadorField.value = name;
        if (rolField) rolField.value = role;
        
        // Mostrar información de asignación
        const currentAssignment = document.getElementById('currentAssignment');
        const selectedWorkerInfo = document.getElementById('selectedWorkerInfo');
        
        if (currentAssignment) currentAssignment.style.display = 'block';
        if (selectedWorkerInfo) selectedWorkerInfo.textContent = `${name} (${role})`;
    };

    // --- FUNCIONES PARA PRIORIDAD ---
    window.selectPriority = function(element, priority) {
        // Limpiar selección anterior
        document.querySelectorAll('.priority-badge').forEach(btn => {
            btn.classList.remove('active', 'btn-success', 'btn-warning', 'btn-danger');
            btn.classList.add('btn-outline-success', 'btn-outline-warning', 'btn-outline-danger');
        });
        
        // Marcar como seleccionado
        element.classList.add('active');
        element.classList.remove('btn-outline-success', 'btn-outline-warning', 'btn-outline-danger');
        
        // Aplicar color según prioridad
        if (priority === 'Baja') {
            element.classList.add('btn-success');
        } else if (priority === 'Media') {
            element.classList.add('btn-warning');
        } else if (priority === 'Alta') {
            element.classList.add('btn-danger');
        }
        
        // Guardar prioridad seleccionada
        selectedPriority = priority;
        const prioridadField = document.getElementById('prioridad');
        if (prioridadField) prioridadField.value = priority;
    };

    // --- VALIDACIÓN DEL FORMULARIO ---
    if (taskForm) {
        taskForm.addEventListener('submit', function(e) {
            // Validar trabajador seleccionado
            if (!selectedWorker) {
                e.preventDefault();
                showToast('Error', 'Debes seleccionar un trabajador primero.', 'danger');
                return false;
            }
            
            // Validar prioridad seleccionada
            if (!selectedPriority) {
                e.preventDefault();
                showToast('Error', 'Debes seleccionar una prioridad.', 'danger');
                return false;
            }
            
            // Validar campos requeridos
            const titulo = document.getElementById('titulo');
            const fechaLimite = document.getElementById('fechaLimiteStr');
            
            if (!titulo || !titulo.value.trim()) {
                e.preventDefault();
                showToast('Error', 'El título es requerido.', 'danger');
                return false;
            }
            
            if (!fechaLimite || !fechaLimite.value) {
                e.preventDefault();
                showToast('Error', 'La fecha límite es requerida.', 'danger');
                return false;
            }
            
            // Si todo está bien, mostrar mensaje de éxito (se ejecutará después del redirect)
            showToast('¡Éxito!', `Tarea "${titulo.value}" será asignada.`, 'success');
            return true;
        });
    }

    // --- FUNCIONES AUXILIARES GLOBALES ---
    window.clearForm = clearForm;

    window.logout = function() {
        showToast('Cerrando Sesión', 'Redirigiendo...', 'warning');
        setTimeout(() => { 
            window.location.href = '/'; 
        }, 1500);
    };

    // --- FUNCIONES PARA EDICIÓN Y ELIMINACIÓN ---
    // (Estas son para futuras implementaciones con modales)
    
    // Función para eliminar tarea
    window.eliminarTarea = function(id) {
        if (confirm('¿Estás seguro de que deseas eliminar esta tarea?')) {
            // El formulario ya maneja el POST request
            return true;
        }
        return false;
    };

    // Función para editar tarea (para implementar en el futuro)
    // Tarea.js (Reemplazar/Añadir)

// Función auxiliar para manejar la selección de prioridad en el modal
window.selectPriorityEdit = function(element, priority) {
    // Lógica para marcar el botón de prioridad en el modal
    document.querySelectorAll('.priority-badge-edit').forEach(btn => {
        btn.classList.remove('active', 'btn-success', 'btn-warning', 'btn-danger');
        btn.classList.add('btn-outline-success', 'btn-outline-warning', 'btn-outline-danger');
    });

    element.classList.add('active');
    element.classList.remove('btn-outline-success', 'btn-outline-warning', 'btn-outline-danger');

    if (priority === 'Baja') {
        element.classList.add('btn-success');
    } else if (priority === 'Media') {
        element.classList.add('btn-warning');
    } else if (priority === 'Alta') {
        element.classList.add('btn-danger');
    }

    // Guardar prioridad seleccionada en el campo oculto del modal de edición
    const prioridadField = document.getElementById('editPrioridad');
    if (prioridadField) prioridadField.value = priority;
};


// Función principal para editar tarea (Reemplaza el placeholder en la línea 938)
window.editarTarea = async function(id) {
    console.log('Cargando tarea con ID:', id);

    // 1. Fetch de la tarea por ID
    try {
        const response = await fetch(`/tareas/obtener/${id}`);
        if (!response.ok) {
            throw new Error('No se pudo obtener la tarea');
        }
        const tarea = await response.json();
        
        // 2. Llenar el formulario del modal
        document.getElementById('editId').value = tarea.id;
        document.getElementById('editTitulo').value = tarea.titulo;
        document.getElementById('editDescripcion').value = tarea.descripcion;
        
        // Formatear la fecha para input datetime-local (yyyy-MM-ddThh:mm)
        // Se asume que el backend devuelve un array [año, mes, día, hora, minuto] o un string ISO
        let fechaISO;
        if (Array.isArray(tarea.fechaLimite)) {
            const [y, m, d, h, min] = tarea.fechaLimite;
            const pad = (num) => num.toString().padStart(2, '0');
            fechaISO = `${y}-${pad(m)}-${pad(d)}T${pad(h)}:${pad(min)}`;
        } else {
            // Si es un string ISO (ej: 2025-10-01T15:30:00.123)
            fechaISO = tarea.fechaLimite.substring(0, 16); 
        }
        document.getElementById('editFechaLimiteStr').value = fechaISO;

        // Prioridad: Seleccionar el botón de prioridad en el modal
        const prioridad = tarea.prioridad;
        document.querySelectorAll('.priority-badge-edit').forEach(btn => {
            if (btn.textContent.trim() === prioridad) {
                window.selectPriorityEdit(btn, prioridad); // Usa la nueva función
            }
        });

        // Estado: Seleccionar la opción correcta
        document.getElementById('editEstado').value = tarea.estado;

        // Trabajador: Llenar campos ocultos y la info visible (no se puede cambiar el trabajador al editar)
        document.getElementById('editTrabajador').value = tarea.trabajador;
        document.getElementById('editRol').value = tarea.rol;
        document.getElementById('editWorkerInfo').textContent = `${tarea.trabajador} (${tarea.rol})`;
        
        // 3. Mostrar el modal
        const editModal = new bootstrap.Modal(document.getElementById('modalEditarTarea'));
        editModal.show();

    } catch (error) {
        console.error('Error al editar tarea:', error);
        window.showToast('Error', 'No se pudo cargar la tarea para edición.', 'danger');
    }
};


const editTaskForm = document.getElementById('editTaskForm');
if (editTaskForm) {
    editTaskForm.addEventListener('submit', function(e) {
        window.showToast('Enviando...', `Actualizando tarea...`, 'info');
    });
}

    // --- INICIALIZACIÓN ---
    console.log('Tarea.js cargado correctamente para Spring Boot + Thymeleaf');
    
    // Mostrar mensaje de bienvenida
    setTimeout(() => {
        showToast('Sistema de Tareas', 'Selecciona un trabajador para comenzar', 'info');
    }, 1000);

    // Nueva función para manejar datos desde atributos data-*
window.selectWorkerFromData = function(element) {
    const name = element.getAttribute('data-nombre');
    const role = element.getAttribute('data-rol');
    window.selectWorker(element, name, role);
};
});
