package com.techtech.endorra.aichat.agentic;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByCharacterSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@Configuration
public class AgentConfiguration {

    @Bean
    ChatMemoryProvider chatMemoryProvider() 
    {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(20)
                .build();
    }

    @Bean
    EmbeddingModel embeddingModel() 
    {
        return new AllMiniLmL6V2EmbeddingModel();
    }

    @Bean
    EmbeddingStore<TextSegment> embeddingStore(EmbeddingModel embeddingModel,
        ResourceLoader resourceLoader) throws IOException 
        {
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        Resource resource = resourceLoader.getResource("classpath:endorra-terms-of-use.txt");
        if (!resource.exists()) 
        {
            throw new IOException("Demo document not found in classpath: miles-of-smiles-terms-of-use.txt");
        }

        Path tempFile = Files.createTempFile("tos", ".txt");
        try (InputStream inputStream = resource.getInputStream()) 
        {
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        Document document = loadDocument(tempFile, new TextDocumentParser());

        DocumentSplitter documentSplitter = new DocumentByCharacterSplitter(300, 50);

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(documentSplitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        ingestor.ingest(document);

        return embeddingStore;
    }

    @Bean
    ContentRetriever contentRetriever(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) 
    {
        int maxResults = 3;
        double minScore = 0.6;

        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(maxResults)
                .minScore(minScore)
                .build();
    }

    @Bean
    ChatModelListener chatModelLogger() { return new ModelLogger(); }
}