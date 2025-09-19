package com.xworkz.farmfresh.service;

public interface EmailSender {

    boolean mailSend(String email);
    boolean mailForSupplierRegisterSuccess(String email,String supplierName);
}
