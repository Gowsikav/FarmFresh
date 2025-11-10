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

  // === QR Scanner buttons ===
  const startScanBtn = document.getElementById("startScanBtn");
  const stopScanBtn = document.getElementById("stopScanBtn");
  const qrScannerContainer = document.getElementById("qrScannerContainer");

  let html5QrCode;

  function setButtonState(enabled) {
    button.disabled = !enabled;
  }

  // Load milk types
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

  loadMilkTypes();

  function fetchSupplierByPhone(phone) {
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
    }
  }

  phoneInput.addEventListener("blur", () => {
    fetchSupplierByPhone(phoneInput.value.trim());
  });

  // === Milk type selection ===
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

  // === Total amount auto calc ===
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

  // === QR Scanner logic ===
  startScanBtn.addEventListener("click", () => {
    qrScannerContainer.style.display = "block";
    stopScanBtn.style.display = "inline-block";
    startScanBtn.style.display = "none";

    qrScannerContainer.innerHTML = `
      <div id="reader" style="width:100%; border:1px solid #ccc; border-radius:8px;"></div>
      <button type="button" class="btn btn-danger btn-sm mt-2" id="stopScanBtn">Stop Scanning</button>
    `;

    const stopScanBtnDynamic = document.getElementById("stopScanBtn");
    html5QrCode = new Html5Qrcode("reader");

    html5QrCode
      .start(
        { facingMode: "environment" },
        { fps: 10, qrbox: 250 },
        (decodedText) => {
          console.log("Scanned:", decodedText);

          html5QrCode.stop();

          qrScannerContainer.style.display = "none";
          startScanBtn.style.display = "inline-block";

          const match = decodedText.match(/ID:(\d+);EMAIL:([^;]+);PHONE:(\d+)/);
          if (match) {
            const supplierId = match[1];
            const supplierEmail = match[2];
            const supplierPhone = match[3];

            document.getElementById("qrId").textContent = supplierId;
            document.getElementById("qrEmail").textContent = supplierEmail;
            document.getElementById("qrPhone").textContent = supplierPhone;

            const qrModal = new bootstrap.Modal(document.getElementById("qrResultModal"));
            qrModal.show();

            document.getElementById("fillFormBtn").onclick = function () {
              phoneInput.value = supplierPhone;
              emailInput.value = supplierEmail;
              fetchSupplierByPhone(supplierPhone);
            };
          } else {
            alert("Invalid QR format!");
          }
        },
        (errorMessage) => {
          console.warn("QR error:", errorMessage);
        }
      )
      .catch((err) => {
        console.error("Camera start failed:", err);
        alert("Camera access failed. Check permissions!");
      });

    stopScanBtnDynamic.addEventListener("click", () => {
      html5QrCode.stop().then(() => {
        qrScannerContainer.style.display = "none";
        startScanBtn.style.display = "inline-block";
      });
    });
  });
});

