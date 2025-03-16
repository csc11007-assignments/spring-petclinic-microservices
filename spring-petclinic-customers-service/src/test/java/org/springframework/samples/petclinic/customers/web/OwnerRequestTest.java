package org.springframework.samples.petclinic.customers.web;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OwnerRequestTest {

    @Test
    void testOwnerRequest() {
        // Arrange
        OwnerRequest request = new OwnerRequest("John", "Doe", "123 Main St", "New York", "1234567890");

        // Assert
        assertEquals("John", request.firstName());
        assertEquals("Doe", request.lastName());
        assertEquals("123 Main St", request.address());
        assertEquals("New York", request.city());
        assertEquals("1234567890", request.telephone());
    }
}
