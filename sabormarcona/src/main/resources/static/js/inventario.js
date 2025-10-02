document.addEventListener('DOMContentLoaded', function () {
   let rowToEdit = null;
    let rowToDelete = null;

    // --- SELECTORES ---
    const productForm = document.getElementById('productForm');
    const inventoryTableBody = document.querySelector('#inventoryTable tbody');
    const totalInventoryEl = document.getElementById('totalInventory');

    // MODALES
    const editModalEl = document.getElementById('editProductModal');
    const editProductForm = document.getElementById('editProductForm');

    // --- FUNCIONES ---

    function showToast(title, message, type = 'success') {
        alert(`${title}: ${message}`);
    }

    function updateTotalInventory() {
        let total = 0;
        inventoryTableBody.querySelectorAll('tr').forEach(row => {
            const totalCell = row.querySelector('td:nth-child(4)');
            const totalValue = parseFloat(totalCell.textContent.replace('S/.', '').trim()) || 0;
            total += totalValue;
        });
        totalInventoryEl.textContent = `S/. ${total.toFixed(2)}`;
    }

    function clearForm() {
        productForm.reset();
    }

    function addProductToTable(product) {
        const row = inventoryTableBody.insertRow(0);
        row.innerHTML = `
            <td class="product-name">${product.name}</td>
            <td class="product-quantity">${product.quantity}</td>
            <td class="product-price">S/. ${parseFloat(product.price).toFixed(2)}</td>
            <td class="product-total">S/. ${(product.quantity * product.price).toFixed(2)}</td>
            <td>
                <button class="btn btn-sm btn-outline-primary edit-product-btn"><i class="fas fa-pencil-alt"></i></button>
                <button class="btn btn-sm btn-outline-danger delete-product-btn"><i class="fas fa-trash"></i></button>
            </td>
        `;
        updateTotalInventory();
    }

    // Agregar producto
    productForm.addEventListener('submit', function (e) {
        e.preventDefault();

        const name = document.getElementById('productName').value.trim();
        const quantity = parseInt(document.getElementById('productQuantity').value);
        const price = parseFloat(document.getElementById('productPrice').value);

        if (!name || quantity < 0 || price < 0) {
            showToast('Error', 'Datos inválidos', 'danger');
            return;
        }

        addProductToTable({ name, quantity, price });
        clearForm();
        showToast('Producto agregado', `${name} fue añadido al inventario.`, 'success');
    });

    // Editar o eliminar producto (delegación)
    inventoryTableBody.addEventListener('click', function (event) {
        const target = event.target;
        const row = target.closest('tr');

        if (target.closest('.edit-product-btn')) {
            rowToEdit = row;
            const name = row.querySelector('.product-name').textContent;
            const quantity = row.querySelector('.product-quantity').textContent;
            const price = row.querySelector('.product-price').textContent.replace('S/.', '').trim();

            document.getElementById('editProductName').value = name;
            document.getElementById('editProductQuantity').value = quantity;
            document.getElementById('editProductPrice').value = price;

            const modal = new bootstrap.Modal(editModalEl);
            modal.show();
        }

        if (target.closest('.delete-product-btn')) {
            if (confirm('¿Estás seguro de que deseas eliminar este producto?')) {
                row.remove();
                updateTotalInventory();
                showToast('Eliminado', 'Producto eliminado del inventario.', 'info');
            }
        }
    });

    // Guardar cambios en edición
    editProductForm.addEventListener('submit', function (e) {
        e.preventDefault();

        const name = document.getElementById('editProductName').value.trim();
        const quantity = parseInt(document.getElementById('editProductQuantity').value);
        const price = parseFloat(document.getElementById('editProductPrice').value);

        if (!name || quantity < 0 || price < 0) {
            showToast('Error', 'Datos inválidos', 'danger');
            return;
        }

        if (rowToEdit) {
            rowToEdit.querySelector('.product-name').textContent = name;
            rowToEdit.querySelector('.product-quantity').textContent = quantity;
            rowToEdit.querySelector('.product-price').textContent = `S/. ${price.toFixed(2)}`;
            rowToEdit.querySelector('.product-total').textContent = `S/. ${(quantity * price).toFixed(2)}`;
        }

        const modal = bootstrap.Modal.getInstance(editModalEl);
        modal.hide();
        updateTotalInventory();
        showToast('Editado', 'Producto actualizado correctamente.', 'success');
    });
});
