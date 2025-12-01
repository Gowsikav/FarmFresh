<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/includes/sessionCheck.jspf" %>

<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Farm Fresh | Products Price</title>
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

            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav mb-2 mb-lg-0 align-items-center ms-lg-auto me-3">
                    <li class="nav-item">
                        <a class="nav-link" href="redirectToAdminDashboard"><i
                                class="fa-solid fa-user-shield me-2"></i> Dashboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="redirectToManageProducts"><i
                                class="fa-solid fa-box me-2"></i> Manage Products</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="redirectToProductsPrice"><i
                                class="fa-solid fa-tag me-2"></i> Products Price</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="redirectToMilkSuppliersList?page=1&size=10"><i
                                class="fa-solid fa-bottle-droplet me-2"></i> Milk Suppliers</a>
                    </li>
                    <li class="nav-item">
                          <a class="nav-link" href="redirectToCollectMilk"><i class="fa-solid fa-glass-water-droplet me-2"></i> Collect Milk</a>
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
                                    data-bs-target="#adminProfileModal"><i class="fa-solid fa-user me-2"></i>View
                                    Profile</a></li>
                            <li>
                                <hr class="dropdown-divider">
                            </li>
                            <li>
                                <a class="dropdown-item text-danger" href="adminLogout"><i
                                        class="fa-solid fa-right-from-bracket me-2"></i> Logout</a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="page-wrapper d-flex flex-column min-vh-10" style="margin-top: 80px;">
    <div class="container-fluid mt-4 mb-5 flex-grow-1">
        <h2 class="mb-4">Products Price </h2>
        <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addProductWithPriceModal">
            <i class="fa-solid fa-plus me-2"></i>Add Products
        </button>
        <hr />

        <c:if test="${not empty success}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${success}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <!-- Products Table -->
        <div class="table-responsive">
            <table class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <th>Product Name</th>
                        <th>Price (&#8377;)</th>
                        <th>Product Type</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="product" items="${productsPrice}">
                        <tr>
                            <td>${product.productName}</td>
                            <td><i class="fa-solid fa-indian-rupee-sign me-1"></i>${product.price}</td>
                            <td>${product.productType}</td>
                            <td>
                                <button type="button" class="btn btn-primary btn-sm me-2 editProductBtn"
                                    data-bs-toggle="modal" data-bs-target="#editProductModal"
                                    data-id="${product.productId}" data-name="${product.productName}"
                                    data-price="${product.price}"
                                    data-type="${product.productType}">
                                    <i class="fa-solid fa-pen-to-square"></i> Edit
                                </button>
                                <a href="#" class="btn btn-danger btn-sm" data-bs-toggle="modal"
                                    data-bs-target="#deleteConfirmModal"
                                    data-delete-url="deleteProductPrice?productId=${product.productId}">
                                    <i class="fa-solid fa-trash"></i> Delete
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    </div>

    <!-- Add Product Modal -->
    <div class="modal fade" id="addProductWithPriceModal" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header" style="background: linear-gradient(90deg, #2e7d32, #f9fbe7);">
                    <h5 class="modal-title">Add Product Price</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form action="addProductWithPrice" method="post" id="productPriceForm">
                        <div class="mb-3">
                            <label for="productName" class="form-label">Product Name</label>
                            <input type="text" class="form-control" id="productName" name="productName" required>
                            <div id="productNameError" class="error-msg text-danger small"></div>
                        </div>
                        <div class="mb-3">
                            <label for="price" class="form-label">Price (&#8377;)</label>
                            <input type="number" step="any" class="form-control" id="price" name="price" required>
                            <div id="priceError" class="error-msg text-danger small"></div>
                        </div>
                        <div class="mb-3">
                            <label class="form-label d-block">Product Type</label>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="productType" id="buyType" value="BUY"
                                    required>
                                <label class="form-check-label" for="buyType">Buy</label>
                            </div>
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="productType" id="sellType"
                                    value="SELL" required>
                                <label class="form-check-label" for="sellType">Sell</label>
                            </div>
                            <div id="productTypeError" class="error-msg text-danger small"></div>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="submit" id="submitButton" class="btn btn-primary">Submit</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Edit Product Modal -->
    <div class="modal fade" id="editProductModal" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header" style="background: linear-gradient(90deg, #2e7d32, #f9fbe7);">
                    <h5 class="modal-title">Edit Product Price</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="editProductForm" method="post" action="updateProductPrice">
                        <input type="hidden" name="productId" id="editProductId">
                        <div class="mb-3">
                            <label for="editProductName">Product Name</label>
                            <input type="text" class="form-control" name="productName" id="editProductName" required>
                        </div>
                        <div class="mb-3">
                            <label>Price (&#8377;)</label>
                            <input type="number" step="any" class="form-control" name="price" id="editProductPrice" required>
                        </div>
                        <div class="mb-3">
                            <label for="editProductType">Product Type</label>
                            <select class="form-select" id="editProductType" name="productType" required>
                                <option value="BUY">Buy</option>
                                <option value="SELL">Sell</option>
                            </select>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-primary">Update</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>


    <!-- Delete Confirm Modal -->
    <div class="modal fade" id="deleteConfirmModal" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title">Confirm Deletion</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">Are you sure you want to delete this product price?</div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <a id="confirmDeleteBtn" href="#" class="btn btn-danger">Delete</a>
                </div>
            </div>
        </div>
    </div>


    <!-- profile update for admin -->

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
                    <a href="redirectToUpdateAdminProfile" class="btn btn-primary">Update
                        Profile</a>
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
    <script src="js/product-price-form.js"></script>
    <script src="js/admin-notification.js"></script>
</body>

</html>