const bankName = document.getElementById('bankName');
const branchName = document.getElementById('branchName');
const accountNumber = document.getElementById('accountNumber');
const confirmAccountNumber = document.getElementById('confirmAccountNumber');
const IFSCCode = document.getElementById('IFSCCode');
const accountType = document.getElementById('accountType');

const bankNameError = document.getElementById('bankNameError');
const branchNameError = document.getElementById('branchNameError');
const accountNumberError = document.getElementById('accountNumberError');
const confirmAccountNumberError = document.getElementById('confirmAccountNumberError');
const IFSCCodeError = document.getElementById('IFSCCodeError');
const accountTypeError = document.getElementById('accountTypeError');

// Bank Name validation
bankName.addEventListener('input', () => {
  const value = bankName.value.trim();

  if (value === '') {
    bankNameError.textContent = 'Bank name is required';
  } else if (value.length < 3) {
    bankNameError.textContent = 'Bank name should be at least 3 characters';
  } else {
    bankNameError.textContent = '';
  }
});

// Branch Name validation (optional)
branchName.addEventListener('input', () => {
  if(branchName.value.length > 0 && branchName.value.length < 3) {
    branchNameError.textContent = 'Branch name should be at least 3 characters';
  } else {
    branchNameError.textContent = '';
  }
});

// Account Number validation
accountNumber.addEventListener('input', () => {
  const accVal = accountNumber.value.trim();
  if (!/^\d{0,18}$/.test(accVal)) {
    accountNumberError.textContent = 'Only digits allowed (max 18)';
  } else if (accVal.length < 9 && accVal.length > 0) {
    accountNumberError.textContent = 'Account number should be at least 9 digits';
  } else {
    accountNumberError.textContent = '';
  }

  // Match confirm account number dynamically
  if (confirmAccountNumber.value && accVal !== confirmAccountNumber.value) {
    confirmAccountNumberError.textContent = 'Account numbers not matched';
  } else {
    confirmAccountNumberError.textContent = '';
  }
});

// Confirm Account Number validation
confirmAccountNumber.addEventListener('input', () => {
  if (confirmAccountNumber.value !== accountNumber.value) {
    confirmAccountNumberError.textContent = 'Account numbers not matched';
  } else {
    confirmAccountNumberError.textContent = '';
  }
});

// IFSC Code validation
IFSCCode.addEventListener('input', () => {
  const ifscPattern = /^[A-Z]{0,4}0?[A-Z0-9]{0,6}$/;
  if (!ifscPattern.test(IFSCCode.value.toUpperCase())) {
    IFSCCodeError.textContent = 'Invalid IFSC format';
  } else {
    IFSCCodeError.textContent = '';
  }
});

// Account Type validation
accountType.addEventListener('change', () => {
  accountTypeError.textContent = accountType.value === '' ? 'Please select account type' : '';
});

// Form submission validation
const form = document.getElementById('bankDetailsForm');
form.addEventListener('submit', function(event) {
  event.preventDefault();
  
  let valid = true;

  if (bankName.value.trim() === '') {
    bankNameError.textContent = 'Bank name is required';
    valid = false;
  }

  if (accountNumber.value.trim() === '' || accountNumber.value.length < 9 || accountNumber.value.length > 18) {
    accountNumberError.textContent = 'Account number must be 9-18 digits';
    valid = false;
  }

  if (accountNumber.value !== confirmAccountNumber.value) {
    confirmAccountNumberError.textContent = 'Account numbers do not match';
    valid = false;
  }

  const ifscPatternFull = /^[A-Z]{4}0[A-Z0-9]{6}$/;
  if (!ifscPatternFull.test(IFSCCode.value.toUpperCase())) {
    IFSCCodeError.textContent = 'Invalid IFSC code';
    valid = false;
  }

  if (accountType.value === '') {
    accountTypeError.textContent = 'Please select account type';
    valid = false;
  }

  if (!valid) {
    let button = document.getElementById('submitBtn');
    button.disabled = true;
    event.preventDefault();
  } else {
    form.submit();
  }
});

