package com.xworkz.farmfresh.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
public class CollectMilkDTO {

    private Integer collectMilkId;
    private SupplierDTO supplier;
    private AdminDTO admin;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String typeOfMilk;

    @NotNull
    @Positive
    private Double price;

    @NotNull
    @Positive
    private Float quantity;

    @NotNull
    @Positive
    private Double totalAmount;

    @NotNull
    private LocalDate collectedDate=LocalDate.now();
}
