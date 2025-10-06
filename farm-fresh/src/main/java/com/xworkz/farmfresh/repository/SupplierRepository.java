package com.xworkz.farmfresh.repository;

import com.xworkz.farmfresh.entity.SupplierEntity;

import java.util.List;

public interface SupplierRepository {

    boolean addSupplier(SupplierEntity supplierEntity);
    List<SupplierEntity> getAllSuppliers(int pageNumber,int pageSize);
    boolean checkEmail(String email);
    boolean checkPhoneNumber(String phoneNumber);
    boolean updateSupplierDetails(SupplierEntity supplierEntity,Boolean isDelete);
    SupplierEntity getSupplierByEmail(String email);
    Integer getSuppliersCount();
    List<SupplierEntity> getSearchSuppliers(String keyword);
    SupplierEntity getSupplierByPhone(String phone);
}
