package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.dto.AdminDTO;
import com.xworkz.farmfresh.dto.SupplierBankDetailsDTO;
import com.xworkz.farmfresh.dto.SupplierDTO;
import com.xworkz.farmfresh.entity.*;
import com.xworkz.farmfresh.repository.SupplierRepository;
import com.xworkz.farmfresh.util.OTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
            supplierAuditEntity.setName(supplierEntity.getFirstName()+" "+supplierEntity.getLastName());
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
            if(supplierEntity.getSupplierBankDetails()!=null)
            {
                SupplierBankDetailsDTO supplierBankDetailsDTO=new SupplierBankDetailsDTO();
                BeanUtils.copyProperties(supplierEntity.getSupplierBankDetails(),supplierBankDetailsDTO);
                supplierDTO.setSupplierBankDetails(supplierBankDetailsDTO);
            }
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

    @Override
    public SupplierDTO getSupplierDetails(String phone) {
        log.info("getSupplierDetails method in supplier service");
        SupplierEntity supplierEntity=supplierRepository.getSupplierByPhone(phone);
        SupplierDTO supplierDTO=new SupplierDTO();
        BeanUtils.copyProperties(supplierEntity,supplierDTO);
        return supplierDTO;
    }

    @Override
    public boolean sendOtpTOSupplierForLogin(String email) {
        log.info("sendOtpTOSupplierForLogin method in supplier service");
        String otp= OTPUtil.generateNumericOtp(6);
        if(supplierRepository.setOTPAndTime(email,otp,LocalDateTime.now()))
        {
            return emailSender.mailForSupplierLoginOtp(email, otp);
        }
        return false;
    }

    @Override
    public boolean checkOTPForSupplierLogin(String email, String otp) {
        log.info("checkOTPForSupplierLogin method in supplier service");
        SupplierEntity supplierEntity = supplierRepository.getSupplierByEmail(email);

        if (supplierEntity == null) {
            log.warn("No supplier found with email: {}", email);
            return false;
        }

        String savedOtp = supplierEntity.getLoginOTP();
        LocalDateTime savedTime = supplierEntity.getExpiryTime();

        if (savedOtp == null || !savedOtp.equals(otp)) {
            log.warn("Invalid OTP entered for email: {}", email);
            throw new RuntimeException("Invalid OTP");
        }

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(savedTime, now);
        if (duration.toMinutes() >= 5) {
            log.warn("OTP expired for email: {}", email);
            throw new RuntimeException("Time is expiry.Goto Login");
        }

        log.info("OTP verified successfully for email: {}", email);

        return supplierRepository.setOTPAndTime(email,null,null);
    }

    @Override
    public void setOtpAndTimeNull(String email) {
        log.info("setOtpAndTimeNull method in supplier service");
        supplierRepository.setOTPAndTime(email,null,null);
    }

    @Override
    public SupplierDTO getDetailsByEmail(String email) {
        log.info("getDetailsBy email method in supplier service");
        SupplierEntity supplierEntity= supplierRepository.getSupplierByEmail(email);
        SupplierDTO supplierDTO=new SupplierDTO();
        BeanUtils.copyProperties(supplierEntity,supplierDTO);
        if(supplierEntity.getSupplierBankDetails()!=null)
        {
            SupplierBankDetailsDTO supplierBankDetailsDTO=new SupplierBankDetailsDTO();
            BeanUtils.copyProperties(supplierEntity.getSupplierBankDetails(),supplierBankDetailsDTO);
            supplierDTO.setSupplierBankDetails(supplierBankDetailsDTO);
        }
        return supplierDTO;
    }

    @Override
    public boolean updateSupplierDetailsBySupplier(SupplierDTO supplierDTO) {
        log.info("updateSupplierDetailsBySupplier method in supplier service");
        SupplierEntity existingEntity=supplierRepository.getSupplierByEmail(supplierDTO.getEmail());
        SupplierAuditEntity supplierAuditEntity;
        if(existingEntity==null)
        {
            log.error("Entity not found for update");
            return false;
        }
        supplierAuditEntity=existingEntity.getSupplierAuditEntity();
        if(supplierAuditEntity==null)
        {
            log.error("log not found");
            return false;
        }
        existingEntity.setFirstName(supplierDTO.getFirstName());
        existingEntity.setLastName(supplierDTO.getLastName());
        existingEntity.setAddress(supplierDTO.getAddress());
        if(supplierDTO.getProfilePath()!=null)
        {
            existingEntity.setProfilePath(supplierDTO.getProfilePath());
        }
        supplierAuditEntity.setUpdatedAt(LocalDateTime.now());
        supplierAuditEntity.setUpdatedBy(supplierDTO.getFirstName()+" "+supplierDTO.getLastName());
        existingEntity.setSupplierAuditEntity(supplierAuditEntity);
        supplierAuditEntity.setSupplierEntity(existingEntity);

        return supplierRepository.updateSupplierDetailsBySupplier(existingEntity);
    }

    @Override
    public boolean updateSupplierBankDetails(SupplierBankDetailsDTO supplierBankDetailsDTO, String email) {
        log.info("updateSupplierBankDetails method in supplier service");
        SupplierEntity supplierEntity=supplierRepository.getSupplierByEmail(email);
        if(supplierEntity.getSupplierAuditEntity()==null)
        {
            log.error("Entity not found for supplier");
            return false;
        }
        SupplierAuditEntity supplierAuditEntity=supplierEntity.getSupplierAuditEntity();
        supplierAuditEntity.setUpdatedBy(supplierEntity.getFirstName()+" "+supplierEntity.getLastName());
        supplierAuditEntity.setUpdatedAt(LocalDateTime.now());

        supplierEntity.setSupplierAuditEntity(supplierAuditEntity);
        supplierAuditEntity.setSupplierEntity(supplierEntity);

        SupplierBankDetailsEntity supplierBankDetailsEntity = supplierEntity.getSupplierBankDetails();
        SupplierBankDetailsAuditEntity supplierBankDetailsAuditEntity;

        if (supplierBankDetailsEntity == null) {
            supplierBankDetailsEntity = new SupplierBankDetailsEntity();
            BeanUtils.copyProperties(supplierBankDetailsDTO, supplierBankDetailsEntity);

            supplierBankDetailsAuditEntity = new SupplierBankDetailsAuditEntity();
            supplierBankDetailsAuditEntity.setCreatedAt(LocalDateTime.now());
            supplierBankDetailsAuditEntity.setCreatedBy(supplierEntity.getFirstName() + " " + supplierEntity.getLastName());
        } else {
            BeanUtils.copyProperties(supplierBankDetailsDTO, supplierBankDetailsEntity);
            supplierBankDetailsAuditEntity = supplierBankDetailsEntity.getSupplierBankDetailsAuditEntity();
        }
        supplierBankDetailsAuditEntity.setUpdatedBy(supplierEntity.getFirstName()+" "+supplierEntity.getLastName());
        supplierBankDetailsAuditEntity.setUpdatedAt(LocalDateTime.now());

        supplierEntity.setSupplierBankDetails(supplierBankDetailsEntity);
        supplierBankDetailsEntity.setSupplierEntity(supplierEntity);
        supplierBankDetailsEntity.setSupplierBankDetailsAuditEntity(supplierBankDetailsAuditEntity);
        supplierBankDetailsAuditEntity.setSupplierBankDetailsEntity(supplierBankDetailsEntity);

        if(supplierRepository.updateSupplierDetailsBySupplier(supplierEntity))
        {
            return emailSender.mailForSupplierBankDetails(supplierEntity.getEmail(), supplierBankDetailsEntity);
        }
        return false;
    }

    @Override
    public boolean updateSupplierBankDetailsByAdmin(SupplierBankDetailsDTO supplierBankDetailsDTO, String email,String adminEmail) {
        log.info("updateSupplierBankDetailsByAdmin method in supplier service");
        SupplierEntity supplierEntity=supplierRepository.getSupplierByEmail(email);
        AdminDTO adminDTO=adminService.getAdminDetailsByEmail(adminEmail);
        if(supplierEntity.getSupplierAuditEntity()==null)
        {
            log.error("Entity not found for supplier");
            return false;
        }
        SupplierAuditEntity supplierAuditEntity=supplierEntity.getSupplierAuditEntity();
        supplierAuditEntity.setUpdatedBy(adminDTO.getAdminName());
        supplierAuditEntity.setUpdatedAt(LocalDateTime.now());

        supplierEntity.setSupplierAuditEntity(supplierAuditEntity);
        supplierAuditEntity.setSupplierEntity(supplierEntity);

        SupplierBankDetailsEntity supplierBankDetailsEntity = supplierEntity.getSupplierBankDetails();
        SupplierBankDetailsAuditEntity supplierBankDetailsAuditEntity;

        BeanUtils.copyProperties(supplierBankDetailsDTO, supplierBankDetailsEntity);
        supplierBankDetailsAuditEntity = supplierBankDetailsEntity.getSupplierBankDetailsAuditEntity();

        supplierBankDetailsAuditEntity.setUpdatedBy(adminDTO.getAdminName());
        supplierBankDetailsAuditEntity.setUpdatedAt(LocalDateTime.now());

        supplierEntity.setSupplierBankDetails(supplierBankDetailsEntity);
        supplierBankDetailsEntity.setSupplierEntity(supplierEntity);
        supplierBankDetailsEntity.setSupplierBankDetailsAuditEntity(supplierBankDetailsAuditEntity);
        supplierBankDetailsAuditEntity.setSupplierBankDetailsEntity(supplierBankDetailsEntity);

        if(supplierRepository.updateSupplierDetailsBySupplier(supplierEntity))
        {
            return emailSender.mailForSupplierBankDetails(supplierEntity.getEmail(), supplierBankDetailsEntity);
        }
        return false;
    }
}
