package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.dto.SupplierBankDetailsDTO;
import com.xworkz.farmfresh.dto.SupplierDTO;

import java.util.List;

public interface SupplierService {

    boolean addSupplier(SupplierDTO supplierDTO,String adminEmail);
    List<SupplierDTO> getAllSuppliers(int pageNumber,int pageSize);
    boolean checkEmail(String email);
    boolean checkPhonNumber(String phoneNumber);
    boolean editSupplierDetails(SupplierDTO supplierDTO,String adminEmail);
    boolean deleteSupplierDetails(String email,String adminEmail);
    List<SupplierDTO> searchSuppliers(String keyword);
    SupplierDTO getSupplierDetails(String phone);

    boolean sendOtpTOSupplierForLogin(String email);
    boolean checkOTPForSupplierLogin(String email,String otp);
    void setOtpAndTimeNull(String email);
    SupplierDTO getDetailsByEmail(String email);
    boolean updateSupplierDetailsBySupplier(SupplierDTO supplierDTO);
    boolean updateSupplierBankDetails(SupplierBankDetailsDTO supplierBankDetailsDTO,String email);
    boolean updateSupplierBankDetailsByAdmin(SupplierBankDetailsDTO supplierBankDetailsDTO,String email,String adminEmail);
    SupplierDTO getSupplierDetailsByNotificationId(Long notificationId);
}