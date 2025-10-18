package com.ApiFactory.ApiFactory.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

public class ContractCreateDto {
    private LocalDate startDate;
    private LocalDate endDate;
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal costAmount;

    // getters/setters

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
