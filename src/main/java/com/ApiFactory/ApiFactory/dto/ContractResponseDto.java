package com.ApiFactory.ApiFactory.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContractResponseDto {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal costAmount;

    // Getters /setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(BigDecimal costAmount) {
        this.costAmount = costAmount;
    }
}