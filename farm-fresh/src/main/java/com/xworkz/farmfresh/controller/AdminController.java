package com.xworkz.farmfresh.controller;

import com.xworkz.farmfresh.dto.AdminDTO;
import com.xworkz.farmfresh.dto.PaymentDetailsDTO;
import com.xworkz.farmfresh.service.AdminService;
import com.xworkz.farmfresh.service.CollectMilkService;
import com.xworkz.farmfresh.service.PaymentNotificationService;
import com.xworkz.farmfresh.service.SupplierService;
import com.xworkz.farmfresh.util.CommonControllerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/")
@PropertySource("classpath:application.properties")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PaymentNotificationService notificationService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private CommonControllerHelper controllerHelper;

    @Autowired
    private CollectMilkService collectMilkService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public AdminController() {
        log.info("AdminController constructor");
    }

    @GetMapping("/redirectToIndex")
    public String getHomePage()
    {
        System.out.println("redirected to Home page");
        return "index";
    }

    @GetMapping("/redirectToAdminLogin")
    public String getAdminLogin(Model model) {
        log.info("getAdminLogin method in AdminController");
        return "AdminLogin";
    }

    @PostMapping("/adminLogin")
    public String checkPasswordForAdminLogin(@RequestParam String email, @RequestParam String password,
                                             Model model, HttpSession session, HttpServletResponse response,HttpServletRequest request) {
        log.info("checkPasswordForAdminLogin method in adminController");
        log.info("Email: {} password: {}" ,email , password);
        AdminDTO adminDTO=null;
        try {
            adminDTO= adminService.checkAdminLoginPassword(email, password);
            if (adminDTO != null) {
                session.setAttribute("userRole", "ADMIN");
                session.setAttribute("adminEmail",email);
                Cookie cookie = new Cookie("adminEmail", email);
                cookie.setMaxAge(7 * 24 * 60 * 60);
                cookie.setPath("/");
                response.addCookie(cookie);
                return getDashboard(session,model,request);
            }else {
                model.addAttribute("errorMessage", "Account not present");
                return "AdminLogin";
            }
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage",e.getMessage());
        }
        return "AdminLogin";
    }

    @GetMapping("/redirectToUpdateAdminProfile")
    public String redirectToUpdate(HttpSession session, Model model) {
        log.info("redirectTo update page in adminController");
        String email = (String) session.getAttribute("adminEmail");
        AdminDTO adminDTO = adminService.getAdminDetailsByEmail(email);
        model.addAttribute("dto", adminDTO);
        return "UpdateAdminProfile";
    }

    @GetMapping("/redirectToAdminDashboard")
    public String getDashboard(HttpSession session, Model model, HttpServletRequest request) {
        log.info("getDashboard method in admin controller");
        String email = (String) session.getAttribute("adminEmail");
        if (email == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if ("adminEmail".equals(c.getName())) {
                        email = c.getValue();
                        session.setAttribute("adminEmail", email);
                        break;
                    }
                }
            }
        }
        AdminDTO adminDTO = adminService.getAdminDetailsByEmail(email);
        model.addAttribute("dto", adminDTO);
        int count=adminService.getSupplierCount();
        model.addAttribute("suppliersCount",count);
        model.addAttribute("totalMilkCollected", collectMilkService.getTotalMilkCollected());
        model.addAttribute("recentCollections", collectMilkService.getRecentCollections());
        model.addAttribute("totalPayments", notificationService.getTotalPaymentsThisMonth());
        model.addAttribute("recentPayments", notificationService.getRecentPayments());
        model.addAttribute("pendingAmount", notificationService.totalPendingAmount());
        controllerHelper.addNotificationData(model,email);
        return "AdminDashboard";
    }

    @PostMapping("/updateAdminProfile")
    public String updateAdminProfile(
            @RequestParam("adminName") String adminName,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
            Model model,HttpSession session,HttpServletRequest request) {

        log.info("updateAdminProfile method in adminController");

        String profilePath=null;
        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                log.info(uploadDir);
                byte[] bytes = profilePicture.getBytes();
                String fileName = adminName + "_" + System.currentTimeMillis() + "_" + profilePicture.getOriginalFilename();
                Path path = Paths.get(uploadDir ,fileName);
                Files.write(path, bytes);
                profilePath = path.getFileName().toString();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        } else {
            log.warn("No new profile picture uploaded.");
        }
        if (adminService.updateAdminProfileByEmail(email, adminName, phoneNumber, profilePath)) {
            log.info("profile updated");
            return getDashboard(session,model,request);
        }
        model.addAttribute("errorMessage", "Invalid details");

        return redirectToUpdate(session,model);
    }

    @PostMapping("/forgotPassword")
    public String sendEmailForSetPassword(@RequestParam("email")String email,Model model)
    {
        log.info("sendEmailForSetPassword method in admin controller");
        if(adminService.sendMailToEmailForSetPassword(email))
        {
            log.info("Email send");
            model.addAttribute("successMessage","Email send to your mail");
        }else{
            log.error("Email not send");
            model.addAttribute("errorMessage","Email not send to your mail");
        }
        return "AdminLogin";
    }

    @GetMapping("/redirectToSetPassword")
    public String redirectToSetPassword(@RequestParam("email")String email, Model model)
    {
        log.info("redirectToSetPassword method in admin controller");
        model.addAttribute("email",email);
        return "SetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String email,@RequestParam String password,
                                @RequestParam String confirmPassword,Model model)
    {
        log.info("resetPassword method in admin controller");
        if(adminService.resetPasswordByEmail(email,password,confirmPassword))
        {
            log.info("password changed");
            model.addAttribute("email",email);
            model.addAttribute("successMessage","Account unblocked");
            return "AdminLogin";
        }else {
            log.error("password not changed");
            model.addAttribute("errorMessage","Password incorrect");
            return redirectToSetPassword(email,model);
        }
    }

    @GetMapping("/adminLogout")
    public String updateAdminLogout(Model model,HttpSession session,HttpServletRequest request)
    {
        log.info("updateAdminLogout method in admin controller");
        String email = (String) session.getAttribute("adminEmail");
        if(adminService.updateAdminLogoutTime(email))
        {
            log.info("logout time changed");
            session.invalidate();
            return "redirect:/redirectToIndex";
        }
        return getDashboard(session,model,request);
    }


    @GetMapping("/redirectToManageProducts")
    public String getManageProductPage(HttpSession session,Model model)
    {
        log.info("getManageProductPage method in adminController");
        String email = (String) session.getAttribute("adminEmail");
        AdminDTO adminDTO = adminService.getAdminDetailsByEmail(email);
        model.addAttribute("dto", adminDTO);
        return "ManageProducts";
    }

    @GetMapping("/supplierPaymentDetails")
    public String getSupplierPaymentDetails(@RequestParam Long notificationId,HttpSession session, Model model)
    {
        log.info("getSupplierPaymentDetails method in supplier controller");
        String email = (String) session.getAttribute("adminEmail");
        model.addAttribute("supplier",supplierService.getSupplierDetailsByNotificationId(notificationId));
        model.addAttribute("dto",adminService.getAdminDetailsByEmail(email));
        model.addAttribute("notificationId",notificationId);
        model.addAttribute("paymentAmount",notificationService.getAmountById(notificationId));
        model.addAttribute("milkList",collectMilkService.getAllDetailsBySupplier(notificationId));
        controllerHelper.addNotificationData(model,email);
        return "SupplierPayDetails";
    }

    @PostMapping("/payToSupplier")
    public String payToSupplier(HttpSession session,@RequestParam String supplierEmail,@RequestParam Long notificationId,
                                Model model,HttpServletRequest request)
    {
        log.info("pay to supplier method in supplier controller");
        String email = (String) session.getAttribute("adminEmail");
        if(notificationService.markAsReadForPayment(notificationId,supplierEmail,email))
        {
            return getDashboard(session,model,request);
        }
        model.addAttribute("errorMessage","Amount Not paid");
        return getSupplierPaymentDetails(notificationId,session,model);
    }

    @PostMapping("/requestSupplierBankDetails")
    public String requestForSupplierBankDetails(HttpSession session,@RequestParam String supplierEmail,@RequestParam Long notificationId,Model model)
    {
        log.info("requestForSupplierBankDetails method in admin Controller");
        String email = (String) session.getAttribute("adminEmail");
        if(supplierService.requestForSupplierBankDetails(supplierEmail))
        {
            model.addAttribute("dto",adminService.getAdminDetailsByEmail(email));
            model.addAttribute("successMessage","Mail sent successfully");
        }else {
            model.addAttribute("errorMessage", "Mail not send");
        }
        return getSupplierPaymentDetails(notificationId,session,model);

    }

    @GetMapping("/redirectToAdminPaymentHistory")
    public String redirectToAdminPaymentHistory(HttpSession session,@RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int size, Model model)
    {
        log.info("redirectToAdminPaymentHistory method in adminController");
        String email = (String) session.getAttribute("adminEmail");
        List<PaymentDetailsDTO> list=notificationService.getAllPaymentDetailsForAdminHistory(page,size);
        model.addAttribute("paymentList",list);
        model.addAttribute("dto",adminService.getAdminDetailsByEmail(email));

        Integer totalCount= notificationService.getTotalCount();
        int totalPages = (int) Math.ceil((double) totalCount / size);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);

        controllerHelper.addNotificationData(model,email);
        return "AdminPaymentHistory";
    }
}
