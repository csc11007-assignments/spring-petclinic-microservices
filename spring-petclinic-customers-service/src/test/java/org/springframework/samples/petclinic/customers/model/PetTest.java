package org.springframework.samples.petclinic.customers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PetTest {

    private Pet pet;
    private Owner owner;
    private PetType petType;

    @BeforeEach
    void setUp() {
        pet = new Pet();
        owner = new Owner();
        petType = new PetType();

        owner.setFirstName("John");
        owner.setLastName("Doe");
        petType.setName("Dog");
        pet.setOwner(owner);
        pet.setType(petType);
    }

    @Test
    void testGettersAndSetters() {
        pet.setId(1);
        pet.setName("Buddy");
        pet.setBirthDate(new Date());

        assertEquals(1, pet.getId());
        assertEquals("Buddy", pet.getName());
        assertNotNull(pet.getBirthDate());
        assertEquals("Dog", pet.getType().getName());
        assertEquals("John", pet.getOwner().getFirstName());
    }

    @Test
    void testToString() {
        pet.setId(1);
        pet.setName("Buddy");
        pet.setBirthDate(new Date());

        String result = pet.toString();
    }

    @Test
    void testEqualsAndHashCode() {
        Pet pet1 = new Pet();
        pet1.setId(1);
        pet1.setName("Buddy");
        pet1.setBirthDate(new Date());
        pet1.setType(petType);
        pet1.setOwner(owner);

        Pet pet2 = new Pet();
        pet2.setId(1);
        pet2.setName("Buddy");
        pet2.setBirthDate(pet1.getBirthDate());
        pet2.setType(petType);
        pet2.setOwner(owner);

        assertEquals(pet1, pet2);
        assertEquals(pet1.hashCode(), pet2.hashCode());

        // Test with different ID
        pet2.setId(2);
        assertNotEquals(pet1, pet2);

        // Test with different Name
        pet2.setId(1);
        pet2.setName("Max");
        assertNotEquals(pet1, pet2);

        // Test with different BirthDate
        pet2.setName("Buddy");
        pet2.setBirthDate(new Date(System.currentTimeMillis() - 100000));
        assertNotEquals(pet1, pet2);

        // Test with different Type
        PetType anotherType = new PetType();
        anotherType.setName("Cat");
        pet2.setBirthDate(pet1.getBirthDate());
        pet2.setType(anotherType);
        assertNotEquals(pet1, pet2);

        // Test with different Owner
        Owner anotherOwner = new Owner();
        anotherOwner.setFirstName("Jane");
        pet2.setType(petType);
        pet2.setOwner(anotherOwner);
        assertNotEquals(pet1, pet2);
        assertNotEquals(pet1, null);
        assertNotEquals(pet1, new Object());
        assertEquals(pet1.hashCode(), pet1.hashCode());
    }
}
