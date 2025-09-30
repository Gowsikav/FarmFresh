document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("productPriceForm");
    const productName = document.getElementById("productName");
    const price = document.getElementById("price");
    const productNameError = document.getElementById("productNameError");
    const priceError = document.getElementById("priceError");

    productName.addEventListener("input", () => {
        const name = productName.value.trim();

        if (name === "") {
            productNameError.innerText = "Product name is required.";
            return;
        }
        if (name.length < 3) {
            productNameError.innerText = "Product name must be at least 3 characters.";
            return;
        }

        fetch('/farm-fresh/checkProductName?productName=' + encodeURIComponent(name))
            .then(response => response.json())
            .then(data => {
                if (data=== true || data === "true") {
                    productNameError.innerText = "Product name already exists.";
                } else {
                    productNameError.innerText = "";
                }
            })
            .catch(error => {
                console.error("Error checking product name:", error);
            });
    });

    form?.addEventListener("submit", (e) => {
        let isValid = true;

        if (productName.value.trim() === "" || productName.value.trim().length < 3 || productNameError.innerText !== "") {
            isValid = false;
        }

        if (price.value.trim() === "" || parseFloat(price.value) <= 0) {
            priceError.innerText = "Enter a valid price greater than 0.";
            isValid = false;
        } else {
            priceError.innerText = "";
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
