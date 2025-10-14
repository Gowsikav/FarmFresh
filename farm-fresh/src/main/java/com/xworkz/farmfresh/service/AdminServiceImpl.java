package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.dto.AdminDTO;
import com.xworkz.farmfresh.entity.AdminAuditEntity;
import com.xworkz.farmfresh.entity.AdminEntity;
import com.xworkz.farmfresh.repository.AdminAuditRepository;
import com.xworkz.farmfresh.repository.AdminRepository;
import com.xworkz.farmfresh.repository.SupplierRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private AdminAuditRepository adminAuditRepository;

    private final Map<String, Integer> loginAttempts = new HashMap<>();

    @Autowired
    private EmailSender emailSender;

    public AdminServiceImpl() {
        log.info("AdminService implementation constructor");
    }

    @Override
    public boolean save(AdminDTO adminDTO) {
        log.info("save method in adminService");
        if (adminDTO.getPassword().equals(adminDTO.getConfirmPassword())) {
            log.info("password matched");
            AdminEntity adminEntity = new AdminEntity();
            BeanUtils.copyProperties(adminDTO, adminEntity);
            adminEntity.setPassword(passwordEncoder.encode(adminEntity.getPassword()));
            adminEntity.setIsBlocked(false);
            return adminRepository.save(adminEntity);
        }
        log.warn("password not matched");
        return false;
    }

    @Override
    public AdminDTO checkAdminLoginPassword(String email, String password) {
        log.info("checkAdminLoginPassword method in AdminServiceImpl");

        AdminEntity adminEntity = adminRepository.getDetailsByEmail(email);
        if (adminEntity == null) {
            log.warn("No admin found with email {}", email);
            return null;
        }
        if (adminEntity.getIsBlocked()) {
            log.warn("Account is blocked for email: {}", email);
            throw new RuntimeException("Account is blocked. Please reset password.");
        }
        if (passwordEncoder.matches(password, adminEntity.getPassword())) {
            loginAttempts.remove(email);

            adminAuditRepository.findActiveSession(adminEntity.getAdminId());

            AdminAuditEntity audit = new AdminAuditEntity();
            audit.setAdminEntity(adminEntity);
            audit.setLoginTime(LocalDateTime.now());
            audit.setAuditName(adminEntity.getAdminName());

            if (adminAuditRepository.save(audit)) {
                log.info("Admin audit record created for login");
            } else {
                log.error("Admin audit record not created");
            }

            AdminDTO adminDTO = new AdminDTO();
            BeanUtils.copyProperties(adminEntity, adminDTO);
            log.info("Password match for {}", email);
            return adminDTO;
        }
        int attempts = loginAttempts.getOrDefault(email, 0) + 1;
        loginAttempts.put(email, attempts);

        if (attempts >= 3) {
            if (!adminRepository.updateIsBlockedByEmail(email, true)) {
                log.error("Failed to block account for {}", email);
                return null;
            }
            log.warn("Account blocked for {} after {} failed attempts", email, attempts);
            throw new RuntimeException("Account is blocked. Please reset password.");
        } else {
            log.warn("Password mismatch for {}. Attempt {}/3", email, attempts);
            throw new RuntimeException("Password mismatch for " + email + " Attempt " + attempts + "/3");
        }
    }

    @Override
    public boolean updateAdminLogoutTime(String email) {
        log.info("updateAdminLogoutTime method in AdminServiceImpl");

        AdminEntity adminEntity = adminRepository.getDetailsByEmail(email);
        if (adminEntity == null) {
            log.warn("No admin found with email {}", email);
            return false;
        }
        Optional<AdminAuditEntity> activeAuditOpt = adminAuditRepository.findActiveSession(adminEntity.getAdminId());

        if (activeAuditOpt.isPresent()) {
            AdminAuditEntity lastAudit = activeAuditOpt.get();
            lastAudit.setLogoutTime(LocalDateTime.now());

            if (adminAuditRepository.save(lastAudit)) {
                log.info("Logout time updated for admin audit");
                return true;
            } else {
                log.error("Logout time not changed");
            }
        } else {
            log.warn("No active audit session found to update logout for {}", email);
            return true;
        }
        return false;
    }

    @Override
    public AdminDTO getAdminDetailsByEmail(String email) {
        log.info("getAdminDetailsByEmail method in adminService");
        AdminEntity adminEntity = adminRepository.getDetailsByEmail(email);
        if (adminEntity != null) {
            AdminDTO adminDTO = new AdminDTO();
            BeanUtils.copyProperties(adminEntity, adminDTO);
            return adminDTO;
        }
        return null;
    }

    @Override
    public boolean updateAdminProfileByEmail(String email, String adminName, String phoneNumber, String profilePath) {

        log.info("updateAdminProfileByEmail method in adminService");
        if (adminName == null || !adminName.matches("^[A-Za-z ]{2,50}$")) {
            log.error("Invalid Name: {} ", adminName);
            return false;
        }
        if (phoneNumber == null || !phoneNumber.matches("^[0-9]{10}$")) {
            log.error("Invalid Phone Number:{} ", phoneNumber);
            return false;
        }
        return adminRepository.updateAdminProfileByEmail(email, adminName, phoneNumber, profilePath);
    }

    @Override
    public boolean checkEmail(String email) {
        log.info("checkEmail method in adminService");
        AdminEntity adminEntity = adminRepository.getDetailsByEmail(email);
        return adminEntity != null;
    }

    @Override
    public boolean sendMailToEmailForSetPassword(String email) {
        log.info("sendMailToEmailForSetPassword method in  adminService");
        return emailSender.mailSend(email);
    }

    @Override
    public boolean resetPasswordByEmail(String email, String password, String confirmPassword) {
        log.info("resetPasswordByEmail method in admin adminService");
        if (!password.equals(confirmPassword)) {
            log.warn("Passwords do not match for email: {}", email);
            return false;
        }
        String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{5,}$";
        if (!password.matches(PASSWORD_PATTERN)) {
            log.warn("Password does not meet strength requirements for email: {}", email);
            return false;
        }
        password = passwordEncoder.encode(password);
        return adminRepository.resetPasswordByEmail(email, password, confirmPassword);
    }

    @Override
    public int getSupplierCount() {
        log.info("getSupplierCount method in service");
        return supplierRepository.getSuppliersCount();
    }
}
