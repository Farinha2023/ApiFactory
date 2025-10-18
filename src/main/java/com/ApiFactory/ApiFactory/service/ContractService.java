package com.ApiFactory.ApiFactory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ApiFactory.ApiFactory.domain.Client;
import com.ApiFactory.ApiFactory.domain.Contract;
import com.ApiFactory.ApiFactory.dto.ContractCreateDto;
import com.ApiFactory.ApiFactory.dto.ContractResponseDto;
import com.ApiFactory.ApiFactory.repository.ClientRepository;
import com.ApiFactory.ApiFactory.repository.ContractRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContractService {

    private final ContractRepository contractRepo;
    private final ClientRepository clientRepo;

    public ContractService(ContractRepository contractRepo, ClientRepository clientRepo) {
        this.contractRepo = contractRepo;
        this.clientRepo = clientRepo;
    }

    @Transactional
    public ContractResponseDto create(Long clientId, ContractCreateDto dto) {
        Client client = clientRepo.findById(clientId).orElseThrow();
        Contract c = new Contract();
        c.setClient(client);
        c.setStartDate(dto.getStartDate()); // entity default if null
        c.setEndDate(dto.getEndDate());
        c.setCostAmount(dto.getCostAmount());
        Contract saved = contractRepo.save(c);
        return toDto(saved);
    }

    @Transactional
    public ContractResponseDto updateCost(Long contractId, BigDecimal newCost) {
        Contract c = contractRepo.findById(contractId).orElseThrow();
        c.setCostAmount(newCost); // PreUpdate updates updatedOn
        Contract saved = contractRepo.save(c);
        return toDto(saved);
    }

    public List<ContractResponseDto> getActiveContracts(Long clientId, OffsetDateTime updatedSince) {
        LocalDate today = LocalDate.now();
        List<Contract> list = contractRepo.findActiveContractsByClientId(clientId, today);
        return list.stream()
            .filter(c -> updatedSince == null || (c.getUpdatedOn() != null && c.getUpdatedOn().isAfter(updatedSince)))
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public BigDecimal sumActiveContracts(Long clientId) {
        BigDecimal sum = contractRepo.sumActiveContractCostsByClientId(clientId, LocalDate.now());
        return sum == null ? BigDecimal.ZERO : sum;
    }

    private ContractResponseDto toDto(Contract c) {
        ContractResponseDto d = new ContractResponseDto();
        d.setId(c.getId());
        d.setStartDate(c.getStartDate());
        d.setEndDate(c.getEndDate());
        d.setCostAmount(c.getCostAmount());
        return d;
    }
}
