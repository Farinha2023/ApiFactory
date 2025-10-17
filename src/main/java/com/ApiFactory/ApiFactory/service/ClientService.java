package com.ApiFactory.ApiFactory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ApiFactory.ApiFactory.domain.Client;
import com.ApiFactory.ApiFactory.dto.ClientCreateDto;
import com.ApiFactory.ApiFactory.dto.ClientResponseDto;
import com.ApiFactory.ApiFactory.mapper.ClientMapper;
import com.ApiFactory.ApiFactory.repository.ClientRepository;
import com.ApiFactory.ApiFactory.repository.ContractRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepo;
    private final ContractRepository contractRepo;
    private final ClientFactory factory;
    private final ClientMapper mapper;

    public ClientService(ClientRepository clientRepo, ContractRepository contractRepo,
                         ClientFactory factory, ClientMapper mapper) {
        this.clientRepo = clientRepo;
        this.contractRepo = contractRepo;
        this.factory = factory;
        this.mapper = mapper;
    }

    @Transactional
    public ClientResponseDto create(ClientCreateDto dto) {
        Client client = factory.fromDto(dto);
        Client saved = clientRepo.save(client);
        return mapper.toDto(saved);
    }

    public Optional<ClientResponseDto> findById(Long id) {
        return clientRepo.findById(id).map(mapper::toDto);
    }

    @Transactional
    public ClientResponseDto update(Long id, ClientCreateDto dto) {
        Client client = clientRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
        // Update allowed fields only
        client.setName(dto.getName());
        client.setPhone(dto.getPhone());
        client.setEmail(dto.getEmail());
        // DO NOT update birthDate or companyIdentifier
        Client saved = clientRepo.save(client);
        return mapper.toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        Client client = clientRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
        LocalDate now = LocalDate.now();
        client.getContracts().forEach(c -> {
            if (c.getEndDate() == null || c.getEndDate().isAfter(now)) {
                c.setEndDate(now);
            }
        });
        clientRepo.delete(client);
    }
}
