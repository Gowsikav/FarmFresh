package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.dto.AdminDTO;
import com.xworkz.farmfresh.dto.CollectMilkDTO;
import com.xworkz.farmfresh.entity.AdminEntity;
import com.xworkz.farmfresh.entity.CollectMilkAuditEntity;
import com.xworkz.farmfresh.entity.CollectMilkEntity;
import com.xworkz.farmfresh.repository.CollectMilkRepository;
import com.xworkz.farmfresh.repository.SupplierRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class CollectMilkServiceImpl implements CollectMilkService{

    @Autowired
    private CollectMilkRepository collectMilkRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private SupplierRepository supplierRepository;

    public CollectMilkServiceImpl()
    {
        log.info("CollectMilkServiceImpl constructor");
    }

    @Override
    public boolean save(CollectMilkDTO collectMilkDTO,String email) {
        log.info("save method in CollectMilkServiceImpl");
        AdminDTO adminDTO=adminService.getAdminDetailsByEmail(email);

        AdminEntity adminEntity=new AdminEntity();
        BeanUtils.copyProperties(adminDTO,adminEntity);

        CollectMilkEntity collectMilkEntity=new CollectMilkEntity();
        BeanUtils.copyProperties(collectMilkDTO,collectMilkEntity);
        CollectMilkAuditEntity collectMilkAuditEntity=new CollectMilkAuditEntity();
        collectMilkAuditEntity.setCreatedAt(LocalDateTime.now());
        collectMilkAuditEntity.setCreatedBy(adminDTO.getAdminName());
        collectMilkAuditEntity.setCollectMilkEntity(collectMilkEntity);

        collectMilkEntity.setCollectMilkAuditEntity(collectMilkAuditEntity);

        collectMilkEntity.setAdmin(adminEntity);
        collectMilkEntity.setSupplier(supplierRepository.getSupplierByPhone(collectMilkDTO.getPhoneNumber()));
        return collectMilkRepository.save(collectMilkEntity);
    }
}

