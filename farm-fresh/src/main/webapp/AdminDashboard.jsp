<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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

<body class="d-flex flex-column min-vh-100" style="overflow-y: auto !important;">
    <nav class="navbar navbar-expand-lg fixed-top" style="background: linear-gradient(90deg, #2e7d32, #f9fbe7)">
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

            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav mb-2 mb-lg-0 align-items-center ms-lg-auto me-3">
                    <li class="nav-item">
                        <a class="nav-link active" href="redirectToAdminDashboard?email=${dto.email}"><i
                                class="fa-solid fa-user-shield me-2"></i> Dashboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="redirectToManageProducts?email=${dto.email}"><i
                                class="fa-solid fa-box me-2"></i> Manage Products</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="redirectToProductsPrice?email=${dto.email}"><i
                                class="fa-solid fa-tag me-2"></i> Products Price</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="redirectToMilkSuppliersList?email=${dto.email}&page=1&size=10"><i
                                class="fa-solid fa-bottle-droplet me-2"></i> Milk Suppliers</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="redirectToCollectMilk?email=${dto.email}"><i
                                class="fa-solid fa-glass-water-droplet me-2"></i> Collect Milk</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="redirectToAdminPaymentHistory?email=${dto.email}&page=1&size=10"><i
                                class="fa-solid fa-money-bill-transfer me-2"></i> Payment History</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#"><i class="fa-solid fa-users me-2"></i> Customers</a>
                    </li>
                    <!-- Notification dropdown -->
                    <li class="nav-item dropdown">
                        <a class="nav-link position-relative" href="#" id="notificationDropdown" role="button"
                            data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fa-solid fa-bell fa-lg"></i>
                            <c:if test="${unreadCount > 0}">
                                <span
                                    class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                                    ${unreadCount}
                                </span>
                            </c:if>
                        </a>

                        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="notificationDropdown"
                            style="width: 350px; max-height: 400px; overflow-y: auto;">
                            <li>
                                <h6 class="dropdown-header">Notifications</h6>
                            </li>

                            <c:choose>
                                <c:when test="${empty notifications}">
                                    <li>
                                        <span class="dropdown-item text-muted">No new notifications</span>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="notification" items="${notifications}">
                                        <li data-notification-id="${notification.id}" data-admin-email="${dto.email}"
                                            data-notification-type="${notification.notificationType}">
                                            <a class="dropdown-item notification-item" href="#"
                                                data-notification-id="${notification.id}"
                                                data-admin-email="${dto.email}"
                                                data-notification-type="${notification.notificationType}">
                                                <i class="fas fa-bell me-2"></i>
                                                ${notification.message}
                                                <br />
                                            </a>
                                        </li>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </li>

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
                                data-bs-target="#adminProfileModal"><i class="fa-solid fa-user me-2"></i>View
                                Profile</a></li>
                        <li>
                            <hr class="dropdown-divider">
                        </li>
                        <li>
                            <a class="dropdown-item text-danger" href="adminLogout?email=${dto.email}"><i
                                    class="fa-solid fa-right-from-bracket me-2"></i> Logout</a>
                        </li>
                    </ul>
                </ul>
            </div>
        </div>
    </nav>

    <main class="flex-grow-1 d-flex" style="margin-top: 80px;">
        <div class="container-fluid p-5">
            <!-- Welcome Message -->
            <h1 class="fw-bold">Welcome back, ${dto.adminName} </h1>
            <p class="text-muted">Here's an overview of what's happening in your farm-fresh platform today.</p>

            <!-- Info Cards -->
            <div class="row mt-4">
                <div class="col-md-3">
                    <div class="card text-white bg-primary mb-3 shadow">
                        <div class="card-body">
                            <h5 class="card-title">Total Customers</h5>
                            <p class="card-text fs-4 fw-bold">1,245</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card text-white bg-success mb-3 shadow">
                        <div class="card-body">
                            <h5 class="card-title">Total Milk suppliers</h5>
                            <p class="card-text fs-4 fw-bold">${suppliersCount}</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card text-white bg-warning mb-3 shadow">
                        <div class="card-body">
                            <h5 class="card-title">Active Orders</h5>
                            <p class="card-text fs-4 fw-bold">89</p>
                            <small class="text-light">12 pending delivery</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card text-white bg-danger mb-3 shadow">
                        <div class="card-body">
                            <h5 class="card-title">Revenue</h5>
                            <p class="card-text fs-4 fw-bold">Rs.4,58,920</p>
                            <small class="text-light">This month</small>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Recent Activity -->
            <div class="row">
                <!-- Recent Customers -->
                <div class="col-md-4">
                    <div class="card mt-4 shadow">
                        <div class="card-header bg-dark text-white fw-bold">
                            Recent Customers
                        </div>
                        <div class="card-body p-0">
                            <ul class="list-group list-group-flush">
                                <li class="list-group-item">Ananya Sharma - ananya@gmail.com</li>
                                <li class="list-group-item">Ravi Kumar - ravi.kumar@gmail.com</li>
                                <li class="list-group-item">Megha Singh - megha.singh@gmail.com</li>
                                <li class="list-group-item">Aman Gupta - aman.gupta@gmail.com</li>
                            </ul>
                        </div>
                    </div>
                </div>

                <!-- New Farmer Requests -->
                <div class="col-md-4">
                    <div class="card mt-4 shadow">
                        <div class="card-header bg-dark text-white fw-bold">
                            New Farmer Requests
                        </div>
                        <div class="card-body p-0">
                            <ul class="list-group list-group-flush">
                                <li class="list-group-item">Rajesh Patel - Pending Approval</li>
                                <li class="list-group-item">Kavita Yadav - Pending Approval</li>
                                <li class="list-group-item">Sunil Verma - Pending Approval</li>
                            </ul>
                        </div>
                    </div>
                </div>

                <!-- Latest Orders -->
                <div class="col-md-4">
                    <div class="card mt-4 shadow">
                        <div class="card-header bg-dark text-white fw-bold">
                            Latest Orders
                        </div>
                        <div class="card-body p-0">
                            <ul class="list-group list-group-flush">
                                <li class="list-group-item">Order #1542 - Completed</li>
                                <li class="list-group-item">Order #1543 - Out for Delivery</li>
                                <li class="list-group-item">Order #1544 - Payment Pending</li>
                                <li class="list-group-item">Order #1545 - Cancelled</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
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

    <footer class="text-lg-start py-3 mt-auto" style="background: linear-gradient(90deg, #1b5e20, #fffde7); color: #333">
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
    <script src="js/admin-notification.js"></script>
</body>

</html>