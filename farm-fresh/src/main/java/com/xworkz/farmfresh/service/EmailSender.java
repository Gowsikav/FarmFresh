package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.entity.SupplierBankDetailsEntity;

public interface EmailSender {

    boolean mailSend(String email);
    boolean mailForSupplierRegisterSuccess(String email,String supplierName);
    boolean mailForSupplierLoginOtp(String email,String otp);
    boolean mailForSupplierBankDetails(String email, SupplierBankDetailsEntity bankDetails);
}
