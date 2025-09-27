package com.xworkz.farmfresh.restcontroller;

import com.xworkz.farmfresh.service.ProductPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
public class ProductListRestController {

    @Autowired
    private ProductPriceService productPriceService;

    public ProductListRestController()
    {
        log.info("ProductListRestController constructor");
    }

    @GetMapping("/productList")
    public ResponseEntity<List<String>> getProductList()
    {
        log.info("getProductList method in productListRestController");
        List<String> list=productPriceService.productList();
        return ResponseEntity.ok(list);
    }
}
