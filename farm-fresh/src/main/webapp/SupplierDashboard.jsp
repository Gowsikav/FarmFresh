<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/includes/sessionCheck.jspf" %>

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
    <script>
  localStorage.removeItem("otpStartTime");
</script>


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
                    <li class="nav-item">
                        <a class="nav-link" href="redirectToMilkCollection"><i
                                class="fa-solid fa-glass-water-droplet me-2"></i>Milk Collection</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="redirectToPaymentStatus"><i
                                class="fa-solid fa-money-bill me-2"></i>Payment Status</a>
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
                                    data-bs-target="#supplierProfileModal"><i class="fa-solid fa-user me-2"></i>View
                                    Profile</a></li>
                            <li><a class="dropdown-item" href="#" data-bs-toggle="modal"
                                    data-bs-target="#supplierBankModal"><i
                                        class="fa-solid fa-building-columns me-2"></i>View Bank Details</a></li>
                            <li>
                                <hr class="dropdown-divider">
                            </li>
                            <li><a class="dropdown-item text-danger" href="supplierLogout"><i
                                        class="fa-solid fa-right-from-bracket me-2"></i>Logout</a></li>
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
                            <p class="card-text fs-4 fw-bold">${totalLitres} Ltr</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card text-white bg-info mb-3 shadow">
                        <div class="card-body">
                            <h5 class="card-title">Total Amount Received</h5>
                            <p class="card-text fs-4 fw-bold">${totalAmountPaid}</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card text-white bg-primary mb-3 shadow">
                        <div class="card-body">
                            <h5 class="card-title">Last Collection Date</h5>
                            <p class="card-text fs-4 fw-bold">${lastCollectedDate}</p>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Add more supplier-specific content here -->
        </div>
    </main>

    <!-- Supplier Profile Modal -->
    <div class="modal fade" id="supplierProfileModal" tabindex="-1" aria-labelledby="supplierProfileModalLabel"
        aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header" style="background: linear-gradient(90deg, #388e3c, #e8f5e9);">
                    <h5 class="modal-title" id="supplierProfileModalLabel">Supplier Profile</h5>
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
                            <div class="row mb-2">
                                <div class="col-sm-4 fw-bold"><i class="fa-solid fa-bottle-water me-2"></i>Type of Milk:
                                </div>
                                <div class="col-sm-8">${dto.typeOfMilk}</div>
                            </div>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <a href="redirectToUpdateSupplierProfile" class="btn btn-primary">Update
                        Profile</a>
                </div>
            </div>
        </div>
    </div>

   <!-- Bank Details Modal -->
   <div class="modal fade" id="supplierBankModal" tabindex="-1" aria-labelledby="supplierBankModalLabel"
       aria-hidden="true">
       <div class="modal-dialog modal-dialog-centered modal-lg">
           <div class="modal-content">
               <!-- Modal Header -->
               <div class="modal-header">
                   <h5 class="modal-title" id="supplierBankModalLabel">
                       <i class="fa-solid fa-building-columns me-2"></i>Bank Details
                   </h5>
                   <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
               </div>

               <!-- Modal Body -->
               <div class="modal-body">
                   <c:choose>
                       <c:when test="${empty dto.supplierBankDetails}">
                           <div class="alert alert-warning" role="alert">
                               No bank details found. Please add your bank details.
                           </div>
                       </c:when>
                       <c:otherwise>
                           <div class="card p-3 shadow-sm">
                               <ul class="list-group list-group-flush">
                                   <div class="row mb-2">
                                       <div class="col-sm-4 fw-bold"><i class="fa-solid fa-building-columns me-2"></i>Bank Name:</div>
                                       <div class="col-sm-8 text-break">${dto.supplierBankDetails.bankName}</div>
                                   </div>
                                   <div class="row mb-2">
                                       <div class="col-sm-4 fw-bold"><i class="fa-solid fa-code-branch me-2"></i>Branch Name:</div>
                                       <div class="col-sm-8 text-break">${dto.supplierBankDetails.bankBranch}</div>
                                   </div>
                                   <div class="row mb-2">
                                       <div class="col-sm-4 fw-bold"><i class="fa-solid fa-hashtag me-2"></i>Account Number:</div>
                                       <div class="col-sm-8 text-break">${dto.supplierBankDetails.accountNumber}</div>
                                   </div>
                                   <div class="row mb-2">
                                       <div class="col-sm-4 fw-bold"><i class="fa-solid fa-key me-2"></i>IFSC Code:</div>
                                       <div class="col-sm-8 text-break">${dto.supplierBankDetails.IFSCCode}</div>
                                   </div>
                                   <div class="row mb-2">
                                       <div class="col-sm-4 fw-bold"><i class="fa-solid fa-list-check me-2"></i>Account Type:</div>
                                       <div class="col-sm-8 text-break">${dto.supplierBankDetails.accountType}</div>
                                   </div>
                               </ul>
                           </div>
                           <div class="alert alert-warning mt-3" role="alert">
                               To update bank details, please contact Admin.
                           </div>
                       </c:otherwise>
                   </c:choose>
               </div>

               <!-- Modal Footer -->
               <div class="modal-footer">
                   <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                   <c:if test="${empty dto.supplierBankDetails}">
                       <a href="redirectToUpdateSupplierBankDetails" class="btn btn-primary">Fill Bank Details</a>
                   </c:if>
               </div>
           </div>
       </div>
   </div>

    <!-- Footer -->
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