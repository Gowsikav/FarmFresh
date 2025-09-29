package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.dto.ProductPriceDTO;

import java.util.List;

public interface ProductPriceService {

    boolean saveProduct(ProductPriceDTO productPriceDTO,String adminEmail);
    List<ProductPriceDTO> getAllDetails();
    boolean updateProduct(ProductPriceDTO productPriceDTO,String adminEmail);
    boolean deleteProduct(Integer productId);
    List<String> productListForBuy();
}
