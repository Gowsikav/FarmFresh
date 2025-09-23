document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById("supplierForm");
    const submitButton = document.getElementById("submitButton");

    const firstNameInput = document.getElementById("firstName");
    const lastNameInput = document.getElementById("lastName");
    const emailInput = document.getElementById("email");
    const phoneInput = document.getElementById("phoneNumber");
    const addressInput = document.getElementById("address");
    const milkSelect = document.getElementById("typeOfMilk");

    const firstNameError = document.getElementById("firstNameError");
    const lastNameError = document.getElementById("lastNameError");
    const emailError = document.getElementById("emailError");
    const phoneError = document.getElementById("phoneNumberError");
    const addressError = document.getElementById("addressError");
    const milkError = document.getElementById("typeOfMilkError");

    let emailAvailable = false;
    let phoneAvailable = false;

    // Validate first name
    firstNameInput.addEventListener("input", function() {
        if (firstNameInput.value.trim().length < 3) {
            firstNameError.innerText = "First name must be at least 3 characters.";
            firstNameError.style.color = "red";
        } else {
            firstNameError.innerText = "";
        }
        updateSubmitButton();
    });

    // Validate last name
    lastNameInput.addEventListener("input", function() {
        if (lastNameInput.value.trim().length < 1) {
            lastNameError.innerText = "Last name must be at least 1 characters.";
            lastNameError.style.color = "red";
        } else {
            lastNameError.innerText = "";
        }
        updateSubmitButton();
    });

    // Validate email and check backend
    // Email availability check
emailInput.addEventListener("input", function() {
    const email = emailInput.value.trim();
    if (!email) {
        emailError.innerText = "Email is required";
        emailError.style.color = "red";
        emailAvailable = false;
        updateSubmitButton();
        return;
    }
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        emailError.innerText = "Enter a valid email";
        emailError.style.color = "red";
        emailAvailable = false;
        updateSubmitButton();
        return;
    }

    fetch('/farm-fresh/checkSupplierEmail?email=' + encodeURIComponent(email))
        .then(response => response.json())
        .then(isTaken => {
            const taken = (typeof isTaken === "string") ? (isTaken === "true") : isTaken;
            if (taken) {
                emailAvailable = false;
                emailError.innerText = " Email already exists";
                emailError.style.color = "red";
            } else {
                emailAvailable = true;
                emailError.innerText = "";
            }
            updateSubmitButton();
        })
        .catch(() => {
            emailAvailable = false;
            emailError.innerText = "Unable to check email now";
            emailError.style.color = "orange";
            updateSubmitButton();
        });
});

// Phone availability check
phoneInput.addEventListener("input", function() {
    const phone = phoneInput.value.trim();
    if (!phone) {
        phoneError.innerText = "Phone number is required";
        phoneError.style.color = "red";
        phoneAvailable = false;
        updateSubmitButton();
        return;
    }
    if (!/^[6-9]\d{9}$/.test(phone)) {
        phoneError.innerText = "Phone must be 10 digits and start with 6-9";
        phoneError.style.color = "red";
        phoneAvailable = false;
        updateSubmitButton();
        return;
    }

    fetch('/farm-fresh/checkPhone?phoneNumber=' + encodeURIComponent(phone))
        .then(response => response.json())
        .then(isTaken => {
            const taken = (typeof isTaken === "string") ? (isTaken === "true") : isTaken;
            if (taken) {
                phoneAvailable = false;
                phoneError.innerText = "Phone number already exists";
                phoneError.style.color = "red";
            } else {
                phoneAvailable = true;
                phoneError.innerText = "";
                
            }
            updateSubmitButton();
        })
        .catch(() => {
            phoneAvailable = false;
            phoneError.innerText = "Unable to check phone now";
            phoneError.style.color = "orange";
            updateSubmitButton();
        });
});


    // Validate address
    addressInput.addEventListener("input", function() {
        if (addressInput.value.trim().length < 5) {
            addressError.innerText = "Address must be at least 5 characters.";
            addressError.style.color = "red";
        } else {
            addressError.innerText = "";
        }
        updateSubmitButton();
    });

    // Validate milk type
    milkSelect.addEventListener("input", function() {
        if (milkSelect.value === "Select milk type" || milkSelect.value === "") {
            milkError.innerText = "Please select milk type.";
            milkError.style.color = "red";
        } else {
            milkError.innerText = "";
        }
        updateSubmitButton();
    });

    // Enable/disable submit button based on all checks
    function updateSubmitButton() {
        const isValid = 
            firstNameInput.value.trim().length >= 3 &&
            lastNameInput.value.trim().length >= 1 &&
            /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(emailInput.value.trim()) && emailAvailable &&
            /^[6-9]\d{9}$/.test(phoneInput.value.trim()) && phoneAvailable &&
            addressInput.value.trim().length >= 5 &&
            milkSelect.value !== "Select milk type" && milkSelect.value !== "";

        submitButton.disabled = !isValid;
    }

    // Initial check
    updateSubmitButton();
});

document.querySelectorAll(".viewSupplierBtn").forEach(button => {
    button.addEventListener("click", function () {
        document.getElementById("modalFirstName").innerText = this.dataset.firstname;
        document.getElementById("modalLastName").innerText = this.dataset.lastname;
        document.getElementById("modalEmail").innerText = this.dataset.email;
        document.getElementById("modalPhone").innerText = this.dataset.phone;
        document.getElementById("modalAddress").innerText = this.dataset.address;
        document.getElementById("modalMilk").innerText = this.dataset.milk;
    });
});

document.querySelectorAll(".editSupplierBtn").forEach(button => {
    button.addEventListener("click", function () {
        document.getElementById("editSupplierId").value = this.dataset.id;
        document.getElementById("editFirstName").value = this.dataset.firstname;
        document.getElementById("editLastName").value = this.dataset.lastname;
        document.getElementById("editEmail").value = this.dataset.email;
        document.getElementById("editPhone").value = this.dataset.phone;
        document.getElementById("editAddress").value = this.dataset.address;
        document.getElementById("editMilk").value = this.dataset.milk;
    });
});

const deleteModal = document.getElementById('deleteConfirmModal');
  const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');

  deleteModal.addEventListener('show.bs.modal', function (event) {
    const button = event.relatedTarget; // Button/link that opened the modal
    const deleteUrl = button.getAttribute('data-delete-url');
    confirmDeleteBtn.setAttribute('href', deleteUrl);
  });
