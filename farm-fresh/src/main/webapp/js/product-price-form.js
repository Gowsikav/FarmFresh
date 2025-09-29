document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("productPriceForm");
    const productName = document.getElementById("productName");
    const price = document.getElementById("price");

    form?.addEventListener("submit", (e) => {
        let isValid = true;

        // Validate Product Name
        if (productName.value.trim() === "") {
            document.getElementById("productNameError").innerText = "Product name is required.";
            isValid = false;
        } else {
            document.getElementById("productNameError").innerText = "";
        }

        // Validate Price
        if (price.value.trim() === "" || parseFloat(price.value) <= 0) {
            document.getElementById("priceError").innerText = "Enter a valid price greater than 0.";
            isValid = false;
        } else {
            document.getElementById("priceError").innerText = "";
        }

        if (!isValid) e.preventDefault();
    });

    // Edit Modal Prefill
    const editButtons = document.querySelectorAll(".editProductBtn");
    editButtons.forEach(btn => {
        btn.addEventListener("click", () => {
            document.getElementById("editProductId").value = btn.dataset.id;
            document.getElementById("editProductName").value = btn.dataset.name;
            document.getElementById("editProductPrice").value = btn.dataset.price;
            document.getElementById("editProductType").value = btn.dataset.type;
        });
    });

    // Delete Confirmation
    const deleteModal = document.getElementById("deleteConfirmModal");
    deleteModal?.addEventListener("show.bs.modal", (event) => {
        const button = event.relatedTarget;
        const deleteUrl = button.getAttribute("data-delete-url");
        document.getElementById("confirmDeleteBtn").setAttribute("href", deleteUrl);
    });
});
