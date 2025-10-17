package com.ApiFactory.ApiFactory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ApiFactory.ApiFactory.domain.Client;

public interface ClientRepository extends JpaRepository<Client, Long> { }