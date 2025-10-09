document.addEventListener('DOMContentLoaded', function() {
    function validateName(input, errorId, fieldName, minLen) {
        const value = input.value.trim();
        const errorSpan = document.getElementById(errorId);
        if (!value) {
            errorSpan.textContent = fieldName + ' is required.';
            return false;
        } else if (value.length < minLen) {
            errorSpan.textContent = fieldName + ' must be at least ' + minLen + ' characters.';
            return false;
        } else if (!/^[A-Za-z\s]+$/.test(value)) {
            errorSpan.textContent = 'Only letters and spaces allowed.';
            return false;
        } else {
            errorSpan.textContent = '';
            return true;
        }
    }

    function validateAddress(input, errorId, minLen) {
        const value = input.value.trim();
        const errorSpan = document.getElementById(errorId);
        if (!value) {
            errorSpan.textContent = 'Address is required.';
            return false;
        } else if (value.length < minLen) {
            errorSpan.textContent = 'Address must be at least ' + minLen + ' characters.';
            return false;
        } else {
            errorSpan.textContent = '';
            return true;
        }
    }

    const firstName = document.getElementById('firstName');
    const lastName = document.getElementById('lastName');
    const address = document.getElementById('address');

    firstName.addEventListener('input', function() {
        validateName(firstName, 'firstNameError', 'First Name', 3);
    });

    lastName.addEventListener('input', function() {
        validateName(lastName, 'lastNameError', 'Last Name', 1);
    });

    address.addEventListener('input', function() {
        validateAddress(address, 'addressError', 5);
    });

    document.querySelector('form').addEventListener('submit', function(e) {
        const validFirst = validateName(firstName, 'firstNameError', 'First Name', 3);
        const validLast = validateName(lastName, 'lastNameError', 'Last Name', 1);
        const validAddr = validateAddress(address, 'addressError', 6);
        if (!validFirst || !validLast || !validAddr) {
            e.preventDefault();
        }
    });
});
