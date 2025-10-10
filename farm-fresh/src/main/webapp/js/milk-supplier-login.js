document.addEventListener("DOMContentLoaded", function () {
  const emailInput = document.getElementById("supplierEmail");
  const emailError = document.getElementById("emailError");
  const loginBtn = document.getElementById("loginBtn");
  const otpModalEl = document.getElementById("otpModal");
  const otpModal = new bootstrap.Modal(otpModalEl, { backdrop: "static", keyboard: false });
  const otpTimerDisplay = document.getElementById("otpTimerDisplay");
  const otpForm = document.getElementById("otpForm");
  const otpInput = document.getElementById("otpInput");
  const resendBtn = document.getElementById("resendOtpBtn");

  const OTP_EXPIRY_SECONDS = 300; // 5 minutes
  let otpTimer;
  let timeLeft = OTP_EXPIRY_SECONDS;

  // ------------------ Timer ------------------
  function startOtpTimer() {
    otpInput.disabled = false;
    otpForm.querySelector('button[type="submit"]').disabled = false;
    resendBtn.disabled = true;

    const otpStartTimeStr = localStorage.getItem("otpStartTime");
    if (!otpStartTimeStr) {
      otpTimerDisplay.textContent = "OTP expired.";
      otpInput.disabled = true;
      otpForm.querySelector('button[type="submit"]').disabled = true;
      resendBtn.disabled = false;
      loginBtn.disabled = false;
      return;
    }

    const startTime = new Date(otpStartTimeStr);

    function updateTimer() {
      const now = new Date();
      const elapsedSeconds = Math.floor((now - startTime) / 1000);
      timeLeft = OTP_EXPIRY_SECONDS - elapsedSeconds;

      if (timeLeft <= 0) {
        clearInterval(otpTimer);
        otpTimerDisplay.textContent = "OTP expired.";
        otpInput.disabled = true;
        otpForm.querySelector('button[type="submit"]').disabled = true;
        loginBtn.disabled = false;
        resendBtn.disabled = false;
        localStorage.removeItem("otpStartTime");
      } else {
        const min = Math.floor(timeLeft / 60);
        const sec = timeLeft % 60;
        otpTimerDisplay.textContent = `OTP expires in ${min}:${sec.toString().padStart(2,"0")}`;
      }
    }

    updateTimer();
    otpTimer = setInterval(updateTimer, 1000);
  }

  // ------------------ OTP Sent ------------------
  function onOtpSent() {
    if (!localStorage.getItem("otpStartTime")) {
      localStorage.setItem("otpStartTime", new Date().toISOString());
    }
    otpModal.show();
    startOtpTimer();
  }

  // ------------------ Email Validation ------------------
  emailInput.addEventListener("blur", function () {
    const email = emailInput.value.trim();
    if (!email) {
      emailError.textContent = "";
      loginBtn.disabled = true;
      return;
    }

    emailError.textContent = "Checking email...";
    emailError.style.color = "black";
    loginBtn.disabled = true;

    fetch(`checkSupplierEmail?email=${encodeURIComponent(email)}`)
      .then(res => res.json())
      .then(result => {
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
        emailError.style.color = "red";
        loginBtn.disabled = true;
      });
  });

  // ------------------ Show OTP Modal ------------------
  function handleOtpModal() {
    const errorMessage = document.querySelector(".modal-body .alert-danger")?.textContent.trim();
    const successMessage = document.querySelector(".modal-body .alert-success")?.textContent.trim();
    const otpStartTimeExists = localStorage.getItem("otpStartTime");

    if (otpStartTimeExists) {
      otpModal.show();
      startOtpTimer();
    } else if (successMessage && !errorMessage) {
      onOtpSent();
    } else if (errorMessage) {
      otpModal.show();
      if (otpStartTimeExists) startOtpTimer();
      else {
        otpTimerDisplay.textContent = "OTP expired.";
        otpInput.disabled = true;
        otpForm.querySelector('button[type="submit"]').disabled = true;
        loginBtn.disabled = false;
      }
    }
  }

  handleOtpModal();

  // ------------------ OTP Form Submit ------------------
  // Do NOT prevent default; allow form to submit normally
  otpForm.addEventListener("submit", function () {
    // Timer continues automatically; remove start time only on success if you want
    // localStorage.removeItem("otpStartTime"); // optional
    // Do not show any alert
  });

  // ------------------ Resend OTP ------------------
  resendBtn.addEventListener("click", function () {
    localStorage.removeItem("otpStartTime");
    otpModal.hide();
    loginBtn.disabled = false;
  });

  window.onOtpSent = onOtpSent;
});
