package org.springframework.samples.petclinic.customers.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinimalOwnerPetTest {

    @Test
    void testGetPets() {
        // Create a new owner
        Owner owner = new Owner();

        // Just verify getPets returns a non-null list
        assertNotNull(owner.getPets());
        // A new owner should have no pets
        assertTrue(owner.getPets().isEmpty());
    }

    @Test
    void testAddPet() {
        // Create a new owner and pet
        Owner owner = new Owner();
        Pet pet = new Pet();
        pet.setName("Max");

        // Add the pet to the owner
        owner.addPet(pet);

        // Verify the pet was added
        assertEquals(1, owner.getPets().size());
    }
}
