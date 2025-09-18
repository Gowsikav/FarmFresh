document.addEventListener('DOMContentLoaded', function () {

    function loadProducts() {
        fetch("/farm-fresh/api/products")
            .then(res => res.json())
            .then(products => {
                const cardContainer = document.getElementById('product-list-container');
                cardContainer.innerHTML = "";

                products.forEach(p => {
                    cardContainer.innerHTML += `
                        <div class="col-md-3 col-sm-6 mb-4">
    <div class="card h-100 shadow-sm custom-card d-flex flex-column">
        <div class="ratio ratio-1x1">
            <img src="/farm-fresh/productImages/${p.image}" 
                 class="card-img-top rounded" 
                 alt="${p.name}" 
                 style="object-fit: contain; width: 100%; height: 100%; padding: 5px; background-color: #f8f9fa;">
        </div>
        <div class="card-body d-flex flex-column">
            <h5 class="card-title">${p.name}</h5>
            <p class="card-text">${p.description}</p>
            <p class="card-text mt-auto"><strong>Price:</strong> &#8377;${p.price}</p>
            <div class="mt-3 d-flex justify-content-between">
                <button class="btn btn-primary btn-sm" onclick="openEditModal(${p.id}, '${p.name}', '${p.description}', ${p.price})">
                    Edit
                </button>
                <button class="btn btn-danger btn-sm" onclick="deleteProduct(${p.id})">
                    Delete
                </button>
            </div>
        </div>
    </div>
                   `;
                });
            });
    }

    loadProducts();

    const form = document.getElementById('addProductForm');
    form.addEventListener('submit', function (e) {
        e.preventDefault();

        const fileInput = document.getElementById('imageFile').files[0];
        if (!fileInput) return alert("Select an image");

        const formData = new FormData();
        formData.append("name", document.getElementById('name').value);
        formData.append("description", document.getElementById('description').value);
        formData.append("price", document.getElementById('price').value);
        formData.append("imageFile", fileInput);

        fetch("/farm-fresh/api/products/upload", {
            method: "POST",
            body: formData
        }).then(res => res.text())
            .then(msg => {
                alert(msg);
                form.reset();
                loadProducts();
            });
    });

});

function deleteProduct(id) {
    if (!confirm("Are you sure?")) return;

    fetch(`/farm-fresh/api/products/${id}`, { method: "DELETE" })
        .then(res => res.text())
        .then(msg => {
            alert(msg);
            loadProducts();
        });
}

function openEditModal(id, name, description, price) {
    const modalHtml = `
        <div class="modal fade" id="editModal" tabindex="-1">
          <div class="modal-dialog">
            <form id="editForm" enctype="multipart/form-data">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title">Edit Product</h5>
                  <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                  <input type="hidden" name="id" value="${id}">
                  <div class="mb-3">
                    <label>Name</label>
                    <input type="text" name="name" class="form-control" value="${name}" required>
                  </div>
                  <div class="mb-3">
                    <label>Description</label>
                    <textarea name="description" class="form-control" required>${description}</textarea>
                  </div>
                  <div class="mb-3">
                    <label>Price</label>
                    <input type="number" name="price" class="form-control" value="${price}" required>
                  </div>
                  <div class="mb-3">
                    <label>Change Image</label>
                    <input type="file" name="imageFile" class="form-control">
                  </div>
                </div>
                <div class="modal-footer">
                  <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                  <button type="submit" class="btn btn-success">Save</button>
                </div>
              </div>
            </form>
          </div>
        </div>
    `;
    document.body.insertAdjacentHTML('beforeend', modalHtml);
    const modal = new bootstrap.Modal(document.getElementById('editModal'));
    modal.show();

    const editForm = document.getElementById('editForm');
    editForm.addEventListener('submit', function (e) {
        e.preventDefault();
        const formData = new FormData(editForm);

        fetch("/farm-fresh/api/products/update", {
            method: "POST",
            body: formData
        }).then(res => res.text())
            .then(msg => {
                alert(msg);
                modal.hide();
                document.getElementById('editModal').remove();
                loadProducts();
            });
    });
}
