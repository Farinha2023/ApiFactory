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

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getCompanyIdentifier() { return companyIdentifier; }
    public void setCompanyIdentifier(String companyIdentifier) { this.companyIdentifier = companyIdentifier; }

}