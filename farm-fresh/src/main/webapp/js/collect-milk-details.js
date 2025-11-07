document.addEventListener("DOMContentLoaded", function () {
  document.querySelectorAll(".view-details-btn").forEach(function (btn) {
    btn.addEventListener("click", function (e) {
      let html = `
                <h5 class="mb-3">Supplier Details</h5>
                <ul class="list-group mb-3">
                    <li class="list-group-item"><strong>Name:</strong> ${btn.getAttribute(
                      "data-name"
                    )}</li>
                    <li class="list-group-item"><strong>Email:</strong> ${btn.getAttribute(
                      "data-email"
                    )}</li>
                    <li class="list-group-item"><strong>Phone Number:</strong> ${btn.getAttribute(
                      "data-phone"
                    )}</li>
                    <li class="list-group-item"><strong>Address:</strong> ${btn.getAttribute(
                      "data-address"
                    )}</li>
                    <li class="list-group-item"><strong>Type of Milk:</strong> ${btn.getAttribute(
                      "data-milk"
                    )}</li>
                </ul>
                <h5 class="mb-3">Milk Collection Details</h5>
                <ul class="list-group">
                <li class="list-group-item"><strong>Type of Milk:</strong> ${btn.getAttribute(
                      "data-typeMilk"
                    )}</li>
                    <li class="list-group-item"><strong>Quantity:</strong> ${btn.getAttribute(
                      "data-quantity"
                    )} litres</li>
                    <li class="list-group-item"><strong>Price (per litre):</strong> ${btn.getAttribute(
                      "data-price"
                    )}</li>
                    <li class="list-group-item"><strong>Total Amount:</strong> ${btn.getAttribute(
                      "data-total"
                    )}</li>
                    <li class="list-group-item"><strong>Date:</strong> ${btn.getAttribute(
                      "data-date"
                    )}</li>
                </ul>
            `;
      document.getElementById("milkDetailsModalBody").innerHTML = html;
    });
  });
  const today = new Date().toISOString().split("T")[0];
      document.getElementById("fromSearchDate").setAttribute("max", today);
      document.getElementById("toSearchDate").setAttribute("max", today);
});

function validateDates() {
    const fromDate = document.getElementById("fromSearchDate").value.trim();
    const toDate = document.getElementById("toSearchDate").value.trim();
    const errorMsg = document.getElementById("dateError");
    const datePattern = /^\d{4}-\d{2}-\d{2}$/;

    errorMsg.style.display = "none";
    errorMsg.textContent = "";

    if (!datePattern.test(fromDate) || !datePattern.test(toDate)) {
        errorMsg.textContent = "Please enter valid dates in YYYY-MM-DD format.";
        errorMsg.style.display = "block";
        return false;
    }

    if (new Date(fromDate) > new Date(toDate)) {
        errorMsg.textContent = "'From' date cannot be later than 'To' date.";
        errorMsg.style.display = "block";
        return false;
    }
     if (fromDate > today || toDate > today) {
            errorMsg.textContent = "Future dates are not allowed.";
            errorMsg.style.display = "block";
            return false;
        }

    return true;
}

