package org.springframework.samples.petclinic.customers.web;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PetRequestTest {

    @Test
    void testPetRequest() {
        // Arrange
        Date birthDate = new Date();
        PetRequest petRequest = new PetRequest(1, birthDate, "Buddy", 2);

        // Assert
        assertEquals(1, petRequest.id());
        assertEquals(birthDate, petRequest.birthDate());
        assertEquals("Buddy", petRequest.name());
        assertEquals(2, petRequest.typeId());
    }
}
