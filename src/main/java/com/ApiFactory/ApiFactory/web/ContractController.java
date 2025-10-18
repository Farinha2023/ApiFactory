package com.ApiFactory.ApiFactory.web;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ApiFactory.ApiFactory.dto.ContractCreateDto;
import com.ApiFactory.ApiFactory.dto.ContractResponseDto;
import com.ApiFactory.ApiFactory.service.ContractService;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
public class ContractController {
    private final ContractService service;

    public ContractController(ContractService svc) { this.service = svc; }

    @PostMapping("/clients/{clientId}/contracts")
    public ResponseEntity<ContractResponseDto> create(@PathVariable Long clientId, @Valid @RequestBody ContractCreateDto dto) {
        return ResponseEntity.ok(service.create(clientId, dto));
    }

    @PatchMapping("/contracts/{id}/cost")
    public ResponseEntity<ContractResponseDto> updateCost(@PathVariable Long id, @RequestParam BigDecimal cost) {
        return ResponseEntity.ok(service.updateCost(id, cost));
    }

    @GetMapping("/clients/{clientId}/contracts")
    public ResponseEntity<List<ContractResponseDto>> listActive(
            @PathVariable Long clientId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime updatedSince) {
        return ResponseEntity.ok(service.getActiveContracts(clientId, updatedSince));
    }

    @GetMapping("/clients/{clientId}/contracts/sum")
    public ResponseEntity<BigDecimal> sumActive(@PathVariable Long clientId) {
        return ResponseEntity.ok(service.sumActiveContracts(clientId));
    }
}
