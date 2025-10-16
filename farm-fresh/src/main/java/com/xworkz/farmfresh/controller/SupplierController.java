package com.xworkz.farmfresh.controller;

import com.xworkz.farmfresh.dto.AdminDTO;
import com.xworkz.farmfresh.dto.SupplierBankDetailsDTO;
import com.xworkz.farmfresh.dto.SupplierDTO;
import com.xworkz.farmfresh.service.AdminService;
import com.xworkz.farmfresh.service.SupplierService;
import com.xworkz.farmfresh.util.CommonControllerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/")
@PropertySource("classpath:application.properties")
public class SupplierController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private CommonControllerHelper controllerHelper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public SupplierController() {
        log.info("SupplierController constructor");
    }

    @GetMapping("/redirectToMilkSuppliersList")
    public String getMilkSupplierList(@RequestParam String email,@RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int size, Model model) {
        log.info("getMilkSupplierList method in supplier controller");

        AdminDTO adminDTO = adminService.getAdminDetailsByEmail(email);
        model.addAttribute("dto", adminDTO);
        List<SupplierDTO> list = supplierService.getAllSuppliers(page,size);
        long totalSuppliers = adminService.getSupplierCount();
        int totalPages = (int) Math.ceil((double) totalSuppliers / size);
        model.addAttribute("milkSuppliers", list);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", totalPages);
        controllerHelper.addNotificationData(model,email);
        return "MilkSuppliers";
    }

    @GetMapping("/searchSuppliers")
    public String getSearchSuppliers(@RequestParam String keyword,@RequestParam String email, Model model)
    {
        log.info("getSearchSuppliers method in supplier controller");
        AdminDTO adminDTO = adminService.getAdminDetailsByEmail(email);
        model.addAttribute("dto", adminDTO);
        List<SupplierDTO> list = supplierService.searchSuppliers(keyword);
        if(list.isEmpty())
            model.addAttribute("error","Data not found");
        model.addAttribute("milkSuppliers", list);
        model.addAttribute("currentPage", 1);
        model.addAttribute("totalPages", 1);
        controllerHelper.addNotificationData(model,email);
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
            return getMilkSupplierList(adminEmail, 1,10,model);
        }
        if (supplierService.addSupplier(supplierDTO, adminEmail)) {
            log.info("supplier added");
            model.addAttribute("success", "Supplier details saved");
        } else {
            log.warn("supplier not added");
            model.addAttribute("error", "Supplier details saved");
        }
        return getMilkSupplierList(adminEmail, 1,10,model);
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
            return getMilkSupplierList(adminEmail, 1,10,model);
        }
        if (supplierService.editSupplierDetails(supplierDTO, adminEmail)) {
            log.info("supplier updated");
            model.addAttribute("success", "Supplier details updated");
        } else {
            log.warn("supplier not updated");
            model.addAttribute("error", "Supplier details not updated");
        }
        return getMilkSupplierList(adminEmail, 1,10,model);
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
        return getMilkSupplierList(adminEmail, 1,10,model);
    }

    @GetMapping("/redirectToMilkSupplierLogin")
    public String getMilkSupplierLogin(@RequestParam(required = false) String email)
    {
        log.info("getMilkSupplierLogin method in supplier controller");
        if(email!=null)
        {
           supplierService.setOtpAndTimeNull(email);
        }
        return "MilkSupplierLogin";
    }

    @GetMapping("/sendOTPToSupplier")
    public String supplierLoginOtp(@RequestParam String email,Model model)
    {
        log.info("supplierLoginOtp method in supplier controller");
        if(supplierService.sendOtpTOSupplierForLogin(email))
        {
            model.addAttribute("successMessage","OTP send to your Email");
        }else {
            model.addAttribute("errorMessage","OTP Not Sent");
        }
        model.addAttribute("email",email);
        return "MilkSupplierLogin";
    }

    @PostMapping("/verifySupplierOTP")
    public String checkOtpForSupplierLogin(@RequestParam String email,@RequestParam String otp,Model model)
    {
        log.info("checkOtpForSupplierLogin method in supplier controller");
        try {
            if (supplierService.checkOTPForSupplierLogin(email, otp)) {
                model.addAttribute("errorMessage", "successfully login");
                model.addAttribute("dto",supplierService.getDetailsByEmail(email));
                model.addAttribute("success","success");
                return "SupplierDashboard";
            } else {
                model.addAttribute("errorMessage", "not login");
            }
        }catch (RuntimeException e)
        {
            model.addAttribute("error",e.getMessage());
            model.addAttribute("email",email);
        }
        return "MilkSupplierLogin";
    }

    @GetMapping("/redirectToSupplierDashboard")
    public String getSupplierDashboardPage(@RequestParam String email,Model model)
    {
        log.info("getSupplierDashboardPage method in supplier controller");
        SupplierDTO supplierDTO=supplierService.getDetailsByEmail(email);
        model.addAttribute("dto",supplierDTO);
        return "SupplierDashboard";
    }

    @GetMapping("/redirectToUpdateSupplierProfile")
    public String getUpdateProfilePage(@RequestParam String email,Model model)
    {
        log.info("getUpdateProfilePage method in supplier controller");
        model.addAttribute("dto",supplierService.getDetailsByEmail(email));
        return "UpdateSupplierProfile";
    }

    @PostMapping("updateSupplierProfile")
    public String updateSupplierProfile(@Valid SupplierDTO supplierDTO, BindingResult bindingResult, @RequestParam(required = false)MultipartFile profilePicture, Model model)
    {
        log.info("updateSupplierProfile method in supplier controller");
        if(bindingResult.hasErrors())
        {
            log.error("fields has error");
            bindingResult.getFieldErrors().stream().map(e->e.getField()+" -> "+e.getDefaultMessage())
                    .forEach(log::error);
            model.addAttribute("dto",supplierDTO);
            model.addAttribute("errorMessage","Invalid details");
            return "UpdateSupplierProfile";
        }
        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                byte[] bytes = profilePicture.getBytes();
                String fileName = supplierDTO.getFirstName() + "_" + System.currentTimeMillis() + "_" + profilePicture.getOriginalFilename();
                Path path = Paths.get(uploadDir ,fileName);
                Files.write(path, bytes);
                supplierDTO.setProfilePath(path.getFileName().toString());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        } else {
            log.warn("No new profile picture uploaded.");
        }
        if(supplierService.updateSupplierDetailsBySupplier(supplierDTO))
        {
            return getSupplierDashboardPage(supplierDTO.getEmail(),model);
        }else {
            model.addAttribute("errorMessage","Details not updated");
            model.addAttribute("dto",supplierDTO);
        }
        return "UpdateSupplierProfile";
    }

    @GetMapping("supplierLogout")
    public String supplierLogout(@RequestParam String email)
    {
        log.info("supplier log out");
        return "index";
    }

    @GetMapping("redirectToUpdateSupplierBankDetails")
    public String redirectToUpdateSupplierBankDetailsPage(@RequestParam String email,Model model)
    {
        log.info("redirectToUpdateSupplierBankDetailsPage method in supplier controller");
        model.addAttribute("dto",supplierService.getDetailsByEmail(email));
        return "UpdateSupplierBankDetails";
    }


    @PostMapping("/updateBankDetails")
    public String supplierUpdateBankDetails(@Valid SupplierBankDetailsDTO supplierBankDetailsDTO,BindingResult bindingResult,@RequestParam String email,Model model)
    {
        log.info("supplierUpdateBankDetailsPage method in supplier controller");
        if(bindingResult.hasErrors())
        {
            log.error("fields has error");
            bindingResult.getFieldErrors().stream().map(e->e.getField()+" -> "+e.getDefaultMessage())
                    .forEach(System.out::println);
            model.addAttribute("bank",supplierBankDetailsDTO);
            model.addAttribute("dto.email",email);
            return "UpdateSupplierBankDetails";
        }
        if(supplierService.updateSupplierBankDetails(supplierBankDetailsDTO,email))
        {
            log.info("bank details updated");
            return getSupplierDashboardPage(email,model);
        }else {
            model.addAttribute("bank",supplierBankDetailsDTO);
            model.addAttribute("dto.email",email);
        }
        return "UpdateSupplierBankDetails";
    }

    @PostMapping("/updateSupplierBankDetailsByAdmin")
    public String updateSupplierBankDetailsByAdmin(@Valid SupplierBankDetailsDTO supplierBankDetailsDTO,BindingResult bindingResult,
                                                   @RequestParam String adminEmail,@RequestParam String email,Model model)
    {
        log.info("updateSupplierBankDetailsByAdmin method in supplier controller");
        if(bindingResult.hasErrors())
        {
            log.error("fields has error");
            bindingResult.getFieldErrors().stream().map(e->e.getField()+" -> "+e.getDefaultMessage())
                    .forEach(System.out::println);
            model.addAttribute("error","Bank details not updated");
            return getMilkSupplierList(adminEmail,1,10,model);
        }
        if(supplierService.updateSupplierBankDetailsByAdmin(supplierBankDetailsDTO,email,adminEmail))
        {
            model.addAttribute("success","Bank details updated");
        }else {
            model.addAttribute("error","Bank details not updated");
        }
        return getMilkSupplierList(adminEmail,1,10,model);
    }
}
