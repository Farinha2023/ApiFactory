package com.ApiFactory.ApiFactory.service;

import org.springframework.stereotype.Component;

import com.ApiFactory.ApiFactory.domain.Client;
import com.ApiFactory.ApiFactory.domain.Company;
import com.ApiFactory.ApiFactory.domain.Person;
import com.ApiFactory.ApiFactory.dto.ClientCreateDto;


@Component
public class ClientFactory {
     public Client fromDto(ClientCreateDto dto) {
        if ("PERSON".equalsIgnoreCase(dto.getType())) {
            Person p = new Person();
            p.setName(dto.getName());
            p.setPhone(dto.getPhone());
            p.setEmail(dto.getEmail());
            p.setBirthDate(dto.getBirthDate());
            return p;
        } else if ("COMPANY".equalsIgnoreCase(dto.getType())) {
            Company c = new Company();
            c.setName(dto.getName());
            c.setPhone(dto.getPhone());
            c.setEmail(dto.getEmail());
            c.setCompanyIdentifier(dto.getCompanyIdentifier());
            return c;
        } else {
            throw new IllegalArgumentException("Unknown client type: " + dto.getType());
        }
    }
}
