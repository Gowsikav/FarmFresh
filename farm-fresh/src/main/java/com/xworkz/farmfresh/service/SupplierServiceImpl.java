package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.dto.AdminDTO;
import com.xworkz.farmfresh.dto.SupplierDTO;
import com.xworkz.farmfresh.entity.SupplierAuditEntity;
import com.xworkz.farmfresh.entity.SupplierEntity;
import com.xworkz.farmfresh.repository.SupplierRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class SupplierServiceImpl implements SupplierService{

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private EmailSender emailSender;

    public SupplierServiceImpl()
    {
        log.info("SupplierServiceImpl constructor");
    }

    @Override
    public boolean addSupplier(SupplierDTO supplierDTO,String adminEmail) {
        log.info("addSupplier method in SupplierServiceImpl");
        SupplierEntity supplierEntity=new SupplierEntity();
        BeanUtils.copyProperties(supplierDTO,supplierEntity);

        AdminDTO adminDTO=adminService.getAdminDetailsByEmail(adminEmail);
        SupplierAuditEntity supplierAuditEntity=supplierEntity.getSupplierAuditEntity();
        if(supplierAuditEntity==null)
        {
            supplierAuditEntity=new SupplierAuditEntity();
            supplierAuditEntity.setName(supplierEntity.getFirstName()+supplierEntity.getLastName());
            supplierAuditEntity.setCreatedBy(adminDTO.getAdminName());
            supplierAuditEntity.setCreatedAt(LocalDateTime.now());
            supplierAuditEntity.setSupplierEntity(supplierEntity);
        }
        supplierEntity.setSupplierAuditEntity(supplierAuditEntity);
        supplierEntity.setIsActive(true);
        if(supplierRepository.addSupplier(supplierEntity))
        {
            log.info("supplier details saved");
            if(emailSender.mailForSupplierRegisterSuccess(supplierEntity.getEmail(),supplierEntity.getFirstName()+supplierEntity.getLastName()))
            {
                log.info("Mail send to supplier");
                return true;
            }
            log.error("Mail not send");
        }
        log.error("details not saved");
        return false;
    }

    @Override
    public List<SupplierDTO> getAllSuppliers(int pageNumber,int pageSize) {
        log.info("getAllSuppliers method in supplier service");
        List<SupplierEntity> supplierEntities=supplierRepository.getAllSuppliers(pageNumber,pageSize);
        List<SupplierDTO> supplierDTOS=new ArrayList<>();
        supplierEntities.forEach(supplierEntity -> {
            SupplierDTO supplierDTO=new SupplierDTO();
            BeanUtils.copyProperties(supplierEntity,supplierDTO);
            supplierDTOS.add(supplierDTO);
        });
        return supplierDTOS;
    }

    @Override
    public boolean checkEmail(String email) {
        log.info("checkEmail method in Supplier service");
        return supplierRepository.checkEmail(email);
    }

    @Override
    public boolean checkPhonNumber(String phoneNumber) {
        log.info("checkPhonNumber method in Supplier service");
        return supplierRepository.checkPhoneNumber(phoneNumber);
    }

    @Override
    public boolean editSupplierDetails(SupplierDTO supplierDTO, String adminEmail) {
        log.info("editSupplierDetails method in supplier service");
        AdminDTO adminDTO = adminService.getAdminDetailsByEmail(adminEmail);

        SupplierEntity supplierEntity=supplierRepository.getSupplierByEmail(supplierDTO.getEmail());

        SupplierEntity sendEntity=new SupplierEntity();
        BeanUtils.copyProperties(supplierDTO,sendEntity);

        sendEntity.setSupplierAuditEntity(supplierEntity.getSupplierAuditEntity());
        SupplierAuditEntity supplierAuditEntity=sendEntity.getSupplierAuditEntity();
        if(supplierAuditEntity==null)
        {
            log.error("getSupplier not found");
            return false;
        }
        supplierAuditEntity.setUpdatedBy(adminDTO.getAdminName());
        supplierAuditEntity.setUpdatedAt(LocalDateTime.now());
        return supplierRepository.updateSupplierDetails(sendEntity, false);
    }

    @Override
    public boolean deleteSupplierDetails(String email, String adminEmail) {
        log.info("deleteSupplierDetails method in supplier service");
        SupplierEntity supplierEntity = supplierRepository.getSupplierByEmail(email);
        AdminDTO adminDTO = adminService.getAdminDetailsByEmail(adminEmail);

        SupplierAuditEntity supplierAuditEntity=supplierEntity.getSupplierAuditEntity();
        if(supplierAuditEntity==null)
        {
            log.error("getSupplier not found");
            return false;
        }
        supplierAuditEntity.setDeletedBy(adminDTO.getAdminName());
        supplierAuditEntity.setDeletedAt(LocalDateTime.now());
        supplierEntity.setSupplierAuditEntity(supplierAuditEntity);
        return supplierRepository.updateSupplierDetails(supplierEntity, true);
    }

    @Override
    public List<SupplierDTO> searchSuppliers(String keyword) {
        List<SupplierEntity> supplierEntities=supplierRepository.getSearchSuppliers(keyword);
        List<SupplierDTO> supplierDTOS=new ArrayList<>();
        supplierEntities.forEach(supplierEntity -> {
            SupplierDTO supplierDTO=new SupplierDTO();
            BeanUtils.copyProperties(supplierEntity,supplierDTO);
            supplierDTOS.add(supplierDTO);
        });
        return supplierDTOS;
    }
}
