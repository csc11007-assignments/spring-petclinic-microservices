package org.springframework.samples.petclinic.genai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AIBeanConfigurationTest {

    private final AIBeanConfiguration config = new AIBeanConfiguration();

    @Test
    void testChatMemoryBean() {
        // Act
        ChatMemory chatMemory = config.chatMemory();

        // Assert
        assertNotNull(chatMemory);
        assertTrue(chatMemory instanceof InMemoryChatMemory);
    }

    @Test
    void testVectorStoreBean() {
        // Arrange
        EmbeddingModel embeddingModel = mock(EmbeddingModel.class);

        // Act
        VectorStore vectorStore = config.vectorStore(embeddingModel);

        // Assert
        assertNotNull(vectorStore);
        assertTrue(vectorStore instanceof SimpleVectorStore);
    }

    @Test
    void testLoadBalancedWebClientBuilder() {
        // Act
        WebClient.Builder builder = config.loadBalancedWebClientBuilder();

        // Assert
        assertNotNull(builder);
    }
}
