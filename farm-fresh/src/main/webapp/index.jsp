<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Farm Fresh | Home</title>
    <link rel="shortcut icon" href="images/title-pic.png" type="image/x-icon">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
    <link rel="stylesheet" href="css/index.css" />
</head>

<body class="d-flex flex-column min-vh-100">
    <nav class="navbar navbar-expand-lg fixed-top" style="background: linear-gradient(90deg, #2e7d32, #f9fbe7);">
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
                        <a class="nav-link active" href="redirectToIndex"><i class="fa-solid fa-house me-2"></i>Home</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="loginDropdown" role="button"
                            data-bs-toggle="dropdown" aria-expanded="false"><i class="fa-solid fa-right-to-bracket me-2"></i>
                            Login
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="loginDropdown">
                            <li>
                                <a class="dropdown-item" href="redirectToAdminLogin"><i class="fa-solid fa-user-tie me-2"></i>Admin Login</a>
                            </li>
                            <li>
                              <a class="dropdown-item" href="redirectToMilkSupplierLogin"><i class="fa-solid fa-bottle-droplet me-2"></i> Milk Supplier Login</a>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <main class="flex-grow-2 d-flex" style="margin-top: 80px;">
        <div class="container-fluid farm-bg d-flex flex-column justify-content-center align-items-start w-100"
            style="padding-left: 60px">
            <h2 class="mb-4" style="max-width: 600px">Welcome to Farm Fresh</h2>
            <p style="max-width: 600px">
                Farm Fresh connects local farmers directly to customers.<br />
                Farmers supply fresh produce, our admin team ensures <br />quality and
                processing, and customers enjoy healthy, farm-to-table products.
            </p>
        </div>
    </main>

<section class="py-5 text-center my-5">
      <div class="container">
        <h2 class="mb-4 pb-5" style="color:#2e7d32;">Our Fresh Products</h2>
        <div class="row g-4" id="product-list-container">
          <!-- Products will be loaded here by JavaScript -->
        </div>
      </div>
    </section>

<section id="about" class="py-3" style="background: #f1f8e9;">
  <div class="container">
    <div class="row align-items-center mb-5">
  <div class="col-md-12">
    <h2 class="mb-4 text-center">Our Story</h2>
    <p>
      At <strong>Farm Fresh</strong>, we bring you the freshest dairy products by partnering directly with dedicated farmers. 
      These hardworking farmers care deeply for their cows, providing high-quality milk that we collect and deliver straight to your home, preserving its natural taste and nutrition.
    </p>
    <p>
      Our farmers follow ethical and sustainable practices, ensuring the wellbeing of the animals and the quality of every drop. 
      Farm Fresh acts as the bridge between these farmers and you, so you can enjoy fresh, pure milk with peace of mind.
    </p>
    <p>
      Every product reflects our commitment to health, freshness, and the wholesome goodness of nature-straight from trusted hands to your table.
    </p>
  </div>
</div>


    <div id="videoCarousel" class="carousel slide custom-carousel" data-bs-interval="false">
      <div class="carousel-inner">

        <div class="carousel-item active">
          <video class="video-slide" autoplay muted playsinline>
            <source src="https://www.pexels.com/download/video/5914482/" type="video/mp4">
          </video>
        </div>

        <div class="carousel-item">
          <video class="video-slide" autoplay muted playsinline>
            <source src="https://www.pexels.com/download/video/32578172/" type="video/mp4">
          </video>
        </div>

        <div class="carousel-item">
          <video class="video-slide" autoplay muted playsinline>
            <source src="https://www.pexels.com/download/video/8064118/" type="video/mp4">
          </video>
        </div>

         <div class="carousel-item">
          <video class="video-slide" autoplay muted playsinline>
            <source src="https://www.pexels.com/download/video/8064120/" type="video/mp4">
          </video>
        </div>

        <div class="carousel-item">
          <video class="video-slide" autoplay muted playsinline>
            <source src="https://www.pexels.com/download/video/8064126/" type="video/mp4">
          </video>
        </div>
       
        <div class="carousel-item">
          <video class="video-slide" autoplay muted playsinline>
            <source src="https://www.pexels.com/download/video/8064133/" type="video/mp4">
          </video>
        </div>
        <div class="carousel-item">
          <video class="video-slide" autoplay muted playsinline>
            <source src="https://www.pexels.com/download/video/855303/" type="video/mp4">
          </video>
        </div>
      </div>

      <button class="carousel-control-prev" type="button" data-bs-target="#videoCarousel" data-bs-slide="prev">
        <span class="carousel-control-prev-icon"></span>
      </button>
      <button class="carousel-control-next" type="button" data-bs-target="#videoCarousel" data-bs-slide="next">
        <span class="carousel-control-next-icon"></span>
      </button>
    </div>

  </div>
