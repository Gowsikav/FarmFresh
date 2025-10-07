package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.dto.CollectMilkDTO;

import java.time.LocalDate;
import java.util.List;

public interface CollectMilkService {

    boolean save(CollectMilkDTO collectMilkDTO,String email);
    List<CollectMilkDTO> getAllDetailsByDate(LocalDate selectDate);
}
