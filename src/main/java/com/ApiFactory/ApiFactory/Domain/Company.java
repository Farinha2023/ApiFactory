package com.ApiFactory.ApiFactory.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("COMPANY")
public class Company extends Client{

     private String companyIdentifier; // example abc-911

    // getter/setter
    public String getCompanyIdentifier() {
        return this.companyIdentifier;
    }
     public void setCompanyIdentifier(String newIdentifier) {
        this.companyIdentifier = newIdentifier;
     }
}
