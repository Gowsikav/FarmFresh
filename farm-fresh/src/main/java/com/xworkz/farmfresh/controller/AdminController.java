package com.xworkz.farmfresh.controller;

import com.xworkz.farmfresh.dto.AdminDTO;
import com.xworkz.farmfresh.entity.NotificationEntity;
import com.xworkz.farmfresh.service.AdminService;
import com.xworkz.farmfresh.service.PaymentNotificationService;
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

    @Value("${file.upload-dir}")
    private String uploadDir;

    public AdminController() {
        log.info("AdminController constructor");
    }

    @GetMapping("/redirectToAdminLogin")
    public String getAdminLogin(Model model) {
        log.info("getAdminLogin method in AdminController");
        return "AdminLogin";
    }

    @PostMapping("/adminLogin")
    public String checkPasswordForAdminLogin(@RequestParam String email, @RequestParam String password, Model model) {
        log.info("checkPasswordForAdminLogin method in adminController");
        log.info("Email: {} password: {}" ,email , password);
        AdminDTO adminDTO=null;
        try {
            adminDTO= adminService.checkAdminLoginPassword(email, password);
            if (adminDTO != null) {
                model.addAttribute("dto", adminDTO);
                int count=adminService.getSupplierCount();
                model.addAttribute("suppliersCount",count);
                return "AdminDashboard";
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
    public String redirectToUpdate(@RequestParam("email") String email, Model model) {
        log.info("redirectTo update page in adminController");
        AdminDTO adminDTO = adminService.getAdminDetailsByEmail(email);
        model.addAttribute("dto", adminDTO);
        return "UpdateAdminProfile";
    }

    @GetMapping("/redirectToAdminDashboard")
    public String getDashboard(@RequestParam("email") String email, Model model) {
        log.info("getDashboard method in admin controller");
        AdminDTO adminDTO = adminService.getAdminDetailsByEmail(email);
        model.addAttribute("dto", adminDTO);
        int count=adminService.getSupplierCount();
        model.addAttribute("suppliersCount",count);
        List<NotificationEntity> notifications = notificationService.getNotificationsByAdminEmail(email);
        long unreadCount = notifications.size();
// In your controller method
        log.info("Notifications count: {}", notifications.size());
        log.info("Unread count: {}", unreadCount);

        for (NotificationEntity notification : notifications) {
            log.info("Notification: id={}, message={}, isRead={}",
                    notification.getId(), notification.getMessage(), notification.getIsRead());
        }

        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", unreadCount);
        return "AdminDashboard";
    }

    @PostMapping("/updateAdminProfile")
    public String updateAdminProfile(
            @RequestParam("adminName") String adminName,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
            Model model) {

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
            return getDashboard(email, model);
        }
        model.addAttribute("errorMessage", "Invalid details");

        return redirectToUpdate(email,model);
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
    public String updateAdminLogout(@RequestParam("email")String email,Model model)
    {
        log.info("updateAdminLogout method in admin controller");
        if(adminService.updateAdminLogoutTime(email))
        {
            log.info("logout time changed");
            return "index";
        }
        return getDashboard(email,model);
    }


    @GetMapping("/redirectToManageProducts")
    public String getManageProductPage(@RequestParam("email")String email,Model model)
    {
        log.info("getManageProductPage method in adminController");
        AdminDTO adminDTO = adminService.getAdminDetailsByEmail(email);
        model.addAttribute("dto", adminDTO);
        return "ManageProducts";
    }
}
