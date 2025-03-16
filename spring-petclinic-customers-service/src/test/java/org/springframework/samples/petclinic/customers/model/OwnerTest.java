package org.springframework.samples.petclinic.customers.model;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OwnerTest {

    @Test
    void testGetPets() {
        Owner owner = new Owner();
        assertNotNull(owner.getPets());
        assertTrue(owner.getPets().isEmpty());
    }

    @Test
    void testAddPet() {
        Owner owner = new Owner();
        Pet pet = new Pet();
        pet.setName("Max");
        owner.addPet(pet);
        assertEquals(1, owner.getPets().size());
        assertEquals("Max", owner.getPets().get(0).getName());
        assertEquals(owner, pet.getOwner());
    }

    @Test
    void testOwnerGettersAndSetters() {
        Owner owner = new Owner();

        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setAddress("123 Main St");
        owner.setCity("Springfield");
        owner.setTelephone("1234567890");

        assertEquals("John", owner.getFirstName());
        assertEquals("Doe", owner.getLastName());
        assertEquals("123 Main St", owner.getAddress());
        assertEquals("Springfield", owner.getCity());
        assertEquals("1234567890", owner.getTelephone());
    }

    @Test
    void testToString() {
        Owner owner = new Owner();
        owner.setFirstName("John");
        owner.setLastName("Doe");
        owner.setAddress("123 Main St");
        owner.setCity("Springfield");
        owner.setTelephone("1234567890");

        String ownerString = owner.toString();
        assertTrue(ownerString.contains("John"));
        assertTrue(ownerString.contains("Doe"));
        assertTrue(ownerString.contains("123 Main St"));
        assertTrue(ownerString.contains("Springfield"));
        assertTrue(ownerString.contains("1234567890"));
    }

    @Test
    void testGetPetsInternalWhenNull() {
        Owner owner = new Owner();
        List<Pet> pets = owner.getPets();
        assertNotNull(pets);
        assertTrue(pets.isEmpty());
    }
}
