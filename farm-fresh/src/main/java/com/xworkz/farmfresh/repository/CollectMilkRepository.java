package com.xworkz.farmfresh.repository;

import com.xworkz.farmfresh.entity.CollectMilkEntity;

import java.time.LocalDate;
import java.util.List;

public interface CollectMilkRepository {

    boolean save(CollectMilkEntity collectMilkEntity);
    List<CollectMilkEntity> getAllDetailsByDate(LocalDate selectDate);
    List<CollectMilkEntity> getAllDetailsBySupplier(String email,int page,int size);
    Integer getCountOFMilkDetailsByEmail(String email);
}
