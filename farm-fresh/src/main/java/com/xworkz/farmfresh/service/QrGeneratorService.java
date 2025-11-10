package com.xworkz.farmfresh.service;

public interface QrGeneratorService {

     String generateSupplierQR(Integer supplierId, String email, String phoneNumber);
}
