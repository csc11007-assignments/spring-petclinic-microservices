package org.springframework.samples.petclinic.customers.web;

import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.customers.model.Pet;
import org.springframework.samples.petclinic.customers.model.PetType;
import org.springframework.samples.petclinic.customers.model.Owner;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PetDetailsTest {

    @Test
    void testPetDetailsConstructor() {
        // Arrange
        Owner owner = new Owner();
        owner.setFirstName("John");
        owner.setLastName("Doe");

        PetType petType = new PetType();
        petType.setName("Dog");

        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Buddy");
        pet.setBirthDate(new Date());
        pet.setType(petType);
        pet.setOwner(owner);

        // Act
        PetDetails petDetails = new PetDetails(pet);

        // Assert
        assertEquals(1, petDetails.id());
        assertEquals("Buddy", petDetails.name());
        assertEquals("John Doe", petDetails.owner());
        assertEquals(pet.getBirthDate(), petDetails.birthDate());
        assertEquals(petType, petDetails.type());
    }
}
