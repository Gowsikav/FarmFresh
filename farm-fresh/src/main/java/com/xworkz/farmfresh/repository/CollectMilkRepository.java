package com.xworkz.farmfresh.repository;

import com.xworkz.farmfresh.entity.CollectMilkEntity;

import java.time.LocalDate;
import java.util.List;

public interface CollectMilkRepository {

    boolean save(CollectMilkEntity collectMilkEntity);
    List<CollectMilkEntity> getAllDetailsByDate(LocalDate selectDate);
}
