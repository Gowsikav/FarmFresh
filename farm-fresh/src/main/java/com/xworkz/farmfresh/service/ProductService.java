package com.xworkz.farmfresh.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xworkz.farmfresh.dto.Product;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private static final String JSON_FILE = "E:/farm-fresh/product.json";
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Product> getProducts() throws IOException {
        File file = new File(JSON_FILE);
        if (!file.exists()) {
            file.createNewFile();
            mapper.writeValue(file, new ArrayList<Product>());
        }
        return mapper.readValue(file, new TypeReference<List<Product>>() {});
    }

    public void saveProducts(List<Product> products) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(JSON_FILE), products);
    }

    public int getNextId() throws IOException {
        List<Product> products = getProducts();
        return products.stream().mapToInt(Product::getId).max().orElse(0) + 1;
    }

    public void addProduct(Product product) throws IOException {
        List<Product> products = getProducts();
        product.setId(getNextId());
        products.add(product);
        saveProducts(products);
    }

    public void deleteProduct(int id) throws IOException {
        List<Product> products = getProducts();
        products.removeIf(p -> p.getId() == id);
        saveProducts(products);
    }

    public Product getProductById(int id) throws IOException {
        return getProducts().stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public void updateProduct(Product updatedProduct) throws IOException {
        List<Product> products = getProducts();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == updatedProduct.getId()) {
                products.set(i, updatedProduct);
                break;
            }
        }
        saveProducts(products);
    }
}
