package org.springframework.samples.petclinic.customers.web;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    void testConstructor() {
        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException("Not found");

        // Assert
        assertEquals("Not found", exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }
}
