package com.ApiFactory.ApiFactory.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class ClientCreateDto {

    @NotBlank
    private String type; // PERSON or COMPANY

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "^\\+?\\d{7,15}$", message = "Phone must be 7-15 digits, optional +")
    private String phone;

    @NotBlank @Email
    private String email;

    @Past
    private LocalDate birthDate; // only for PERSON

    @Pattern(regexp = "^[a-z]{3}-\\d{3}$", message = "companyIdentifier must match aaa-123")
    private String companyIdentifier;

    // getters and setters
}