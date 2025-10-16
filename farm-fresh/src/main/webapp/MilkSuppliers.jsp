<%@ page isELIgnored="false" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Farm Fresh | Milk Suppliers</title>
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
                        <a class="nav-link" href="redirectToAdminDashboard?email=${dto.email}"><i
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
                        <a class="nav-link active"
                            href="redirectToMilkSuppliersList?email=${dto.email}&page=1&size=10"><i
                                class="fa-solid fa-bottle-droplet me-2"></i> Milk Suppliers</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="redirectToCollectMilk?email=${dto.email}"><i
                                class="fa-solid fa-glass-water-droplet me-2"></i> Collect Milk</a>
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
                                <a class="dropdown-item text-danger" href="adminLogout?email=${dto.email}"><i
                                        class="fa-solid fa-right-from-bracket me-2"></i> Logout</a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="page-wrapper d-flex flex-column min-vh-10" style="margin-top: 80px;"></div>
    <div class="container mt-4 mb-5 flex-grow-1">
        <h2 class="mb-4">Milk Suppliers</h2>

        <div class="d-flex justify-content-between align-items-center mb-3">
            <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addMilkSupplierModal">
                <i class="fa-solid fa-plus me-2"></i>Add Milk Supplier
            </button>

            <form action="searchSuppliers" method="get" class="d-flex">
                <input type="hidden" name="email" value="${dto.email}">
                <input type="text" name="keyword" class="form-control me-2" placeholder="Search suppliers...">
                <button type="submit" class="btn btn-primary">Search</button>
            </form>
        </div>


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

        <div class="table-responsive">
            <table class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <th>FirstName</th>
                        <th>LastName</th>
                        <th>Email</th>
                        <th>Phone Number</th>
                        <th>Address</th>
                        <th>Type Of Milk</th>
                        <th>Bank Details</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="supplier" items="${milkSuppliers}">
                        <tr>
                            <td>${supplier.firstName}</td>
                            <td>${supplier.lastName}</td>
                            <td>${supplier.email}</td>
                            <td>${supplier.phoneNumber}</td>
                            <td class="text-break">${supplier.address}</td>
                            <td>${supplier.typeOfMilk}</td>
                            
                            <td>
                                <c:choose>
                                    <c:when test="${not empty supplier.supplierBankDetails}">
                                        <button class="btn btn-primary btn-sm me-2 viewSupplierBankBtn"
                                            data-bs-toggle="modal" data-bs-target="#viewSupplierBankDetailsModal"
                                            data-supplier-name="${supplier.firstName} ${supplier.lastName}"
                                            data-supplier-email="${supplier.email}"
                                            data-bank-name="${supplier.supplierBankDetails.bankName}"
                                            data-bank-branch="${supplier.supplierBankDetails.bankBranch}"
                                            data-account-number="${supplier.supplierBankDetails.accountNumber}"
                                            data-ifsc-code="${supplier.supplierBankDetails.IFSCCode}"
                                            data-account-type="${supplier.supplierBankDetails.accountType}">
                                             <i class="fa-solid fa-eye"></i>View
                                        </button>
                                        <button class="btn btn-primary btn-sm me-2 editSupplierBankBtn"
                                            data-bs-toggle="modal" data-bs-target="#editSupplierBankDetailsModal"
                                            data-supplier-id="${supplier.supplierId}"
                                            data-supplier-name="${supplier.firstName} ${supplier.lastName}"
                                            data-supplier-email="${supplier.email}"
                                            data-bank-name="${supplier.supplierBankDetails.bankName}"
                                            data-bank-branch="${supplier.supplierBankDetails.bankBranch}"
                                            data-account-number="${supplier.supplierBankDetails.accountNumber}"
                                            data-ifsc-code="${supplier.supplierBankDetails.IFSCCode}"
                                            data-account-type="${supplier.supplierBankDetails.accountType}">
                                            <i class="fa-solid fa-pen-to-square"></i> Edit
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">No Bank Details</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td>
                                <button type="button" class="btn btn-primary btn-sm me-2 viewSupplierBtn"
                                    data-bs-toggle="modal" data-bs-target="#viewSupplierModal"
                                    data-firstname="${supplier.firstName}" data-lastname="${supplier.lastName}"
                                    data-email="${supplier.email}" data-phone="${supplier.phoneNumber}"
                                    data-address="${supplier.address}" data-milk="${supplier.typeOfMilk}">
                                    <i class="fa-solid fa-eye"></i> View
                                </button>

                                <button type="button" class="btn btn-primary btn-sm me-2 editSupplierBtn"
                                    data-bs-toggle="modal" data-bs-target="#editSupplierModal"
                                    data-id="${supplier.supplierId}" data-firstname="${supplier.firstName}"
                                    data-lastname="${supplier.lastName}" data-email="${supplier.email}"
                                    data-phone="${supplier.phoneNumber}" data-address="${supplier.address}"
                                    data-milk="${supplier.typeOfMilk}">
                                    <i class="fa-solid fa-pen-to-square"></i> Edit
                                </button>

                                <a href="#" class="btn btn-danger btn-sm" data-bs-toggle="modal"
                                    data-bs-target="#deleteConfirmModal"
                                    data-delete-url="deleteMilkSupplier?email=${supplier.email}&adminEmail=${dto.email}">
                                    <i class="fa-solid fa-trash"></i> Delete
                                </a>
                            </td>
                        </tr>
                    </c:forEach>

                </tbody>
            </table>
            <div>
                <c:if test="${currentPage > 1}">
                    <a class="btn btn-outline-secondary"
                        href="redirectToMilkSuppliersList?email=${dto.email}&page=${currentPage - 1}&size=${pageSize}">Previous</a>
                </c:if>
                <span> Page ${currentPage} of ${totalPages} </span>
                <c:if test="${currentPage < totalPages}">
                    <a class="btn btn-outline-secondary"
                        href="redirectToMilkSuppliersList?email=${dto.email}&page=${currentPage + 1}&size=${pageSize}">Next</a>
                </c:if>
            </div>
        </div>
    </div>

    <!-- Add Milk Supplier Modal -->
    <div class="modal fade" id="addMilkSupplierModal" tabindex="-1" aria-labelledby="addMilkSupplierModalLabel"
        aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header" style="background: linear-gradient(90deg, #2e7d32, #f9fbe7);">
                    <h5 class="modal-title" id="addMilkSupplierModalLabel">Add New Milk Supplier</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            ${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>
                    <form action="addMilkSupplier?adminEmail=${dto.email}" method="post" id="supplierForm">
                        <div class="mb-3">
                            <label for="firstName" class="form-label">First Name</label>
                            <input type="text" class="form-control" id="firstName" name="firstName"
                                value="${supplier.firstName}" placeholder="Enter first name" required>
                            <div id="firstNameError" class="error-msg text-danger small"></div>
                        </div>

                        <div class="mb-3">
                            <label for="lastName" class="form-label">Last Name</label>
                            <input type="text" class="form-control" id="lastName" name="lastName"
                                value="${supplier.lastName}" placeholder="Enter last name" required>
                            <div id="lastNameError" class="error-msg text-danger small"></div>
                        </div>

                        <div class="mb-3">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="email" name="email" value="${supplier.email}"
                                placeholder="Enter email" required>
                            <div id="emailError" class="error-msg text-danger small"></div>
                        </div>

                        <div class="mb-3">
                            <label for="phoneNumber" class="form-label">Phone Number</label>
                            <input type="tel" class="form-control" id="phoneNumber" name="phoneNumber"
                                value="${supplier.phoneNumber}" placeholder="Enter phone number" required>
                            <div id="phoneNumberError" class="error-msg text-danger small"></div>
                        </div>

                        <div class="mb-3">
                            <label for="address" class="form-label">Address</label>
                            <textarea class="form-control" id="address" rows="3" name="address"
                                placeholder="Enter address">${supplier.address}</textarea>
                            <div id="addressError" class="error-msg text-danger small"></div>
                        </div>

                        <div class="mb-3">
                            <label for="typeOfMilk" class="form-label">Type of Milk</label>
                            <select class="form-select" id="typeOfMilk" name="typeOfMilk" required>
                                <option value="">Select milk type</option>
                            </select>
                            <div id="typeOfMilkError" class="error-msg text-danger small"></div>

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

    <!-- view Supplier Modal  -->

    <div class="modal fade" id="viewSupplierModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header" style="background: linear-gradient(90deg, #2e7d32, #f9fbe7);">
                    <h5 class="modal-title">Supplier Details</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p><strong>First Name:</strong> <span id="modalFirstName"></span></p>
                    <p><strong>Last Name:</strong> <span id="modalLastName"></span></p>
                    <p><strong>Email:</strong> <span id="modalEmail"></span></p>
                    <p><strong>Phone:</strong> <span id="modalPhone"></span></p>
                    <p><strong>Address:</strong> <span id="modalAddress"></span></p>
                    <p><strong>Type of Milk:</strong> <span id="modalMilk"></span></p>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Edit Supplier Modal  -->

    <div class="modal fade" id="editSupplierModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header" style="background: linear-gradient(90deg, #2e7d32, #f9fbe7);">
                    <h5 class="modal-title">Edit Supplier</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="editSupplierForm" method="post" action="updateMilkSupplier?adminEmail=${dto.email}">
                        <input type="hidden" name="id" id="editSupplierId">
                        <div class="mb-3">
                            <label for="editFirstName">First Name</label>
                            <input type="text" class="form-control" name="firstName" id="editFirstName">
                        </div>
                        <div class="mb-3">
                            <label for="editLastName">Last Name</label>
                            <input type="text" class="form-control" name="lastName" id="editLastName">
                        </div>
                        <div class="mb-3">
                            <label for="editEmail">Email</label>
                            <input type="email" class="form-control" name="email" id="editEmail" readonly>
                        </div>
                        <div class="mb-3">
                            <label for="editPhone">Phone</label>
                            <input type="tel" class="form-control" name="phoneNumber" id="editPhone" readonly>
                        </div>
                        <div class="mb-3">
                            <label for="editAddress">Address</label>
                            <textarea class="form-control" name="address" id="editAddress" rows="3"></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="editMilk" class="form-label">Type of Milk</label>
                            <select class="form-select" id="editMilk" name="typeOfMilk" required>
                                <option value="">Select milk type</option>
                            </select>
                            <div id="editMilkError" class="error-msg text-danger small"></div>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="submit" id="submitButton1" class="btn btn-primary">Update</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!--     delete confirm-->

    <div class="modal fade" id="deleteConfirmModal" tabindex="-1" aria-labelledby="deleteConfirmLabel"
        aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title" id="deleteConfirmLabel">Confirm Deletion</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    Are you sure you want to delete this supplier?
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <a id="confirmDeleteBtn" href="#" class="btn btn-danger">Delete</a>
                </div>
            </div>
        </div>
    </div>

    <!-- View Supplier Bank Details Modal (Read-Only) -->
    <div class="modal fade" id="viewSupplierBankDetailsModal" tabindex="-1"
        aria-labelledby="viewSupplierBankDetailsModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg">
            <div class="modal-content rounded-4 shadow-lg">
                <div class="modal-header bg-success text-white">
                    <h5 class="modal-title" id="viewSupplierBankDetailsModalLabel">
                        <i class="fa-solid fa-building-columns me-2"></i>Supplier Bank Details
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                        aria-label="Close"></button>
                </div>

                <div class="modal-body px-4 py-3">
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label class="form-label fw-bold">Supplier Name</label>
                            <div><span id="supplierName"></span></div>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-bold">Email</label>
                            <div><span id="supplierEmail"></span></div>
                        </div>
                    </div>
                    <hr>
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label class="form-label fw-bold">Bank Name</label>
                            <div><span id="bankName"></span></div>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-bold">Branch</label>
                            <div><span id="branch"></span></div>
                        </div>
                    </div>
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label class="form-label fw-bold">Account Number</label>
                            <div><span id="accountNumber"></span></div>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-bold">IFSC Code</label>
                            <div><span id="ifscCode"></span></div>
                        </div>
                    </div>
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label class="form-label fw-bold">Account Type</label>
                            <div><span id="accountType"></span></div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary rounded-pill" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Edit Supplier Bank Details Modal -->
    <div class="modal fade" id="editSupplierBankDetailsModal" tabindex="-1"
        aria-labelledby="editSupplierBankDetailsModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-lg">
            <div class="modal-content rounded-4 shadow-lg">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title" id="editSupplierBankDetailsModalLabel">
                        <i class="fa-solid fa-building-columns me-2"></i>Edit Supplier Bank Details
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"
                        aria-label="Close"></button>
                </div>
                <form id="editBankDetailsForm" method="post" action="updateSupplierBankDetailsByAdmin?adminEmail=${dto.email}">
                    <div class="modal-body px-4 py-3">
                        <input type="hidden" name="supplierId" id="editBankSupplierId">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="editBankSupplierName" class="form-label fw-bold">Supplier Name</label>
                                <input type="text" class="form-control" id="editBankSupplierName" readonly>
                            </div>
                            <div class="col-md-6">
                                <label for="editBankSupplierEmail" class="form-label fw-bold">Email</label>
                                <input type="text" class="form-control" id="editBankSupplierEmail" name="email" readonly>
                            </div>
                        </div>
                        <hr>
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="editBankName" class="form-label fw-bold">Bank Name</label>
                                <input type="text" class="form-control" name="bankName" id="editBankName" required>
                                <span class="text-danger" id="bankNameError"></span>
                            </div>
                            <div class="col-md-6">
                                <label for="editBankBranch" class="form-label fw-bold">Branch</label>
                                <input type="text" class="form-control" name="bankBranch" id="editBankBranch" required>
                                <span class="text-danger" id="bankBranchError"></span>
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="editAccountNumber" class="form-label fw-bold">Account Number</label>
                                <input type="text" class="form-control" name="accountNumber" id="editAccountNumber"
                                    required>
                                <span class="text-danger" id="accountNumberError"></span>

                            </div>
                            <div class="col-md-6">
                                <label for="editIfscCode" class="form-label fw-bold">IFSC Code</label>
                                <input type="text" class="form-control" name="IFSCCode" id="editIfscCode" required>
                                <span class="text-small">4 captial letters +0 + 6 alphanumeric letters</span>
                                <span class="text-danger" id="ifscCodeError"></span>
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="editAccountType" class="form-label fw-bold">Account Type</label>
                                <select class="form-select" name="accountType" id="editAccountType" required>
        <option value="">Select Account Type</option>
        <option value="Savings">Savings</option>
        <option value="Current">Current</option>
        <option value="Salary">Salary</option>
    </select>
                                <span class="text-danger" id="accountTypeError"></span>
                            </div>
                        
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary rounded-pill"
                            data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary rounded-pill">Save Changes</button>
                    </div>
                </form>
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
                    <a href="redirectToUpdateAdminProfile?email=${dto.email}" class="btn btn-primary">Update
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
    <script src="js/supplier-form-validation.js"></script>
    <script src="js/admin-notification.js"></script>
</body>

</html>