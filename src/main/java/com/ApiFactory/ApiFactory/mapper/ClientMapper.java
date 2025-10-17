package com.ApiFactory.ApiFactory.mapper;
import org.springframework.stereotype.Component;

import com.ApiFactory.ApiFactory.domain.Client;
import com.ApiFactory.ApiFactory.domain.Company;
import com.ApiFactory.ApiFactory.domain.Person;
import com.ApiFactory.ApiFactory.dto.ClientResponseDto;

@Component
public class ClientMapper {
    public ClientResponseDto toDto(Client entity) {
        ClientResponseDto dto = new ClientResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setCreatedAt(entity.getCreatedAt());
        if (entity instanceof Person p) {
            dto.setType("PERSON");
            dto.setBirthDate(p.getBirthDate());
        } else if (entity instanceof Company c) {
            dto.setType("COMPANY");
            dto.setCompanyIdentifier(c.getCompanyIdentifier());
        }
        return dto;
    }
}
