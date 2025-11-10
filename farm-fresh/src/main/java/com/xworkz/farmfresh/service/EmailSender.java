package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.dto.PaymentDetailsDTO;
import com.xworkz.farmfresh.entity.PaymentDetailsEntity;
import com.xworkz.farmfresh.entity.SupplierBankDetailsEntity;
import com.xworkz.farmfresh.entity.SupplierEntity;

import java.util.List;

public interface EmailSender {

    boolean mailSend(String email);
    boolean mailForSupplierRegisterSuccess(String email,String supplierName,String qrCodePath);
    boolean mailForSupplierLoginOtp(String email,String otp);
    boolean mailForSupplierBankDetails(String email, SupplierBankDetailsEntity bankDetails);
    boolean mailForSupplierPayment(SupplierEntity supplier, PaymentDetailsEntity paymentDetails);
    boolean mailForBankDetailsRequest(SupplierEntity supplier);
    boolean mailForAdminPaymentSummary(List<PaymentDetailsDTO> payments);
}
