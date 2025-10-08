<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Farm Fresh | Supplier Dashboard</title>
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
                <img src="images/farm-fresh-logo.png" alt="Farm Fresh Logo" height="60" width="60" class="rounded-circle border border-light p-1 ms-3 me-2" />
            </a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav ms-auto me-3">
                    <li class="nav-item">
                        <a class="nav-link active" href="redirectToSupplierDashboard?email=${dto.email}"><i class="fa-solid fa-user me-2"></i>Dashboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="redirectToMilkCollection?email=${dto.email}"><i class="fa-solid fa-glass-water-droplet me-2"></i>Milk Collection</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="redirectToPaymentStatus?email=${dto.email}"><i class="fa-solid fa-money-bill me-2"></i>Payment Status</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="profileDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                            
                            <img src="images/dummy-profile.png" alt="Profile" class="rounded-circle" width="40" height="40" style="object-fit: cover;">
                                
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="profileDropdown">
                            <li><a class="dropdown-item" href="#" data-bs-toggle="modal" data-bs-target="#supplierProfileModal"><i class="fa-solid fa-user me-2"></i>View Profile</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item text-danger" href="supplierLogout?email=${dto.email}"><i class="fa-solid fa-right-from-bracket me-2"></i>Logout</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <main class="flex-grow-1 farm-bg2 d-flex" style="margin-top: 80px; color: aliceblue;">
        <div class="container-fluid p-5">
            <h1 class="fw-bold">Welcome, ${dto.firstName} ${dto.lastName}</h1>
            <p class="text-small text-white">Here's your latest activity and stats.</p>
            <div class="row mt-4">
                <div class="col-md-4">
                    <div class="card text-white bg-success mb-3 shadow">
                        <div class="card-body">
                            <h5 class="card-title">Total Milk Collected</h5>
                            <p class="card-text fs-4 fw-bold">10 Ltr</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card text-white bg-info mb-3 shadow">
                        <div class="card-body">
                            <h5 class="card-title">Pending Payments</h5>
                            <p class="card-text fs-4 fw-bold">Rs.100</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card text-white bg-primary mb-3 shadow">
                        <div class="card-body">
                            <h5 class="card-title">Last Collection Date</h5>
                            <p class="card-text fs-4 fw-bold">10/03/2025</p>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Add more supplier-specific content here -->
        </div>
    </main>

    <!-- Supplier Profile Modal -->
    <div class="modal fade" id="supplierProfileModal" tabindex="-1" aria-labelledby="supplierProfileModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header" style="background: linear-gradient(90deg, #388e3c, #e8f5e9);">
                    <h5 class="modal-title" id="supplierProfileModalLabel">Supplier Profile</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="text-center mb-4">                       
                        <img src="images/dummy-profile.png" alt="Profile" class="rounded-circle" width="150" height="150" style="object-fit: cover;">                        
                    </div>
                    <div class="card p-3 shadow-sm">
                        <ul class="list-group list-group-flush">
                            <div class="row mb-2">
                                <div class="col-sm-4 fw-bold"><i class="fa-solid fa-user me-2"></i>First Name:</div>
                                <div class="col-sm-8 text-break">${dto.firstName}</div>
                            </div>
                            <div class="row mb-2">
                                <div class="col-sm-4 fw-bold"><i class="fa-solid fa-user me-2"></i>Last Name:</div>
                                <div class="col-sm-8 text-break">${dto.lastName}</div>
                            </div>
                            <div class="row mb-2">
                                <div class="col-sm-4 fw-bold"><i class="fa-solid fa-envelope me-2"></i>Email:</div>
                                <div class="col-sm-8 text-break">${dto.email}</div>
                            </div>
                            <div class="row mb-2">
                                <div class="col-sm-4 fw-bold"><i class="fa-solid fa-phone me-2"></i>Phone:</div>
                                <div class="col-sm-8">${dto.phoneNumber}</div>
                            </div>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <a href="redirectToUpdateSupplierProfile?email=${dto.email}" class="btn btn-primary">Update Profile</a>
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
</body>
</html>
