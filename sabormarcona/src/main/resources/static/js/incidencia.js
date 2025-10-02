window.editarIncidencia = async function(id) {
    
    try {
        const response = await fetch(`/incidencias/obtener/${id}`);
        if (!response.ok) {
            throw new Error('No se pudo obtener la incidencia. Error ' + response.status);
        }
        const incidencia = await response.json();
        
        if (!incidencia) return;

        document.getElementById('editId').value = incidencia.id;
        document.getElementById('editTitulo').value = incidencia.titulo;
        document.getElementById('editDescripcion').value = incidencia.descripcion || '';
        
        let fechaISO = '';
        if (Array.isArray(incidencia.fechaLimite) && incidencia.fechaLimite.length >= 5) {
            const [y, m, d, h, min] = incidencia.fechaLimite;
            const pad = (num) => String(num).padStart(2, '0');
            fechaISO = `${y}-${pad(m)}-${pad(d)}T${pad(h)}:${pad(min)}`;
        } else if (typeof incidencia.fechaLimite === 'string') {
            fechaISO = incidencia.fechaLimite.substring(0, 16); 
        }
        
        document.getElementById('editFechaLimite').value = fechaISO;
        document.getElementById('editPrioridad').value = incidencia.prioridad;
        document.getElementById('editEstado').value = incidencia.estado;
        
        document.getElementById('editTrabajador').value = incidencia.trabajador || '';
        document.getElementById('editRol').value = incidencia.rol || '';
        document.getElementById('editWorkerInfo').textContent = `${incidencia.trabajador || 'N/A'} (${incidencia.rol || 'N/A'})`;


        const editModal = new bootstrap.Modal(document.getElementById('modalEditarIncidencia'));
        editModal.show();

    } catch (error) {
        console.error('Error al cargar la incidencia:', error);
        alert('Hubo un error al cargar los datos de la incidencia. Revisa la consola para detalles.');
    }
};