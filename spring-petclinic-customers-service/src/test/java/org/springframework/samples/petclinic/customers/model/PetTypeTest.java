package org.springframework.samples.petclinic.customers.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PetTypeTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        PetType petType = new PetType();
        petType.setId(1);
        petType.setName("Dog");

        // Assert
        assertEquals(1, petType.getId());
        assertEquals("Dog", petType.getName());
    }
}
