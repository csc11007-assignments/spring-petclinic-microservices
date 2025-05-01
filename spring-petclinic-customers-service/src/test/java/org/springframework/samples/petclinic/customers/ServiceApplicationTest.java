package org.springframework.samples.petclinic.customers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class CustomersServiceApplicationTest {

    @Test
    void testMain() {
        // Mock the SpringApplication class
        ConfigurableApplicationContext context = mock(ConfigurableApplicationContext.class);

        try (MockedStatic<SpringApplication> mockedStatic = mockStatic(SpringApplication.class)) {
            // Setup the mock behavior for static method
            mockedStatic.when(() ->
                SpringApplication.run(eq(CustomersServiceApplication.class), any(String[].class)))
                .thenReturn(context);

            // Call the main method
            CustomersServiceApplication.main(new String[]{});

            // Verify the static method was called with correct parameters
            mockedStatic.verify(() ->
                SpringApplication.run(eq(CustomersServiceApplication.class), any(String[].class)));
        }
    }
}
