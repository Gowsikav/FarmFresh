package com.xworkz.farmfresh.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PaymentDetailsDTO {

    private Integer id;
    private SupplierDTO supplier;
    private AdminDTO admin;
    private Double totalAmount;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private LocalDate paymentDate;
    private String paymentStatus;

}
