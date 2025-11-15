package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.dto.AdminDTO;
import com.xworkz.farmfresh.dto.ProductPriceDTO;
import com.xworkz.farmfresh.entity.ProductPriceEntity;
import com.xworkz.farmfresh.repository.ProductPriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductPriceServiceImpl implements ProductPriceService{

    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Autowired
    private AdminService adminService;

    public ProductPriceServiceImpl()
    {
        log.info("ProductPriceService impl constructor");
    }

    @Override
    public boolean saveProduct(ProductPriceDTO productPriceDTO,String adminEmail) {
        log.info("save product method in ProductPriceServiceImpl");
        AdminDTO adminDTO=adminService.getAdminDetailsByEmail(adminEmail);

        ProductPriceEntity productPriceEntity=new ProductPriceEntity();
        BeanUtils.copyProperties(productPriceDTO,productPriceEntity);
        productPriceEntity.setCreatedAt(LocalDateTime.now());
        productPriceEntity.setCreatedBy(adminDTO.getAdminName());
        productPriceEntity.setIsActive(true);

        return productPriceRepository.saveProduct(productPriceEntity);
    }

    @Override
    public List<ProductPriceDTO> getAllDetails() {
        log.info("getAllDetails method in product price service");
        List<ProductPriceEntity> productPriceEntities=productPriceRepository.getAllDetails();
        List<ProductPriceDTO> productPriceDTOS=new ArrayList<>();
        productPriceEntities.forEach(entity -> {
            ProductPriceDTO productPriceDTO=new ProductPriceDTO();
            BeanUtils.copyProperties(entity,productPriceDTO);
            productPriceDTOS.add(productPriceDTO);
        });
        return productPriceDTOS;
    }

    @Override
    public boolean updateProduct(ProductPriceDTO productPriceDTO, String adminEmail) {
        log.info("update product method in product price service");
        AdminDTO adminDTO=adminService.getAdminDetailsByEmail(adminEmail);

        ProductPriceEntity productPriceEntity=new ProductPriceEntity();
        BeanUtils.copyProperties(productPriceDTO,productPriceEntity);
        productPriceEntity.setUpdatedAt(LocalDateTime.now());
        productPriceEntity.setUpdatedBy(adminDTO.getAdminName());
        return productPriceRepository.updateProduct(productPriceEntity);
    }

    @Override
    public boolean deleteProduct(Integer productId) {
        log.info("deleteProduct method in product price service");

        return productPriceRepository.deleteProduct(productId);
    }

    @Override
    public List<String> productListForBuy() {
        log.info("productList method in product price service");
        List<ProductPriceEntity> productPriceEntities=productPriceRepository.getAllDetails();
        List<String> products=new ArrayList<>();
        productPriceEntities.forEach(entity -> {
            ProductPriceDTO productPriceDTO=new ProductPriceDTO();
            BeanUtils.copyProperties(entity,productPriceDTO);
            if(productPriceDTO.getProductType().equalsIgnoreCase("Buy")) {
                products.add(productPriceDTO.getProductName());
            }
        });
        return products.stream().sorted().collect(Collectors.toList());
    }

    @Override
    public boolean checkProductName(String product) {
        log.info("checkProductName method in product price service");
        List<ProductPriceEntity> productPriceEntities=productPriceRepository.getAllDetails();
        return productPriceEntities.stream()
                .anyMatch(entity -> entity.getProductName().equalsIgnoreCase(product));
    }

    @Override
    public Double getPriceForType(String type) {
        log.info("getPriceForType method in product price service");
        List<ProductPriceEntity> productPriceEntities=productPriceRepository.getAllDetails();
        Double price = productPriceEntities.stream()
                .filter(e -> e.getProductName().equalsIgnoreCase(type))
                .map(ProductPriceEntity::getPrice)
                .findFirst()
                .orElse(0.0D); //
        return price;
    }
}
