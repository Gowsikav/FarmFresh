package com.xworkz.farmfresh.service;

import com.xworkz.farmfresh.dto.SupplierDTO;

import java.util.List;


public interface SupplierImportService {

    List<SupplierDTO> importSuppliersFromExcel(String filePath,String email);

}
