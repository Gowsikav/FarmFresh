<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Farm Fresh | Admin Dashboard</title>
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
                <ul class="navbar-nav mb-2 mb-lg-0 align-items-center">
                    <li class="nav-item">
                        <a class="nav-link" href="redirectToIndex"><i class="fa-solid fa-house me-2"></i> Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="redirectToAdminDashboard?email=${dto.email}"><i class="fa-solid fa-user-shield me-2"></i></i> Dashboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#"><i class="fa-solid fa-tractor me-2"></i> Farmers</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#"><i class="fa-solid fa-users me-2"></i> Customers</a>
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
                            <li><a class="dropdown-item" href="#" data-bs-toggle="modal"
                                    data-bs-target="#adminProfileModal"><i class="fa-solid fa-user me-2"></i></i> View Profile</a></li>
                            <li>
                                <hr class="dropdown-divider">
                            </li>
                            <li>
                                <a class="dropdown-item text-danger" href="redirectToIndex"><i class="fa-solid fa-right-from-bracket me-2"></i> Logout</a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <main class="flex-grow-1 d-flex">
        <div class="container-fluid p-5">
            <h1>Welcome, ${dto.adminName}!</h1>
            <p>This is your dashboard. You can manage farmers, customers, and products from here.</p>
            <%-- Add more dashboard content here --%>
        </div>
    </main>

    <div class="modal fade" id="adminProfileModal" tabindex="-1" aria-labelledby="adminProfileModalLabel"
        aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header" style="background: linear-gradient(90deg, #2e7d32, #f9fbe7);">
                    <h5 class="modal-title" id="adminProfileModalLabel">Admin Profile</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="text-center mb-4">
                        <c:choose>
                            <c:when test="${empty dto.profilePath}">
                                <img src="images/dummy-profile.png" alt="Profile" class="rounded-circle" width="150"
                                    height="150" style="object-fit: cover;">
                            </c:when>
                            <c:otherwise>
                                <img src="<c:url value='/uploads/${dto.profilePath}'/>" alt="Profile"
                                    class="rounded-circle" width="150" height="150" style="object-fit: cover;">
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="card p-3 shadow-sm">
                        <ul class="list-group list-group-flush">

                            <div class="row mb-2">
                                <div class="col-sm-4 fw-bold">
                                    <i class="fa-solid fa-user me-2"></i>Name:
                                </div>
                                <div class="col-sm-8 text-break">${dto.adminName}</div>
                            </div>
                            <div class="row mb-2">
                                <div class="col-sm-4 fw-bold">
                                    <i class="fa-solid fa-envelope me-2"></i>Email:
                                </div>
                                <div class="col-sm-8 text-break">${dto.email}</div>
                            </div>
                            <div class="row mb-2">
                                <div class="col-sm-4 fw-bold">
                                    <i class="fa-solid fa-phone me-2"></i>Phone:
                                </div>
                                <div class="col-sm-8">${dto.phoneNumber}</div>
                            </div>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <a href="redirectToUpdateAdminProfile?email=${dto.email}" class="btn btn-primary">Update Profile</a>
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