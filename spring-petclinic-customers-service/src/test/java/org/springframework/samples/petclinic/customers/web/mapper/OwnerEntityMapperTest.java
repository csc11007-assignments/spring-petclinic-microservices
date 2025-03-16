package org.springframework.samples.petclinic.customers.web.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.customers.model.Owner;
import org.springframework.samples.petclinic.customers.web.OwnerRequest;

import static org.junit.jupiter.api.Assertions.*;

class OwnerEntityMapperTest {

    private final OwnerEntityMapper mapper = new OwnerEntityMapper();

    @Test
    void testMap() {
        // Arrange
        Owner owner = new Owner();
        OwnerRequest request = new OwnerRequest("John", "Doe", "123 Main St", "New York", "1234567890");

        // Act
        Owner result = mapper.map(owner, request);

        // Assert
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("123 Main St", result.getAddress());
        assertEquals("New York", result.getCity());
        assertEquals("1234567890", result.getTelephone());
        assertSame(owner, result);
    }
}
