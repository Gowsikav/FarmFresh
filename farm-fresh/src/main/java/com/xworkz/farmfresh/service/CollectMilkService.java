package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.dto.CollectMilkDTO;

import java.time.LocalDate;
import java.util.List;

public interface CollectMilkService {

    boolean save(CollectMilkDTO collectMilkDTO,String email);
    List<CollectMilkDTO> getAllDetailsByDate(LocalDate selectDate);
    List<CollectMilkDTO> getAllDetailsBySupplier(String email,int page,int size);
    Integer getCountOFMilkDetailsByEmail(String email);
    List<CollectMilkDTO> getAllDetailsBySupplier(Long notificationId);
    LocalDate getLastCollectedDate(Integer supplierId);
    Double getTotalLitre(Integer supplierId);

}
