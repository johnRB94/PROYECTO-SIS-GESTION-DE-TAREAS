/**
 * Lógica JavaScript para el modal de edición de incidencias.
 * Se asume que este archivo se carga después de Bootstrap.bundle.js.
 */

// Se hace global para que pueda ser llamado desde el atributo 'onclick' de Thymeleaf en la tabla.
window.editarIncidencia = async function(id) {
    
    // 1. Fetch de la incidencia por ID al endpoint REST del Controller
    try {
        // La URL debe coincidir con el @RequestMapping del Controller: /incidencias/obtener/{id}
        const response = await fetch(`/incidencias/obtener/${id}`);
        if (!response.ok) {
            throw new Error('No se pudo obtener la incidencia. Error ' + response.status);
        }
        const incidencia = await response.json();
        
        if (!incidencia) return;

        // 2. Llenar los campos del formulario en el modal
        document.getElementById('editId').value = incidencia.id;
        document.getElementById('editTitulo').value = incidencia.titulo;
        document.getElementById('editDescripcion').value = incidencia.descripcion || '';
        
        // **IMPORTANTE**: Formatear la fecha para el input type="datetime-local" (YYYY-MM-DDThh:mm)
        let fechaISO = '';
        // Spring con Jackson serializa LocalDateTime como un array [año, mes, día, hora, minuto, segundo...]
        if (Array.isArray(incidencia.fechaLimite) && incidencia.fechaLimite.length >= 5) {
            const [y, m, d, h, min] = incidencia.fechaLimite;
            const pad = (num) => String(num).padStart(2, '0');
            fechaISO = `${y}-${pad(m)}-${pad(d)}T${pad(h)}:${pad(min)}`;
        } else if (typeof incidencia.fechaLimite === 'string') {
            // Si es un string ISO (ej: 2025-10-01T15:30:00.123), tomamos solo los primeros 16 caracteres
            fechaISO = incidencia.fechaLimite.substring(0, 16); 
        }
        
        document.getElementById('editFechaLimite').value = fechaISO;
        document.getElementById('editPrioridad').value = incidencia.prioridad;
        document.getElementById('editEstado').value = incidencia.estado;
        
        // Llenar campos ocultos de Trabajador/Rol y la info visible
        document.getElementById('editTrabajador').value = incidencia.trabajador || '';
        document.getElementById('editRol').value = incidencia.rol || '';
        document.getElementById('editWorkerInfo').textContent = `${incidencia.trabajador || 'N/A'} (${incidencia.rol || 'N/A'})`;


        // 3. Mostrar el modal (requiere que Bootstrap esté cargado)
        const editModal = new bootstrap.Modal(document.getElementById('modalEditarIncidencia'));
        editModal.show();

    } catch (error) {
        console.error('Error al cargar la incidencia:', error);
        alert('Hubo un error al cargar los datos de la incidencia. Revisa la consola para detalles.');
    }
};