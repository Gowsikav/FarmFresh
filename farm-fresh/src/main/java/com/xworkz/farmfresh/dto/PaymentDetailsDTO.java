package com.xworkz.farmfresh.dto;

import com.xworkz.farmfresh.entity.AdminEntity;
import com.xworkz.farmfresh.entity.SupplierEntity;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PaymentDetailsDTO {

    private Integer id;
    private SupplierEntity supplier;
    private AdminEntity admin;
    private Double totalAmount;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private LocalDate paymentDate;
    private String paymentStatus;

}
