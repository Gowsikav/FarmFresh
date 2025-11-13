package com.xworkz.farmfresh.controller;

import com.xworkz.farmfresh.dto.AdminDTO;
import com.xworkz.farmfresh.dto.PaymentDetailsDTO;
import com.xworkz.farmfresh.dto.SupplierBankDetailsDTO;
import com.xworkz.farmfresh.dto.SupplierDTO;
import com.xworkz.farmfresh.service.*;
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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
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

    @Autowired
    private CollectMilkService collectMilkService;

    @Autowired
    private PaymentNotificationService paymentNotificationService;

    @Autowired
    private SupplierImportService supplierImportService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${supplier-upload.path}")
    private String uploadPath;

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
    public String checkOtpForSupplierLogin(@RequestParam String email, @RequestParam String otp, Model model, HttpSession session)
    {
        log.info("checkOtpForSupplierLogin method in supplier controller");
        try {
            if (supplierService.checkOTPForSupplierLogin(email, otp)) {
                model.addAttribute("errorMessage", "successfully login");
                model.addAttribute("success","success");
                session.setAttribute("userRole", "SUPPLIER");
                return "redirect:/redirectToSupplierDashboard?email="+email;
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
        LocalDate lastDate=collectMilkService.getLastCollectedDate(supplierDTO.getSupplierId());
        if(lastDate==null)
            model.addAttribute("lastCollectedDate","Yet to collect");
        else model.addAttribute("lastCollectedDate",lastDate);

        Double litres=collectMilkService.getTotalLitre(supplierDTO.getSupplierId());
        if(litres==null)
            model.addAttribute("totalLitres",0);
        else model.addAttribute("totalLitres",litres);

        Double amount=paymentNotificationService.getTotalAmountPaid(supplierDTO.getSupplierId());
        if(amount==null)
            model.addAttribute("totalAmountPaid","No Collection");
        else model.addAttribute("totalAmountPaid","Rs."+amount+"/-");
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
    public String supplierLogout(@RequestParam String email,HttpSession session)
    {
        log.info("supplier log out");
        session.invalidate();
        return "redirect:/redirectToIndex";
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

    @GetMapping("/redirectToPaymentStatus")
    public String getPaymentStatus(@RequestParam String email,Model model)
    {
        log.info("getPayment status in supplier controller");
        SupplierDTO supplierDTO=supplierService.getDetailsByEmail(email);
        List<PaymentDetailsDTO> list=paymentNotificationService.getPaymentDetailsForSupplier(supplierDTO);
        model.addAttribute("dto",supplierDTO);
        model.addAttribute("paymentList",list);
        return "SupplierPaymentStatus";
    }

    @GetMapping("/generateInvoiceForSupplier")
    public void getInvoiceForSupplier(@RequestParam String periodStart, @RequestParam String periodEnd,
                                      @RequestParam String paymentDate, @RequestParam Integer supplierId,@RequestParam Integer paymentId, HttpServletResponse response)
    {
        log.info("getInvoiceForSupplier method in supplier controller");
        supplierService.downloadInvoicePdf(supplierId,paymentId, LocalDate.parse(periodStart), LocalDate.parse(periodEnd), LocalDate.parse(paymentDate), response);
    }

    @PostMapping("/importForSupplierRegister")
    public String uploadFile(@RequestParam("file") MultipartFile file,@RequestParam String email, Model model) {
        log.info("uploadFile method in SupplierController");
        AdminDTO adminDTO = adminService.getAdminDetailsByEmail(email);
        model.addAttribute("dto", adminDTO);
        try {
            if (file.isEmpty()) {
                model.addAttribute("error", "Please choose a file to upload.");
                return getMilkSupplierList(email,1,10,model);
            }

            File directory = new File(uploadPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadPath, fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            List<SupplierDTO> invalidRows=supplierImportService.importSuppliersFromExcel(filePath.toString(),email);
            if(invalidRows.isEmpty())
            {
                model.addAttribute("success", "All records are saved");
                return getMilkSupplierList(email,1,10,model);
            }else{
                model.addAttribute("invalidRows",invalidRows);
                model.addAttribute("error",invalidRows.size()+" records are not saved");
            }
            log.info("File uploaded successfully to: {}", filePath.toAbsolutePath());
        } catch (Exception e) {
            log.error("Upload failed: " + e.getMessage());
            model.addAttribute("error", "Error saving file: " + e.getMessage());
        }
        controllerHelper.addNotificationData(model,email);
        return "MilkSuppliers";
    }

}
