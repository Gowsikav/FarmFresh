document.addEventListener("DOMContentLoaded", function () {
    const password = document.getElementById("password");
    const confirmPassword = document.getElementById("confirmpassword");
    const feedback = document.getElementById("confirmFeedback");
    const submitBtn = document.getElementById("loginBtn");

    function validatePasswords() {
      if (confirmPassword.value.trim() === "") {
        submitBtn.disabled = true;
        feedback.classList.add("d-none");
        return;
      }

      if (password.value === confirmPassword.value) {
        submitBtn.disabled = false;
        feedback.classList.add("d-none");
        confirmPassword.classList.remove("is-invalid");
        confirmPassword.classList.add("is-valid");
      } else {
        submitBtn.disabled = true;
        feedback.classList.remove("d-none");
        confirmPassword.classList.remove("is-valid");
        confirmPassword.classList.add("is-invalid");
      }
    }

    password.addEventListener("input", validatePasswords);
    confirmPassword.addEventListener("input", validatePasswords);
  });