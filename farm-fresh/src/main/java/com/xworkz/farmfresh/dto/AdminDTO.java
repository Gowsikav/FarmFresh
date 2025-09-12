package com.xworkz.farmfresh.dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class AdminDTO {

    private Integer adminId;

    @NotBlank
    private String adminName;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$")
    private String phoneNumber;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 5,max = 15)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{5,15}$")
    private String password;

    @NotBlank
    @Size(min = 5,max = 15)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{5,15}$")
    private String confirmPassword;
}
