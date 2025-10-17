package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.entity.PaymentDetailsEntity;
import com.xworkz.farmfresh.entity.SupplierBankDetailsEntity;
import com.xworkz.farmfresh.entity.SupplierEntity;

public interface EmailSender {

    boolean mailSend(String email);
    boolean mailForSupplierRegisterSuccess(String email,String supplierName);
    boolean mailForSupplierLoginOtp(String email,String otp);
    boolean mailForSupplierBankDetails(String email, SupplierBankDetailsEntity bankDetails);
    boolean mailForSupplierPayment(SupplierEntity supplier, PaymentDetailsEntity paymentDetails);
    boolean mailForBankDetailsRequest(SupplierEntity supplier);
}
