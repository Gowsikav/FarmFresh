document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("collectMilkForm");
  const emailInput = document.getElementById("email");
  const nameInput = document.getElementById("name");
  const phoneInput = document.getElementById("phone");
  const typeSelect = document.getElementById("typeOfMilk");
  const priceInput = document.getElementById("price");
  const quantityInput = document.getElementById("quantity");
  const totalAmountInput = document.getElementById("totalAmount");
  const phoneError = document.getElementById("phoneError");
  const typeError = document.getElementById("typeOfMilkError");
  const button = document.querySelector('button[type="submit"]');

  // Helper: Disable button
  function setButtonState(enabled) {
    button.disabled = !enabled;
  }

  // Load milk types from backend
  function loadMilkTypes() {
    fetch("/farm-fresh/productList")
      .then((response) => response.json())
      .then((data) => {
        typeSelect.innerHTML = '<option value="">Select milk type</option>';
        data.forEach((type) => {
          const option = document.createElement("option");
          option.value = type;
          option.textContent = type;

          typeSelect.appendChild(option);
        });
        typeError.textContent = "";
      })
      .catch(() => {
        typeError.textContent = "Unable to load milk types";
        setButtonState(false);
      });
  }

  // On page load, load milk types (no preselect)
  loadMilkTypes();

  // Autofill by phone number
  phoneInput.addEventListener("blur", function () {
    const phone = phoneInput.value.trim();
    if (phone) {
      phoneError.textContent = "Checking phone number";
      setButtonState(false);
      fetch(`checkPhone?phoneNumber=${encodeURIComponent(phone)}`)
        .then((response) => response.json())
        .then((result) => {
          if (result === "true" || result === true) {
            phoneError.textContent = "Phone number found";
            phoneError.style.color = "green";
            fetch(`getSupplierByPhone?phone=${encodeURIComponent(phone)}`)
              .then((response) => (response.ok ? response.json() : null))
              .then((data) => {
                if (data && data.firstName && data.lastName && data.email) {
                  nameInput.value = data.firstName + " " + data.lastName;
                  emailInput.value = data.email;
                  // Do NOT set typeSelect.value here; user must select manually!
                  setButtonState(true);
                } else {
                  nameInput.value = "";
                  emailInput.value = "";
                  phoneError.textContent = "Supplier details incomplete.";
                  phoneError.style.color = "red";
                  setButtonState(false);
                }
              });
          } else {
            nameInput.value = "";
            emailInput.value = "";
            phoneError.textContent =
              "Phone number not found. Please register the supplier first.";
            phoneError.style.color = "red";
            setButtonState(false);
          }
        })
        .catch(() => {
          nameInput.value = "";
          emailInput.value = "";
          phoneError.textContent = "Error checking phone number.";
          phoneError.style.color = "red";
          setButtonState(false);
        });
    } else {
      nameInput.value = "";
      emailInput.value = "";
      typeSelect.value = "";
      phoneError.textContent = "";
      setButtonState(false);
    }
  });

  // When milk type changes, fetch price from backend
  typeSelect.addEventListener("change", function () {
    const milkType = typeSelect.value;
    if (milkType) {
      fetch(`/farm-fresh/getMilkPrice?type=${encodeURIComponent(milkType)}`)
        .then((response) => response.json())
        .then((data) => {
          if (data !== null) {
            priceInput.value = data.toFixed(2);
            typeError.textContent = "";
            setButtonState(true);
            // Trigger calculation if quantity already entered
            if (quantityInput.value) {
              totalAmountInput.value = (
                parseFloat(data) * parseFloat(quantityInput.value)
              ).toFixed(2);
            }
          } else {
            priceInput.value = "";
            totalAmountInput.value = "";
            typeError.textContent = "Price not found for selected milk type.";
            setButtonState(false);
          }
        })
        .catch(() => {
          priceInput.value = "";
          totalAmountInput.value = "";
          typeError.textContent = "Error fetching price.";
          setButtonState(false);
        });
    } else {
      priceInput.value = "";
      totalAmountInput.value = "";
      setButtonState(false);
    }
  });

  // Calculate total amount automatically
  quantityInput.addEventListener("input", function () {
    const price = parseFloat(priceInput.value);
    const qty = parseFloat(quantityInput.value);
    if (!isNaN(price) && !isNaN(qty)) {
      totalAmountInput.value = (price * qty).toFixed(2);
      setButtonState(true);
    } else {
      totalAmountInput.value = "";
      setButtonState(false);
    }
  });

  // On any error, disable button
  form.addEventListener("input", function () {
    const phoneHasError =
      phoneError.textContent && phoneError.style.color === "red";
    const typeHasError =
      typeError.textContent && typeError.style.color === "red";
    if (
      phoneHasError ||
      typeHasError ||
      !phoneInput.value.trim() ||
      !nameInput.value.trim() ||
      !emailInput.value.trim() ||
      !typeSelect.value ||
      !priceInput.value ||
      !quantityInput.value ||
      !totalAmountInput.value
    ) {
      setButtonState(false);
    } else {
      setButtonState(true);
    }
  });
});
