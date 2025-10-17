package com.ApiFactory.ApiFactory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.ApiFactory.ApiFactory.domain.Contract;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface  ContractRepository extends JpaRepository<Contract, Long>{

    @Query("select c from Contract c where c.client.id = :clientId and (c.endDate is null or c.endDate > :today)")
    List<Contract> findActiveContractsByClientId(Long clientId, LocalDate today);

    @Query("select sum(c.costAmount) from Contract c where c.client.id = :clientId and (c.endDate is null or c.endDate > :today)")
    BigDecimal sumActiveContractCostsByClientId(Long clientId, LocalDate today);
}
