package com.xworkz.farmfresh.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProductPriceDTO {

    private Integer productId;

    @NotBlank
    private String productName;

    @NotNull
    @DecimalMin(value = "1")
    @Digits(fraction = 2,integer = 5)
    private Double price;
}
