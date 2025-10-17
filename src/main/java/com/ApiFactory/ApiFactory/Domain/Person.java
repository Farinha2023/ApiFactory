package com.ApiFactory.ApiFactory.Domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("PERSON")
public class Person extends Client {

     private LocalDate birthDate;
    // getter/setter
}
