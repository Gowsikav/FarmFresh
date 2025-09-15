package com.xworkz.farmfresh.service;


import com.xworkz.farmfresh.dto.AdminDTO;

public interface AdminService {

    boolean save(AdminDTO adminDTO);
    AdminDTO checkAdminLoginPassword(String email,String password);
    AdminDTO getAdminDetailsByEmail(String email);
    boolean updateAdminProfileByEmail(String email,String adminName,String phoneNumber,String profilePath);
}
