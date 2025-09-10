<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Farm Fresh | Farmer Register</title>
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
                        <a class="nav-link" href="redirectToIndex">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="redirectToFarmerRegister">Farmer Register</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="loginDropdown" role="button"
                            data-bs-toggle="dropdown" aria-expanded="false">
                            Login
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end bg-opacity-50" aria-labelledby="loginDropdown">
                            <li>
                                <a class="dropdown-item" href="redirectToAdminLogin">Admin Login</a>
                            </li>
                            <li>
                                <a class="dropdown-item" href="redirectToCustomerLogin">Customer Login</a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <main class="flex-grow-2 d-flex">
        <div class="container-fluid farm-bg2 d-flex flex-column justify-content-center align-items-start w-100"
            style="padding-left: 60px">
            <h2 class="mb-4" style="max-width: 600px; color: azure">
                Farmer Registration
            </h2>
            <p style="max-width: 600px; color: azure">
                Register as a farmer to supply fresh produce directly to customers.<br />
                Fill in your details below to join Farm Fresh.
            </p>
            <form action="registerFarmer" method="post" class="bg-white bg-opacity-75 p-4 rounded shadow-sm"
                style="max-width: 600px; width: 60%">
                <div class="mb-3">
                    <label for="farmerName" class="form-label">Full Name <span class="text-danger"> *</span></label>
                    <input type="text" class="form-control" id="farmerName" name="farmerName" required />
                </div>
                <div class="mb-3">
                    <label for="farmerEmail" class="form-label">Email <span class="text-danger"> *</span></label>
                    <input type="email" class="form-control" id="farmerEmail" name="farmerEmail" required />
                </div>
                <div class="mb-3">
                    <label for="farmerPhone" class="form-label">Phone Number <span class="text-danger"> *</span></label>
                    <input type="tel" class="form-control" id="farmerPhone" name="farmerPhone" required />
                </div>
                <div class="mb-3">
                    <label for="farmerAddress" class="form-label">Address <span class="text-danger"> *</span></label>
                    <textarea class="form-control" id="farmerAddress" name="farmerAddress" rows="2" required></textarea>
                </div>
                <div class="mb-3">
                    <label for="farmType" class="form-label">Type of Farm/Produce <span class="text-danger"> *</span></label>
                    <input type="text" class="form-control" id="farmType" name="farmType" required />
                </div>
                <button type="submit" class="btn btn-success w-100">Register</button>
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
</body>

</html>