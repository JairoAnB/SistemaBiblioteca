document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById('form-modal');
    const createButton = document.getElementById('create-button');
    const closeButton = document.getElementById('modal-close-button');
    const cancelButton = document.getElementById('modal-cancel-button');
    const modalTitle = document.getElementById('modal-title');
    const itemForm = document.getElementById('item-form'); // El formulario dentro del modal
    const itemIdField = document.querySelector('#item-form input[type="hidden"]'); // Campo oculto para ID

    // Función para abrir el modal
    const openModal = (title = 'Nuevo Elemento', data = null) => {
        modalTitle.textContent = title; // Establecer título del modal
        if (itemIdField) itemIdField.value = ''; // Limpiar ID oculto
        if (data && itemIdField) {
            // Si se proporcionan datos (para editar), llenar el formulario
            // ¡Esta parte necesitará lógica específica cuando conectes la API!
            // Por ahora, solo ponemos el ID si existe el campo
            itemIdField.value = data.id || '';
            // Aquí iría la lógica para llenar los otros campos del formulario con 'data'
            console.log("Abriendo modal para editar:", data);
            // Ejemplo (requiere adaptar los nombres de campo del form):
            // document.getElementById('book-title').value = data.title || '';
            // document.getElementById('book-author').value = data.author || '';
            // ...etcétera para todos los campos
        } else {
            console.log("Abriendo modal para crear nuevo item");
        }

        if (modal) {
            modal.classList.add('modal-visible');
        }
    };

    // Función para cerrar el modal
    const closeModal = () => {
        if (modal) {
            modal.classList.remove('modal-visible');
        }
    };

    // --- Event Listeners ---

    // Abrir modal al hacer clic en "Nuevo X"
    if (createButton) {
        createButton.addEventListener('click', () => {
            // Determinar el título basado en el H1 de la página actual
            const pageTitle = document.querySelector('h1')?.textContent || '';
            let modalBaseTitle = 'Nuevo Elemento';
            if (pageTitle.includes('Libros')) modalBaseTitle = 'Nuevo Libro';
            else if (pageTitle.includes('Usuarios')) modalBaseTitle = 'Nuevo Usuario';
            else if (pageTitle.includes('Préstamos')) modalBaseTitle = 'Nuevo Préstamo';
            openModal(modalBaseTitle);
        });
    }

    // Cerrar modal con el botón X
    if (closeButton) {
        closeButton.addEventListener('click', closeModal);
    }

    // Cerrar modal con el botón Cancelar
    if (cancelButton) {
        cancelButton.addEventListener('click', closeModal);
    }

    // Cerrar modal si se hace clic fuera del contenido del modal
    if (modal) {
        modal.addEventListener('click', (event) => {
            // Si el clic fue directamente sobre el fondo oscuro del modal
            if (event.target === modal) {
                closeModal();
            }
        });
    }

    // Escuchar clics en la tabla para botones de Editar/Eliminar (futuro)
    const dataTable = document.getElementById('data-table');
    if (dataTable) {
        dataTable.addEventListener('click', (event) => {
            const target = event.target;

            // Si se hizo clic en un botón de editar
            if (target.classList.contains('btn-edit')) {
                event.preventDefault(); // Evitar comportamiento por defecto si es un enlace/botón
                const itemId = target.dataset.id; // Obtener el ID del atributo data-id
                console.log(`Editar item con ID: ${itemId}`);

                // --- Lógica futura para obtener datos del item ---
                // Aquí harías una llamada a tu API para obtener los datos del item con 'itemId'
                // const itemData = await fetchItemData(itemId); // (Función ficticia)

                // Datos de ejemplo para simular la edición:
                const exampleData = { id: itemId, /* ...otros campos simulados */ };
                let modalEditTitle = 'Editar Elemento';
                const pageTitle = document.querySelector('h1')?.textContent || '';
                if (pageTitle.includes('Libros')) modalEditTitle = 'Editar Libro';
                else if (pageTitle.includes('Usuarios')) modalEditTitle = 'Editar Usuario';
                else if (pageTitle.includes('Préstamos')) modalEditTitle = 'Editar Préstamo';

                // Abre el modal en modo edición
                openModal(modalEditTitle, exampleData);
            }

            // Si se hizo clic en un botón de eliminar
            if (target.classList.contains('btn-delete')) {
                event.preventDefault();
                const itemId = target.dataset.id;
                console.log(`Eliminar item con ID: ${itemId}`);
                // --- Lógica futura para confirmar y eliminar ---
                // if (confirm('¿Estás seguro de que deseas eliminar este elemento?')) {
                //     await deleteItem(itemId); // (Función ficticia para llamar a la API)
                //     // Luego, recargar la tabla o eliminar la fila visualmente
                // }
            }
        });
    }


});