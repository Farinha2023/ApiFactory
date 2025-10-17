package com.ApiFactory.ApiFactory.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "contracts", indexes = {
    @Index(name = "idx_contract_client_end", columnList = "client_id, end_date")
})
public class Contract {

     @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Client client;

    private LocalDate startDate; // default to today if null
    private LocalDate endDate;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal costAmount;

    // internal last modified
    private OffsetDateTime updatedOn;

    @PrePersist
    void prePersist() {
        if (startDate == null) {
            startDate = LocalDate.now();
        }
        updatedOn = OffsetDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        updatedOn = OffsetDateTime.now();
    }

    // getters and setters
}