</section>

      
<section id="health-benefits" class="py-5">
  <div class="container text-center">
    <h2 class="mb-4" style="color:#2e7d32;">Health Benefits of Milk</h2>
    <p class="mb-5">Our milk is not only fresh but packed with nutrients to keep you healthy and strong.</p>
    
    <div class="row g-4">
      <div class="col-md-4">
        <div class="p-4 border rounded shadow-sm h-100">       
            <img src="https://img.freepik.com/premium-vector/calcium-bones-line-icon-vector_116137-8271.jpg" alt="Strong Bones" width="80" class="mb-3">
          <h5>&#128170; Strong Bones</h5>
          <p>Rich in calcium and vitamin D for healthy bones and teeth.</p>
        </div>
      </div>

      <div class="col-md-4">
        <div class="p-4 border rounded shadow-sm h-100">
            <img src="https://img.freepik.com/premium-vector/vital-energy-charge-active-girl-activate-recharging-vitality-battery-healthy-body-level-boost-concept-tiredness-woman-business-worker_81894-14287.jpg" 
               alt="Energy Boost" width="80" class="mb-3">
          <h5>&#9889; Energy Boost</h5>
          <p>High-quality protein provides strength and stamina for your daily activities.</p>
        </div>
      </div>

      <div class="col-md-4">
        <div class="p-4 border rounded shadow-sm h-100">
          <img src="https://static.vecteezy.com/system/resources/previews/019/582/392/original/healthy-heart-cartoon-png.png" alt="Heart Health" width="70" class="mb-3">
          <h5>&#10084;&#65039; Heart Health</h5>
          <p>Contains essential nutrients that support overall heart and body wellness.</p>
        </div>
      </div>

    </div>
  </div>
</section>


    <section class="py-5" style="background: #f1f8e9;">
        <div class="container text-center">
            <h2 class="mb-4">Why Choose Farm Fresh?</h2>
            <div class="row">
                <div class="col-md-4">
                    <i class="fa-solid fa-leaf fa-2x text-success mb-2"></i>
                    <h5>100% Natural</h5>
                    <p>No preservatives, no chemicals -  just fresh products from the farm.</p>
                </div>
                <div class="col-md-4">
                    <i class="fa-solid fa-truck fa-2x text-primary mb-2"></i>
                    <h5>Fast Delivery</h5>
                    <p>Farm-to-doorstep delivery to keep your products fresh.</p>
                </div>
                <div class="col-md-4">
                    <i class="fa-solid fa-people-group fa-2x text-warning mb-2"></i>
                    <h5>Supporting Farmers</h5>
                    <p>Every purchase directly supports local farmers.</p>
                </div>
            </div>
        </div>
    </section>

    <section class="py-5 text-center">
        <div class="container">
            <h2 class="mb-4" style="color:#2e7d32;">What Our Customers Say</h2>
            <div class="row">
                <div class="col-md-4">
                    <blockquote class="blockquote">
                        <p>"The freshest milk I've ever had. My kids love it!"</p>
                        <footer class="blockquote-footer">Priya, Chennai</footer>
                    </blockquote>
                </div>
                <div class="col-md-4">
                    <blockquote class="blockquote">
                        <p>"Fast delivery and top-notch quality. Highly recommended."</p>
                        <footer class="blockquote-footer">Arjun, Bangalore</footer>
                    </blockquote>
                </div>
                <div class="col-md-4">
                    <blockquote class="blockquote">
                        <p>"Feels good knowing my purchase supports local farmers."</p>
                        <footer class="blockquote-footer">Meera, Pune</footer>
                    </blockquote>
                </div>
            </div>
        </div>
    </section>

    <section class="text-center py-5" style="background:#f9fbe7;">
        <h2>Join the Farm Fresh Family</h2>
        <p>Whether you are a farmer or a customer, we welcome you to be part of our journey.</p>
        <a href="redirectToCustomerRegister" class="btn btn-success me-3">Register as Customer</a>
        <a href="redirectToCustomerLogin" class="btn btn-outline-dark">Order Now</a>
    </section>

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
        integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r"
        crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.min.js"
        integrity="sha384-7qAoOXltbVP82dhxHAUje59V5r2YsVfBafyUDxEdApLPmcdhBPg1DKg1ERo0BZlK"
        crossorigin="anonymous"></script>
        <script src="js/index.js"></script>
</body>
</html>
