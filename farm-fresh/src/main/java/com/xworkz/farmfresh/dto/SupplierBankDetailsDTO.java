package com.xworkz.farmfresh.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

@Data
public class SupplierBankDetailsDTO {

    @NotBlank
    private String bankName;

    @NotBlank
    private String bankBranch;

    @Positive
    @Pattern(regexp = "\\d{9,18}")
    private String accountNumber;

    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$")
    private String IFSCCode;

    @NotBlank
    private String accountType;
}
