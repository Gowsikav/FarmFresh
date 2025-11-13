<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/includes/sessionCheck.jspf" %>

<html lang="en">

<head>
    <meta charset="UTF-8" />
    <title>Farm Fresh | Update Supplier Bank Details</title>
    <link rel="shortcut icon" href="images/title-pic.png" type="image/x-icon" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet"
        crossorigin="anonymous" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
    <link rel="stylesheet" href="css/index.css" />
</head>

<body class="d-flex flex-column min-vh-100">
    <nav class="navbar navbar-expand-lg fixed-top" style="background: linear-gradient(90deg, #388e3c, #e8f5e9)">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">
                <img src="images/farm-fresh-logo.png" alt="Farm Fresh Logo" height="60" width="60"
                    class="rounded-circle border border-light p-1 ms-3 me-2" />
            </a>
             <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false"
                aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav ms-auto me-3">
                    <li class="nav-item">
                        <a class="nav-link active" href="redirectToSupplierDashboard"><i
                                class="fa-solid fa-user me-2"></i>Dashboard</a>
                    </li>

                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="profileDropdown" role="button"
                            data-bs-toggle="dropdown" aria-expanded="false">
                            <c:choose>
                                <c:when test="${empty dto.profilePath}">
                                    <img src="images/dummy-profile.png" alt="Profile" class="rounded-circle" width="40"
                                        height="40" style="object-fit: cover;">
                                </c:when>
                                <c:otherwise>
                                    <img src="<c:url value='/uploads/${dto.profilePath}'/>" alt="Profile"
                                        class="rounded-circle" width="40" height="40" style="object-fit: cover;">
                                </c:otherwise>
                            </c:choose>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="profileDropdown">
                            <li><a class="dropdown-item text-danger" href="supplierLogout"><i
                                        class="fa-solid fa-right-from-bracket me-2"></i>Logout</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <main class="flex-grow-2 d-flex" style="margin-top: 80px; ">
        <div class="container-fluid farm-bg2 d-flex flex-column justify-content-md-start align-items-center w-100 pt-5">
            <h2 class="mb-4" style="color: #fffde7;"><i class="fa-solid fa-building-columns me-2"></i>Supplier Bank
                Details</h2>

            <form action="updateBankDetails" method="post" id="bankDetailsForm"
                class="bg-white bg-opacity-75 p-4 rounded shadow-sm" style="max-width: 600px; width: 60%; color: #333;">
                <!-- Bank Name -->
                <input type="email" name="email" value="${dto.email}" hidden />
                <div class="mb-3">
                    <label for="bankName" class="form-label">Bank Name <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" id="bankName" name="bankName" value="${bank.bankName}"
                        placeholder="Enter Bank Name" required>
                    <span class="form-text text-danger" id="bankNameError"></span>
                </div>

                <!-- Branch Name -->
                <div class="mb-3">
                    <label for="branchName" class="form-label">Branch Name <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" id="branchName" name="bankBranch" value="${bank.bankBranch}"
                        placeholder="Enter Branch Name">
                    <span class="form-text text-danger" id="branchNameError"></span>
                </div>

                <!-- Account Number -->
                <div class="mb-3">
                    <label for="accountNumber" class="form-label">Account Number <span
                            class="text-danger">*</span></label>
                    <input type="number" class="form-control" id="accountNumber" name="accountNumber"
                        value="${bank.accountNumber}" placeholder="Enter Account Number" required pattern="\d{9,18}">
                    <div class="form-text">Account number should be 9-18 digits.</div>
                    <span class="form-text text-danger" id="accountNumberError"></span>
                </div>

                <!-- Confirm Account Number -->
                <div class="mb-3">
                    <label for="confirmAccountNumber" class="form-label">Confirm Account Number <span
                            class="text-danger">*</span></label>
                    <input type="text" class="form-control" id="confirmAccountNumber"
                        placeholder="Confirm Account Number" required>
                    <span class="form-text text-danger" id="confirmAccountNumberError"></span>
                </div>

                <!-- IFSC Code -->
                <div class="mb-3">
                    <label for="IFSCCode" class="form-label">IFSC Code <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" id="IFSCCode" name="IFSCCode" value="${bank.IFSCCode}"
                        placeholder="Enter IFSC Code" required pattern="^[A-Z]{4}0[A-Z0-9]{6}$">
                    <div class="form-text">Format: 4 capital letters + 0 + 6 alphanumeric characters.</div>
                    <span class="form-text text-danger" id="IFSCCodeError"></span>
                </div>

                <!-- Account Type -->
                <div class="mb-3">
                    <label for="accountType" class="form-label">Account Type</label>
                    <select class="form-select" id="accountType" name="accountType" required>
                        <option value="">Select Account Type</option>
                        <option value="Savings">Savings</option>
                        <option value="Current">Current</option>
                        <option value="Salary">Salary</option>
                    </select>
                    <span class="form-text text-danger" id="accountTypeError"></span>
                </div>

                <button id="submitBtn" type="submit" class="btn btn-primary">Save Bank Details</button>
            </form>
        </div>
    </main>


    <footer class="text-lg-start py-3" style="background: linear-gradient(90deg, #1b5e20, #fffde7); color: #333">
        <div class="container">
            <div class="row text-dark align-items-start text-center text-md-start">
                <div class="col-md-3 mb-3">
                    <div class="d-flex align-items-center justify-content-center justify-content-md-start">
                        <span class="me-2" style="font-size: 1.5rem">
                            <i class="fa-solid fa-location-dot"></i>
                        </span>
                        <div>
                            <strong>Address:</strong><br />
                            123 Green Valley, Agri Road,<br />
                            Thanjavur, India
                        </div>
                    </div>
                </div>

                <div class="col-md-3 mb-3">
                    <div>
                        <strong>Contact:</strong><br />
                        <span>
                            <i class="fa-solid fa-phone me-1"></i>
                            +91-9876543210 </span><br />
                        <small>Mon-Sat: 8am - 8pm</small>
                    </div>
                </div>

                <div class="col-md-3 mb-3">
                    <div>
                        <strong>Email:</strong><br />
                        <span>
                            <i class="fa-solid fa-envelope me-1"></i>
                            info@farmfresh.com
                        </span>
                    </div>
                </div>

                <div class="col-md-3 mb-3 text-center text-md-start">
                    <div>
                        <strong>Follow Us:</strong><br />
                        <a href="#" class="text-dark me-3">
                            <i class="fa-brands fa-twitter fa-lg"></i>
                        </a>
                        <a href="#" class="text-dark me-3">
                            <i class="fa-brands fa-instagram fa-lg"></i>
                        </a>
                        <a href="#" class="text-dark">
                            <i class="fa-brands fa-facebook fa-lg"></i>
                        </a>
                    </div>
                </div>
            </div>

            <div class="row mt-3">
                <div class="col-12">
                    <div class="text-center text-dark">
                        &copy; 2025 Farm Fresh. All rights reserved.
                    </div>
                </div>
            </div>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"
        crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.min.js"
        crossorigin="anonymous"></script>
    <script src="js/update-supplier-bank-details.js"></script>
</body>

</html>