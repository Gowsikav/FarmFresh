package com.xworkz.farmfresh.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class SupplierDTO {

    private Integer supplierId;

    @NotBlank
    @Size(min = 3, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 50)
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^[6-9]\\d{9}$")
    private String phoneNumber;

    @NotBlank
    @Size(min = 5, max = 250)
    private String address;

    private String typeOfMilk;

    private String profilePath;

    private SupplierBankDetailsDTO supplierBankDetails;
}
