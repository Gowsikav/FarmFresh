package com.xworkz.farmfresh.restcontroller;

import com.xworkz.farmfresh.dto.Product;
import com.xworkz.farmfresh.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@PropertySource("classpath:application.properties")
public class ProductRestController {

    @Autowired
    private ProductService service;

    @Value("${product.image-upload}")
    private String IMAGE_DIR;

    @GetMapping
    public List<Product> getProducts() throws IOException {
        return service.getProducts();
    }

    @PostMapping("/upload")
    public String uploadProduct(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        if (imageFile.isEmpty()) return "No file selected";

        File file = new File(IMAGE_DIR + imageFile.getOriginalFilename());
        imageFile.transferTo(file);

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setImage(imageFile.getOriginalFilename());

        service.addProduct(product);

        return "Product added successfully!";
    }

    @PostMapping("/update")
    public String updateProduct(
            @RequestParam int id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

        Product product = service.getProductById(id);
        if (product == null) return "Product not found";

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);

        if (imageFile != null && !imageFile.isEmpty()) {
            File file = new File(IMAGE_DIR + imageFile.getOriginalFilename());
            imageFile.transferTo(file);
            product.setImage(imageFile.getOriginalFilename());
        }

        service.updateProduct(product);
        return "Product updated successfully!";
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable int id) throws IOException {
        Product product = service.getProductById(id);
        if (product == null) return "Product not found";

        File file = new File(IMAGE_DIR + product.getImage());
        if (file.exists()) file.delete();

        service.deleteProduct(id);
        return "Product deleted successfully!";
    }
}