package com.xworkz.farmfresh.controller;

import com.xworkz.farmfresh.dto.AdminDTO;
import com.xworkz.farmfresh.service.AdminService;
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

@Slf4j
@Controller
@RequestMapping("/")
@PropertySource("classpath:application.properties")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public AdminController() {
        System.out.println("AdminController constructor");
    }

    @GetMapping("/redirectToAdminLogin")
    public String getAdminLogin(Model model) {
        System.out.println("getAdminLogin method in AdminController");
        return "AdminLogin";
    }

    @PostMapping("/adminLogin")
    public String checkPasswordForAdminLogin(@RequestParam String email, @RequestParam String password, Model model) {
        System.out.println("checkPasswordForAdminLogin method in adminController");
        System.out.println("Email: " + email + " password: " + password);
        AdminDTO adminDTO = adminService.checkAdminLoginPassword(email, password);
        if (adminDTO != null) {
            model.addAttribute("dto", adminDTO);
            return "AdminDashboard";
        } else {
            model.addAttribute("errorMessage", "Password / email incorrect");
            return "AdminLogin";
        }
    }

    @GetMapping("/redirectToUpdateAdminProfile")
    public String redirectToUpdate(@RequestParam("email") String email, Model model) {
        System.out.println("redirectTo update page in adminController");
        AdminDTO adminDTO = adminService.getAdminDetailsByEmail(email);
        model.addAttribute("dto", adminDTO);
        return "UpdateAdminProfile";
    }

    @GetMapping("/redirectToAdminDashboard")
    public String getDashboard(@RequestParam("email") String email, Model model) {
        AdminDTO adminDTO = adminService.getAdminDetailsByEmail(email);
        model.addAttribute("dto", adminDTO);
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
                System.out.println(uploadDir);
                byte[] bytes = profilePicture.getBytes();
                String fileName = adminName + "_" + System.currentTimeMillis() + "_" + profilePicture.getOriginalFilename();
                Path path = Paths.get(uploadDir ,fileName);
                Files.write(path, bytes);
                profilePath = path.getFileName().toString();
            } catch (IOException e) {
                System.out.println(e.getMessage());
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

    @GetMapping("/adminLogout")
    public String adminLogoutIndexPage()
    {
        log.info("adminLogoutIndexPage method in adminController");
        return "index";
    }

}
