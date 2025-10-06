package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.dto.CollectMilkDTO;

public interface CollectMilkService {

    boolean save(CollectMilkDTO collectMilkDTO,String email);
}
