package org.springframework.samples.petclinic.genai;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.genai.dto.Vet;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AIFunctionConfigurationTest {

    private final AIFunctionConfiguration config = new AIFunctionConfiguration();

    @Test
    void testListVetsHandlesException() throws JsonProcessingException {
        // Arrange
        AIDataProvider provider = mock(AIDataProvider.class);
        VetRequest request = new VetRequest(new Vet(1, "John", "Doe", null));
        when(provider.getVets(request)).thenThrow(new JsonProcessingException("Test exception") {});

        // Act
        Function<VetRequest, VetResponse> listVetsFunction = config.listVets(provider);
        VetResponse response = listVetsFunction.apply(request);

        // Assert
        assertNull(response);
    }
}
