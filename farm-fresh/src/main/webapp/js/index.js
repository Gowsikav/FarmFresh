document.addEventListener('DOMContentLoaded', function () {
            fetch('json/product-image.json')
                .then(response => response.json())
                .then(products => {
                    const container = document.getElementById('product-list-container');
                    let productsHTML = '';
                    products.forEach(product => {
                        productsHTML += `
                            <div class="col-md-3 col-sm-6">
                              <div class="card h-100 shadow-sm custom-card">
                                <div class="ratio ratio-1x1">
                                  <img src="images/${product.image}" class="card-img-top object-fit-cover rounded" alt="${product.name}">
                                </div>
                                <div class="card-body">
                                  <h5 class="card-title">${product.name}</h5>
                                 <p class="card-text">${product.description}</p>

                                </div>
                              </div>
                            </div>
                        `;
                    });
                    container.innerHTML = productsHTML;
                })
                .catch(error => {
                    console.error('Error fetching products:', error);
                    const container = document.getElementById('product-list-container');
                    container.innerHTML = '<p class="text-danger text-center">Could not load products at this time.</p>';
                });
        });

const carouselElement = document.querySelector('#videoCarousel');
  const carousel = new bootstrap.Carousel(carouselElement, { interval: false });

  // Move to next video when one ends
  document.querySelectorAll('#videoCarousel video').forEach(video => {
    video.addEventListener('ended', () => {
      carousel.next();
    });
  });

  // Play the active video on slide change
  carouselElement.addEventListener('slid.bs.carousel', () => {
    document.querySelectorAll('#videoCarousel video').forEach(v => v.pause());
    const activeVideo = carouselElement.querySelector('.carousel-item.active video');
    if (activeVideo) {
      activeVideo.play();
    }
  });