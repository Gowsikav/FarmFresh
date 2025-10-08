document.addEventListener("DOMContentLoaded", function () {
  const emailInput = document.getElementById("supplierEmail");
  const emailError = document.getElementById("emailError");
  const loginBtn = document.getElementById("loginBtn");
  const loginForm = document.getElementById("milkSupplierLoginForm");
  const otpModalEl = document.getElementById("otpModal");
  const otpModal = new bootstrap.Modal(otpModalEl, {
    backdrop: "static",
    keyboard: false,
  });
  const otpTimerDisplay = document.getElementById("otpTimerDisplay");
  const otpForm = document.getElementById("otpForm");
  const otpInput = document.getElementById("otpInput");
  const resendBtn = document.getElementById("resendOtpBtn");

  let otpTimer;
  const OTP_EXPIRY_SECONDS = 300; // 5 minutes
  let timeLeft = OTP_EXPIRY_SECONDS;

  function startOtpTimer() {
    resendBtn.disabled = true;
    otpInput.disabled = false;
    otpForm.querySelector('button[type="submit"]').disabled = false;

    const otpStartTime = localStorage.getItem("otpStartTime");
    let startTime = otpStartTime ? new Date(otpStartTime) : new Date();

    // If OTP start time not saved yet, set now
    if (!otpStartTime) {
      localStorage.setItem("otpStartTime", startTime.toISOString());
    }

    function updateTimer() {
      const now = new Date();
      const elapsedSeconds = Math.floor((now - startTime) / 1000);
      timeLeft = OTP_EXPIRY_SECONDS - elapsedSeconds;

      if (timeLeft <= 0) {
        clearInterval(otpTimer);
        otpTimerDisplay.textContent = "OTP expired.";
        otpInput.disabled = true;
        otpForm.querySelector('button[type="submit"]').disabled = true;
        resendBtn.disabled = false;
        localStorage.removeItem("otpStartTime");
      } else {
        updateTimerDisplay();
      }
    }

    updateTimer();
    otpTimer = setInterval(updateTimer, 1000);
  }

  function updateTimerDisplay() {
    let min = Math.floor(timeLeft / 60);
    let sec = timeLeft % 60;
    otpTimerDisplay.textContent = `OTP expires in ${min}:${sec
      .toString()
      .padStart(2, "0")}`;
  }

  resendBtn.addEventListener("click", function () {
    localStorage.removeItem("otpStartTime");
    otpModal.hide();
    window.location.href = "redirectToMilkSupplierLogin?email=" + encodeURIComponent(emailInput.value.trim());
  });

  emailInput.addEventListener("input", function () {
    const email = emailInput.value.trim();
    if (email) {
      emailError.textContent = "Checking email...";
      loginBtn.disabled = true;
      fetch(`checkSupplierEmail?email=${encodeURIComponent(email)}`)
        .then((response) => response.json())
        .then((result) => {
          if (result === true || result === "true") {
            emailError.textContent = "Email found. You can proceed.";
            emailError.style.color = "green";
            loginBtn.disabled = false;
          } else {
            emailError.textContent = "Email not found. Please register first.";
            emailError.style.color = "red";
            loginBtn.disabled = true;
          }
        })
        .catch(() => {
          emailError.textContent = "Error checking email.";
          loginBtn.disabled = true;
        });
    } else {
      emailError.textContent = "";
      loginBtn.disabled = true;
    }
  });

  // Show modal and continue timer if OTP exists
  const errorMessage = document
    .querySelector(".modal-body .alert-danger")
    ?.textContent.trim();
  const successMessage = document
    .querySelector(".modal-body .alert-success")
    ?.textContent.trim();

  if (
    (successMessage && successMessage !== "") ||
    (errorMessage && errorMessage !== "")
  ) {
    otpModal.show();
    startOtpTimer();
  }

  otpForm.addEventListener("submit", function (e) {
    otpModal.hide();
  });
});
