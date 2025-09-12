package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.dto.AdminDTO;
import com.xworkz.farmfresh.entity.AdminEntity;
import com.xworkz.farmfresh.repository.AdminRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
}
