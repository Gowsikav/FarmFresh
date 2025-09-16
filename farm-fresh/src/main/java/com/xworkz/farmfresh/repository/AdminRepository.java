package com.xworkz.farmfresh.repository;

import com.xworkz.farmfresh.entity.AdminEntity;

public interface AdminRepository {

    boolean save(AdminEntity adminEntity);
    AdminEntity getDetailsByEmail(String email);
    boolean updateAdminProfileByEmail(String email,String adminName,String phoneNumber,String profilePath);
    boolean updateIsBlockedByEmail(String email,boolean isBlocked);
    boolean resetPasswordByEmail(String email,String password,String confirmPassword);

}
