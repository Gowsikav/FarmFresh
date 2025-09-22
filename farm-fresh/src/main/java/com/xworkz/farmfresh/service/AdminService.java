package com.xworkz.farmfresh.service;


import com.xworkz.farmfresh.dto.AdminDTO;

public interface AdminService {

    boolean save(AdminDTO adminDTO);
    AdminDTO checkAdminLoginPassword(String email,String password);
    AdminDTO getAdminDetailsByEmail(String email);
    boolean updateAdminProfileByEmail(String email,String adminName,String phoneNumber,String profilePath);
    boolean checkEmail(String email);
    boolean sendMailToEmailForSetPassword(String email);
    boolean resetPasswordByEmail(String email,String password,String confirmPassword);
    boolean updateAdminLogoutTime(String email);
    int getSupplierCount();
}
