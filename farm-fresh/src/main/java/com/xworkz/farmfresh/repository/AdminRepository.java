package com.xworkz.farmfresh.repository;

import com.xworkz.farmfresh.entity.AdminEntity;

public interface AdminRepository {

    boolean save(AdminEntity adminEntity);
    AdminEntity getPasswordByEmail(String email);
    boolean updateAdminProfileByEmail(String email,String adminName,String phoneNumber,String profilePath);
}
