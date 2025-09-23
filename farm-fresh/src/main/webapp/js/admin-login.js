document.addEventListener("DOMContentLoaded", function() {
        const errorDiv = document.getElementById("errorMsg");
        const loginBtn = document.getElementById("loginBtn");

        if (errorDiv && errorDiv.innerText.includes("Account is blocked")) {
            loginBtn.disabled = true;
            loginBtn.innerText = "Account Blocked";
            loginBtn.classList.remove("btn-success");
            loginBtn.classList.add("btn-secondary");
        }
    });


    document.addEventListener("DOMContentLoaded", function() {
    const emailInput = document.getElementById("resetEmail");
    const sendBtn = document.getElementById("sendBtn");
    const emailFeedback = document.getElementById("emailFeedback");

    emailInput.addEventListener("input", function() {
        const email = emailInput.value.trim();
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!emailPattern.test(email)) {
            sendBtn.disabled = true;
            emailFeedback.classList.remove("d-none");
            emailFeedback.innerText = "Enter a valid email address.";
            return;
        }

        fetch('/farm-fresh/checkEmail?email=' + encodeURIComponent(email),{method:"GET"})
            .then(response => response.text())
            .then(text => (text === "true"))
            .then(exists => {
            
                if (exists === "true" || exists === true) {
                    sendBtn.disabled = false;
                    emailFeedback.classList.remove("d-none");
                    emailFeedback.innerText = "Email found. You can proceed.";
                    emailFeedback.style.color = "green";
                } else {
                    sendBtn.disabled = true;
                    emailFeedback.classList.remove("d-none");
                    emailFeedback.innerText = "Email not found. Please check your email address.";
                }
            })
            .catch(error => console.error('Error:', error));
    });
});
