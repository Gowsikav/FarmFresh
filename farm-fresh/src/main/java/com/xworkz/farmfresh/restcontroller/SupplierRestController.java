package com.xworkz.farmfresh.restcontroller;

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
public class SupplierRestController {

    @Autowired
    private SupplierService supplierService;

    public SupplierRestController()
    {
        log.info("SupplierRestController constructor");
    }

    @GetMapping("/checkSupplierEmail")
    public ResponseEntity<Boolean> checkEmailAvailability(@RequestParam String email)
    {
        log.info("checkEmailAvailability method in SupplierRestController");
        return ResponseEntity.ok(supplierService.checkEmail(email));
    }

    @GetMapping("/checkPhone")
    public ResponseEntity<Boolean> checkPhoneNumberAvailability(@RequestParam String phoneNumber)
    {
        log.info("checkPhoneNumberAvailability method in SupplierRestController");

        return ResponseEntity.ok(supplierService.checkPhonNumber(phoneNumber));
    }
}
