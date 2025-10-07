document.addEventListener("DOMContentLoaded", function () {
  document.querySelectorAll(".view-details-btn").forEach(function (btn) {
    btn.addEventListener("click", function (e) {
      // No need to preventDefault, since modal is triggered by Bootstrap
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
      // Modal will show automatically via Bootstrap's data-bs-toggle
    });
  });
});

