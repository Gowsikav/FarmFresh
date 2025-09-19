package com.xworkz.farmfresh.controller;

import com.xworkz.farmfresh.dto.AdminDTO;
import com.xworkz.farmfresh.dto.SupplierDTO;
import com.xworkz.farmfresh.service.AdminService;
import com.xworkz.farmfresh.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/")
public class SupplierController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private SupplierService supplierService;

    public SupplierController() {
        log.info("SupplierController constructor");
    }

    @GetMapping("/redirectToMilkSuppliersList")
    public String getMilkSupplierList(@RequestParam String email, Model model) {
        log.info("getMilkSupplierList method in supplier controller");

        AdminDTO adminDTO = adminService.getAdminDetailsByEmail(email);
        model.addAttribute("dto", adminDTO);
        List<SupplierDTO> list = supplierService.getAllSuppliers();
        model.addAttribute("milkSuppliers", list);
        return "MilkSuppliers";
    }

    @PostMapping("/addMilkSupplier")
    public String addMilkSupplier(@Valid SupplierDTO supplierDTO, BindingResult bindingResult, @RequestParam String adminEmail, Model model) {
        log.info("addMilkSupplier in supplierController");
        if (bindingResult.hasErrors()) {
            log.warn("fields has error");
            bindingResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getField() + "->" + fieldError.getDefaultMessage())
                    .forEach(System.out::println);
            model.addAttribute("supplier", supplierDTO);
            model.addAttribute("error", "Details not saved");
            return getMilkSupplierList(adminEmail, model);
        }
        if (supplierService.addSupplier(supplierDTO, adminEmail)) {
            log.info("supplier added");
            model.addAttribute("success", "Supplier details saved");
        } else {
            log.warn("supplier not added");
            model.addAttribute("error", "Supplier details saved");
        }
        return getMilkSupplierList(adminEmail, model);
    }

    @PostMapping("/updateMilkSupplier")
    public String editSupplier(@Valid SupplierDTO supplierDTO, BindingResult bindingResult, @RequestParam String adminEmail, Model model) {
        log.info("editSupplier method in supplierController");
        if (bindingResult.hasErrors()) {
            log.warn("fields has error");
            bindingResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getField() + "->" + fieldError.getDefaultMessage())
                    .forEach(System.out::println);
            model.addAttribute("supplier", supplierDTO);
            model.addAttribute("error", "Details not saved");
            return getMilkSupplierList(adminEmail, model);
        }
        if (supplierService.editSupplierDetails(supplierDTO, adminEmail)) {
            log.info("supplier updated");
            model.addAttribute("success", "Supplier details updated");
        } else {
            log.warn("supplier not updated");
            model.addAttribute("error", "Supplier details not updated");
        }
        return getMilkSupplierList(adminEmail, model);
    }

    @GetMapping("/deleteMilkSupplier")
    public String deleteSupplier(@RequestParam String email, @RequestParam String adminEmail, Model model) {
        log.info("deleteSupplier in supplier controller");
        if (supplierService.deleteSupplierDetails(email, adminEmail)) {
            log.info("supplier deleted");
            model.addAttribute("success", "Supplier details deleted");
        } else {
            log.warn("supplier not deleted");
            model.addAttribute("error", "Supplier details deleted");
        }
        return getMilkSupplierList(adminEmail, model);
    }
}
