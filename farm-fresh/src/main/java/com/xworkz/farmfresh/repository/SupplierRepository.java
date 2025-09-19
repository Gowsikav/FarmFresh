package com.xworkz.farmfresh.repository;

import com.xworkz.farmfresh.entity.SupplierEntity;

import java.util.List;

public interface SupplierRepository {

    boolean addSupplier(SupplierEntity supplierEntity);
    List<SupplierEntity> getAllSuppliers();
    boolean checkEmail(String email);
    boolean checkPhoneNumber(String phoneNumber);
    boolean updateSupplierDetails(SupplierEntity supplierEntity,Boolean isDelete);
    SupplierEntity getSupplierByEmail(String email);
}
