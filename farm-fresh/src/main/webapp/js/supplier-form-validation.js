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

    // Load milk types dynamically from backend
fetch('/farm-fresh/productList')
    .then(response => response.json())
    .then(data => {
        milkSelect.innerHTML = '<option value="">Select milk type</option>';
        data.forEach(type => {
            const option = document.createElement("option");
            option.value = type;
            option.textContent = type;

            // Preselect supplier’s milk type if editing
            if (type === "${supplier.typeOfMilk}") {
                option.selected = true;
            }

            milkSelect.appendChild(option);
        });
    })
    .catch(() => {
        milkError.innerText = "Unable to load milk types";
        milkError.style.color = "orange";
    });

// Validate milk type after fetch
milkSelect.addEventListener("change", function() {
    if (milkSelect.value === "") {
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


const deleteModal = document.getElementById('deleteConfirmModal');
  const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');

  deleteModal.addEventListener('show.bs.modal', function (event) {
    const button = event.relatedTarget; // Button/link that opened the modal
    const deleteUrl = button.getAttribute('data-delete-url');
    confirmDeleteBtn.setAttribute('href', deleteUrl);
  });


function loadMilkTypes(selectElement, selectedValue = "") {
    fetch('/farm-fresh/productList')
        .then(response => response.json())
        .then(data => {
            selectElement.innerHTML = '<option value="">Select milk type</option>';
            data.forEach(type => {
                const option = document.createElement("option");
                option.value = type;
                option.textContent = type;

                // Preselect if matches existing value
                if (type === selectedValue) {
                    option.selected = true;
                }

                selectElement.appendChild(option);
            });
        })
        .catch(() => {
            const errorDiv = document.getElementById(
                selectElement.id === "typeOfMilk" ? "typeOfMilkError" : "editMilkError"
            );
            errorDiv.innerText = "Unable to load milk types";
            errorDiv.style.color = "orange";
        });
}

// For Add form (preselect from supplier if available)
loadMilkTypes(document.getElementById("typeOfMilk"), "${supplier.typeOfMilk}");

// For Edit form when modal opens
document.querySelectorAll(".editSupplierBtn").forEach(button => {
    button.addEventListener("click", function () {
        document.getElementById("editSupplierId").value = this.dataset.id;
        document.getElementById("editFirstName").value = this.dataset.firstname;
        document.getElementById("editLastName").value = this.dataset.lastname;
        document.getElementById("editEmail").value = this.dataset.email;
        document.getElementById("editPhone").value = this.dataset.phone;
        document.getElementById("editAddress").value = this.dataset.address;

        // Load milk types & preselect supplier’s existing type
        loadMilkTypes(document.getElementById("editMilk"), this.dataset.milk);
    });
});

// Place this script at the end of your HTML, before </body>
document.querySelectorAll('.viewSupplierBankBtn').forEach(function(btn) {
  btn.addEventListener('click', function() {
    document.getElementById('supplierName').textContent = btn.getAttribute('data-supplier-name') || '';
    document.getElementById('supplierEmail').textContent = btn.getAttribute('data-supplier-email') || '';
    document.getElementById('bankName').textContent = btn.getAttribute('data-bank-name') || '';
    document.getElementById('branch').textContent = btn.getAttribute('data-bank-branch') || '';
    document.getElementById('accountNumber').textContent = btn.getAttribute('data-account-number') || '';
    document.getElementById('ifscCode').textContent = btn.getAttribute('data-ifsc-code') || '';
    document.getElementById('accountType').textContent = btn.getAttribute('data-account-type') || '';
  });
});

document.querySelectorAll('.editSupplierBankBtn').forEach(function(btn) {
  btn.addEventListener('click', function() {
    document.getElementById('editBankSupplierId').value = btn.getAttribute('data-supplier-id') || '';
    document.getElementById('editBankSupplierName').value = btn.getAttribute('data-supplier-name') || '';
    document.getElementById('editBankSupplierEmail').value = btn.getAttribute('data-supplier-email') || '';
    document.getElementById('editBankName').value = btn.getAttribute('data-bank-name') || '';
    document.getElementById('editBankBranch').value = btn.getAttribute('data-bank-branch') || '';
    document.getElementById('editAccountNumber').value = btn.getAttribute('data-account-number') || '';
    document.getElementById('editIfscCode').value = btn.getAttribute('data-ifsc-code') || '';
    document.getElementById('editAccountType').value = btn.getAttribute('data-account-type') || '';
  });
});

// Edit Bank Details Form Validation
// Edit Bank Details Form Validation
document.getElementById('editBankDetailsForm').addEventListener('input', function (e) {
    let valid = true;

    // Clear previous errors
    document.getElementById('bankNameError').textContent = '';
    document.getElementById('bankBranchError').textContent = '';
    document.getElementById('accountNumberError').textContent = '';
    document.getElementById('ifscCodeError').textContent = '';
    document.getElementById('accountTypeError').textContent = '';

    // Bank Name
    const bankName = document.getElementById('editBankName').value.trim();
    if (!bankName) {
        document.getElementById('bankNameError').textContent = 'Bank name is required';
        valid = false;
    } else if (bankName.length < 3) {
        document.getElementById('bankNameError').textContent = 'Bank name should be at least 3 characters';
        valid = false;
    }

    // Branch
    const bankBranch = document.getElementById('editBankBranch').value.trim();
    if (!bankBranch) {
        document.getElementById('bankBranchError').textContent = 'Branch is required';
        valid = false;
    } else if (bankBranch.length < 3) {
        document.getElementById('bankBranchError').textContent = 'Branch name should be at least 3 characters';
        valid = false;
    }

    // Account Number
    const accountNumber = document.getElementById('editAccountNumber').value.trim();
    if (!/^\d{9,18}$/.test(accountNumber)) {
        document.getElementById('accountNumberError').textContent = 'Enter a valid account number (9-18 digits)';
        valid = false;
    }

    // IFSC Code
    const ifscCode = document.getElementById('editIfscCode').value.trim();
    if (!/^[A-Z]{4}0[A-Z0-9]{6}$/.test(ifscCode)) {
        document.getElementById('ifscCodeError').textContent = 'Invalid IFSC code format';
        valid = false;
    }

    // Account Type
    const accountType = document.getElementById('editAccountType').value;
    if (!accountType) {
        document.getElementById('accountTypeError').textContent = 'Please select an account type.';
        valid = false;
    }

    if (!valid) {
        e.preventDefault();
    }
});



