package com.ApiFactory.ApiFactory.dto;

import java.time.OffsetDateTime;
import java.time.LocalDate;

public class ClientResponseDto {
    private Long id;
    private String type;
    private String name;
    private String phone;
    private String email;
    private LocalDate birthDate;
    private String companyIdentifier;
    private OffsetDateTime createdAt;
    // not visible for  updatedAt
    // getters/setters
}