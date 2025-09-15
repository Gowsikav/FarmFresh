package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.dto.AdminDTO;
import com.xworkz.farmfresh.entity.AdminEntity;
import com.xworkz.farmfresh.repository.AdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService{

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AdminRepository adminRepository;

    public AdminServiceImpl()
    {
        System.out.println("AdminService implementation constructor");
    }

    @Override
    public boolean save(AdminDTO adminDTO) {
        System.out.println("save method in service");
        System.out.println("service data: "+adminDTO);
        if(adminDTO.getPassword().equals(adminDTO.getConfirmPassword()))
        {
            System.out.println("password matched");
            AdminEntity adminEntity=new AdminEntity();
            BeanUtils.copyProperties(adminDTO,adminEntity);
            adminEntity.setPassword(passwordEncoder.encode(adminEntity.getPassword()));
            return adminRepository.save(adminEntity);
        }
        System.out.println("password not matched");
        return false;
    }

    @Override
    public AdminDTO checkAdminLoginPassword(String email, String password) {
        System.out.println("checkAdminLoginPassword method in service");
        AdminEntity adminEntity=adminRepository.getPasswordByEmail(email);
        if(adminEntity==null)
            return null;
        if(passwordEncoder.matches(password,adminEntity.getPassword()))
        {
            AdminDTO adminDTO=new AdminDTO();
            BeanUtils.copyProperties(adminEntity,adminDTO);
            System.out.println("Password match");
            return adminDTO;
        }else System.out.println("password mismatch");
        return null;
    }

    @Override
    public AdminDTO getAdminDetailsByEmail(String email) {
        log.info("getAdminDetailsByEmail method in service");
        AdminEntity adminEntity=adminRepository.getPasswordByEmail(email);
        if(adminEntity!=null)
        {
            AdminDTO adminDTO=new AdminDTO();
            BeanUtils.copyProperties(adminEntity,adminDTO);
            return adminDTO;
        }
        return null;
    }

    @Override
    public boolean updateAdminProfileByEmail(String email, String adminName, String phoneNumber, String profilePath) {

        log.info("updateAdminProfileByEmail method in service");
        if (adminName == null || !adminName.matches("^[A-Za-z ]{2,50}$")) {
            log.error("Invalid Name: " ,adminName);
            return false;
        }
        if (phoneNumber == null || !phoneNumber.matches("^[0-9]{10}$")) {
            log.error("Invalid Phone Number: " , phoneNumber);
            return false;
        }
        return adminRepository.updateAdminProfileByEmail(email,adminName,phoneNumber,profilePath);
    }
}
