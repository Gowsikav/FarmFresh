package com.xworkz.farmfresh.repository;

import com.xworkz.farmfresh.entity.CollectMilkEntity;

import java.time.LocalDate;
import java.util.List;

public interface CollectMilkRepository {

    boolean save(CollectMilkEntity collectMilkEntity);
    List<CollectMilkEntity> getAllDetailsByDate(LocalDate selectDate);
    List<CollectMilkEntity> getAllDetailsBySupplier(String email,int page,int size);
    Integer getCountOFMilkDetailsByEmail(String email);
    int countSuppliersWithCollections(LocalDate startDate, LocalDate endDate);
    List<Object[]> getEntityForPaymentNotification(LocalDate startDate,LocalDate endDate);
    List<CollectMilkEntity> getCollectMilkDetailsForSupplierById(Integer supplierId,LocalDate start,LocalDate end);
    LocalDate getLastCollectedDate(Integer supplierId);
    Double getTotalLitre(Integer supplierId);
    Double getTotalMilkCollected();
    List<CollectMilkEntity> getRecentCollections();
}
