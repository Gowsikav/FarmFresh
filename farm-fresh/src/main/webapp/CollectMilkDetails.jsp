<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/includes/sessionCheck.jspf" %>

<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Farm Fresh | Collected Milk Details</title>
    <link rel="shortcut icon" href="images/title-pic.png" type="image/x-icon" />

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet"
        crossorigin="anonymous" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
    <link rel="stylesheet" href="css/index.css" />
</head>

<body class="d-flex flex-column min-vh-100">
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

            <div class="collapse navbar-collapse justify-content-end" id="navbarSupportedContent">
                <ul class="navbar-nav mb-2 mb-lg-0 align-items-center">
                    <li class="nav-item">
                        <a class="nav-link" href="redirectToAdminDashboard"><i class="fa-solid fa-user-shield me-2"></i> Dashboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="redirectToManageProducts"><i class="fa-solid fa-box me-2"></i> Manage Products</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="redirectToProductsPrice"><i class="fa-solid fa-tag me-2"></i> Products Price</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="redirectToMilkSuppliersList?page=1&size=10"><i class="fa-solid fa-bottle-droplet me-2"></i> Milk Suppliers</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="redirectToCollectMilk"><i class="fa-solid fa-glass-water-droplet me-2"></i> Collect Milk</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="redirectToAdminPaymentHistory?page=1&size=10"><i
                                class="fa-solid fa-money-bill-transfer me-2"></i> Payment History</a>
                    </li>

                    <!-- Notification dropdown -->
                  <li class="nav-item dropdown">
                      <a class="nav-link position-relative" href="#" id="notificationDropdown" role="button"
                         data-bs-toggle="dropdown" aria-expanded="false">
                          <i class="fa-solid fa-bell fa-lg"></i>
                          <c:if test="${unreadCount > 0}">
            <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                ${unreadCount}
            </span>
                          </c:if>
                      </a>

                      <ul class="dropdown-menu dropdown-menu-end"
                          aria-labelledby="notificationDropdown"
                          style="width: 350px; max-height: 400px; overflow-y: auto;">
                          <li><h6 class="dropdown-header">Notifications</h6></li>

                          <c:choose>
                              <c:when test="${empty notifications}">
                                  <li>
                                      <span class="dropdown-item text-muted">No new notifications</span>
                                  </li>
                              </c:when>
                              <c:otherwise>
                                  <c:forEach var="notification" items="${notifications}">
                                      <li data-notification-id="${notification.id}"
                                        data-admin-email="${dto.email}"
                                          data-notification-type="${notification.notificationType}">
                                          <a class="dropdown-item notification-item" href="#"
                                             data-notification-id="${notification.id}"
                                             data-admin-email="${dto.email}"
                                             data-notification-type="${notification.notificationType}">
                                              <i class="fas fa-bell me-2"></i>
                                              ${notification.message}
                                              <br/>
                                          </a>
                                      </li>
                                  </c:forEach>
                              </c:otherwise>
                          </c:choose>
                      </ul>
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
                                    data-bs-target="#adminProfileModal"><i class="fa-solid fa-user me-2"></i> View Profile</a></li>
                            <li>
                                <hr class="dropdown-divider">
                            </li>
                            <li>
                                <a class="dropdown-item text-danger" href="adminLogout"><i class="fa-solid fa-right-from-bracket me-2"></i> Logout</a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <main class="flex-grow-1 d-flex flex-column min-vh-10" style="margin-top: 80px;">
        <div class="container p-5">

            <div class="d-flex justify-content-between align-items-center mb-3">
                <h1 class="mb-4">Collected Milk Details</h1>
                <a href="redirectToExportAllMilkCollectData?email=${dto.email}" class="btn btn-primary">
                    <i class="fa-solid fa-file-export me-2"></i>Export All Data
                </a>
            </div>
            <form class="row g-3 mb-4" method="get" action="redirectToCollectMilkDetails" onsubmit="return validateDates()">

                <div class="col-auto">
                    <label class="form-label">Search by Date:</label>
                </div>

                <div class="col-auto">
                    <label for="fromSearchDate">From</label>
                    <input type="date" class="form-control" id="fromSearchDate" name="fromSearchDate"
                           value="${fromSearchDate}" pattern="\d{4}-\d{2}-\d{2}" required>
                </div>

                <div class="col-auto">
                    <label for="toSearchDate">To</label>
                    <input type="date" class="form-control" id="toSearchDate" name="toSearchDate"
                           value="${toSearchDate}" pattern="\d{4}-\d{2}-\d{2}" required>
                </div>

                <div class="col-auto">
                    <button type="submit" class="btn btn-primary mt-4">Search</button>
                </div>

                <div class="col-12">
                    <small id="dateError" class="text-danger" style="display:none;"></small>
                </div>
            </form>
            <c:if test="${not empty success}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${success}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
            <div class="table-responsive">
                <table class="table table-bordered table-hover align-middle">
                    <thead class="table-success">
                        <tr>
                            <th>Name</th>
                            <th>Phone Number</th>
                            <th>Type of Milk</th>
                            <th>Quantity (litres)</th>
                            <th>Price (per litre)</th>
                            <th>Total Amount</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="milk" items="${milkList}">
                                        <tr>
                                            <td>${milk.supplier.firstName} ${milk.supplier.lastName}</td>
                                            <td>${milk.supplier.phoneNumber}</td>
                                            <td>${milk.typeOfMilk}</td>
                                            <td>${milk.quantity}</td>
                                            <td>${milk.price}</td>
                                            <td>${milk.totalAmount}</td>
                                            <td>
                                                
                                                <button type="button"
                                                        class="btn btn-info btn-sm view-details-btn"
                                                        data-bs-toggle="modal"
                                                        data-bs-target="#milkDetailsModal"
                                                        data-name="${milk.supplier.firstName} ${milk.supplier.lastName}"
                                                        data-email="${milk.supplier.email}"
                                                        data-phone="${milk.supplier.phoneNumber}"
                                                        data-address="${milk.supplier.address}"
                                                        data-milk="${milk.supplier.typeOfMilk}"
                                                        data-quantity="${milk.quantity}"
                                                        data-typeMilk="${milk.typeOfMilk}"
                                                        data-price="${milk.price}"
                                                        data-total="${milk.totalAmount}"
                                                        data-date="${milk.collectedDate}">
                                                    <i class="fa fa-eye"></i> View
                                                </button>
                                            </td>
                                        </tr>      
                        </c:forEach>
                        <c:if test="${empty milkList}">
                            <tr>
                                <td colspan="8" class="text-center text-muted">No records found for the selected date.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
    
    <div class="modal fade" id="milkDetailsModal" tabindex="-1" aria-labelledby="milkDetailsModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header bg-success text-white">
        <h5 class="modal-title" id="milkDetailsModalLabel">Collected Milk & Supplier Details</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body" id="milkDetailsModalBody">
        <!-- Details will be loaded here by JS -->
      </div>
    </div>
  </div>
</div>

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
                    <a href="redirectToUpdateAdminProfile" class="btn btn-primary">Update Profile</a>
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
        <script src="js/collect-milk-details.js"></script>
        <script src="js/admin-notification.js"></script>
</body>

</html>