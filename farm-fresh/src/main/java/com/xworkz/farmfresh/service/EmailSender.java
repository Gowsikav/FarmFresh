package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.dto.PaymentDetailsDTO;
import com.xworkz.farmfresh.entity.PaymentDetailsEntity;
import com.xworkz.farmfresh.entity.SupplierBankDetailsEntity;
import com.xworkz.farmfresh.entity.SupplierEntity;

import java.util.List;


public interface EmailSender {

    boolean mailSend(String email);
    void mailForSupplierRegisterSuccess(String email,String supplierName,String qrCodePath);
    boolean mailForSupplierLoginOtp(String email,String otp);
    void mailForSupplierBankDetails(String email, SupplierBankDetailsEntity bankDetails);
    void mailForSupplierPayment(SupplierEntity supplier, PaymentDetailsEntity paymentDetails);
    void mailForBankDetailsRequest(SupplierEntity supplier);
    void mailForAdminPaymentSummary(List<PaymentDetailsDTO> payments);
}
