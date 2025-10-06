package com.xworkz.farmfresh.restcontroller;

import com.xworkz.farmfresh.dto.SupplierDTO;
import com.xworkz.farmfresh.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/")
public class CollectMilkRestController {

    @Autowired
    private SupplierService supplierService;

    public CollectMilkRestController()
    {
        log.info("CollectMilkRestController constructor");
    }

    @GetMapping("/getSupplierByPhone")
    public ResponseEntity<SupplierDTO> getDetails(@RequestParam String phone)
    {
        log.info("getDetails method in CollectMilkRestController");
        return ResponseEntity.ok(supplierService.getSupplierDetails(phone));

    }
}
