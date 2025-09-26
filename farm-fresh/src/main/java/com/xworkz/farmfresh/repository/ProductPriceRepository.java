package com.xworkz.farmfresh.repository;

import com.xworkz.farmfresh.entity.ProductPriceEntity;

import java.util.List;

public interface ProductPriceRepository {

    boolean saveProduct(ProductPriceEntity entity);
    List<ProductPriceEntity> getAllDetails();
    boolean updateProduct(ProductPriceEntity productPriceEntity);
    boolean deleteProduct(Integer productId);
}
