package com.ApiFactory.ApiFactory.Domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("COMPANY")
public class Company extends Client{

     private String companyIdentifier; // example abc-911
    // getter/setter
}
