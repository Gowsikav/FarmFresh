<%@ page isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Farm Fresh | Milk Supplier Login</title>
    <link rel="shortcut icon" href="images/title-pic.png" type="image/x-icon" />

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet"
        crossorigin="anonymous" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
    <link rel="stylesheet" href="css/index.css" />
</head>

<body class="d-flex flex-column min-vh-100">
    <nav class="navbar navbar-expand-lg" style="background: linear-gradient(90deg, #2e7d32, #f9fbe7)">
        <div class="container-fluid">
            <a class="navbar-brand">
                <img src="images/farm-fresh-logo.png" alt="Farm Fresh Logo" height="80" width="80"
                    class="d-inline-block align-text-top rounded-circle border border-light p-1 ms-3 me-2" />
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false"
                aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse justify-content-end" id="navbarSupportedContent">
                <ul class="navbar-nav mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="redirectToIndex"><i class="fa-solid fa-house me-2"></i>Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="redirectToCustomerRegister"><i
                                class="fa-solid fa-user-plus me-2"></i>Customer Register</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="loginDropdown" role="button"
                            data-bs-toggle="dropdown" aria-expanded="false"><i
                                class="fa-solid fa-right-to-bracket me-2"></i>
                            Login
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="loginDropdown">
                            <li>
                                <a class="dropdown-item" href="redirectToAdminLogin"><i
                                        class="fa-solid fa-user-tie me-2"></i>Admin Login</a>
                            </li>
                            <li>
                                <a class="dropdown-item" href="redirectToMilkSupplierLogin"><i
                                        class="fa-solid fa-bottle-droplet me-2"></i> Milk
                                    Supplier Login</a>
                            </li>

                            <li>
                                <a class="dropdown-item" href="redirectToCustomerLogin"><i
                                        class="fa-solid fa-user me-2"></i>Customer Login</a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <main class="flex-grow-1 d-flex">
        <div class="container-fluid supplier-bg d-flex flex-column justify-content-md-start align-items-center w-100">
            <h2 class="mb-4" style="max-width: 600px; color: #f9fbe7">
                Milk Supplier Login
            </h2>

            <form action="sendOTPToSupplier" id="milkSupplierLoginForm"
                class="bg-white bg-opacity-75 p-4 rounded shadow-sm" style="max-width: 600px; width: 60%">
                <div class="mb-3">
                    <label for="supplierEmail" class="form-label">Email <span class="text-danger">*</span></label>
                    <div class="input-group">
                        <span class="input-group-text"><i class="fa-solid fa-envelope"></i></span>
                        <input type="email" class="form-control" id="supplierEmail" placeholder="Enter Email"
                            name="email" value="${email}" required />
                    </div>
                    <span id="emailError" class="text small"></span>
                </div>
                <button id="loginBtn" type="submit" class="btn btn-success w-100" disabled>
                    Login
                </button>
            </form>
        </div>
    </main>

    <div class="modal fade" id="otpModal" tabindex="-1" aria-labelledby="otpModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="otpModalLabel">Enter OTP</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            ${successMessage}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>
                    <c:if test="${not empty error}">
                        <div id="error" class="alert alert-danger alert-dismissible fade show" role="alert">
                            ${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>
                    <form id="otpForm" action="verifySupplierOTP" method="post">
                        <input type="email" name="email" value="${email}" hidden />
                        <div id="otpTimerDisplay" class="text-center text-secondary mb-2"></div>
                        <div class="mb-3">
                            <label for="otpInput" class="form-label">OTP</label>
                            <input type="text" class="form-control" id="otpInput" name="otp" required />
                        </div>
                        <button type="submit" class="btn btn-primary w-100">
                            Verify OTP
                        </button>
                        <button type="button" id="resendOtpBtn" class="btn btn-warning w-100 mt-2" disabled>
                             Login Again
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>

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
    <script src="js/milk-supplier-login.js"></script>
</body>

</html>