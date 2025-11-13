<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/views/includes/sessionCheck.jspf" %>


<html lang="en">

<head>
    <meta charset="UTF-8" />
    <title>Farm Fresh | Update Supplier Profile</title>
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

    <main class="flex-grow-2 d-flex" style="margin-top: 80px;">
        <div class="container-fluid farm-bg2 d-flex flex-column justify-content-md-start align-items-center w-100 pt-5">
            <h2 class="mb-4" style="max-width: 600px; color: azure">Update Suppller Profile</h2>

            <form action="updateSupplierProfile" method="post" enctype="multipart/form-data"
                class="bg-white bg-opacity-75 p-4 rounded shadow-sm" style="max-width: 600px; width: 60%">


                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger" role="alert">
                        ${errorMessage}
                    </div>
                </c:if>

                <div class="mb-3">
                    <label for="firstName" class="form-label">First Name <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" id="firstName" name="firstName" value="${dto.firstName}"
                        required />
                    <span class="text-danger" id="firstNameError"></span>
                </div>
                <div class="mb-3">
                    <label for="lastName" class="form-label">Last Name <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" id="lastName" name="lastName" value="${dto.lastName}"
                        required />
                    <span class="text-danger" id="lastNameError"></span>

                </div>
                <div class="mb-3">
                    <label for="Email" class="form-label">Email</label>
                    <input type="email" class="form-control" id="Email" name="email" value="${dto.email}" readonly />
                </div>
                <div class="mb-3">
                    <label for="phoneNumber" class="form-label">Phone Number</label>
                    <input type="tel" class="form-control" id="phoneNumber" name="phoneNumber"
                        value="${dto.phoneNumber}" readonly />
                </div>
                <div class="mb-3">
                    <label for="address" class="form-label">Address <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" id="address" name="address" value="${dto.address}"
                        required />
                    <span class="text-danger" id="addressError"></span>

                </div>
                <div class="mb-3">
                    <label for="profilePicture" class="form-label">Profile Picture</label>
                    <input type="file" class="form-control" id="profilePicture" name="profilePicture" accept="image/*">
                    <small class="form-text text-muted">Leave blank to keep the current picture.</small>
                </div>

                <button type="submit" class="btn btn-success w-100">Update Profile</button>
                <a href="redirectToSupplierDashboard" class="btn btn-secondary w-100 mt-2">Back to
                    Dashboard</a>
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
    <script src="js/update-supplier-profile.js"></script>
</body>

</html>