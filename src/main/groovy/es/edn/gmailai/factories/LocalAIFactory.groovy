package es.edn.gmailai.factories;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.localai.LocalAiChatModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.DefaultMetadataStorageConfig;
import dev.langchain4j.store.embedding.pgvector.MetadataStorageConfig;
import dev.langchain4j.store.embedding.pgvector.MetadataStorageMode;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;

import java.time.Duration;

@Factory
class LocalAIFactory {

    @Singleton
    EmbeddingStore<TextSegment> embeddingStore(
            @Value('${localai.store.host}') String host,
            @Value('${localai.store.port:5432}') int port,
            @Value('${localai.store.database}') String database,
            @Value('${localai.store.user}') String user,
            @Value('${localai.store.password}') String password,
            @Value('${localai.store.table}') String table,
            EmbeddingModel model
    ) {
        MetadataStorageConfig metaConfig = DefaultMetadataStorageConfig.builder()
                .columnDefinitions(['metadatas json'])
                .storageMode(MetadataStorageMode.COMBINED_JSON)
                .build();
        return PgVectorEmbeddingStore.builder()
                .host(host)
                .port(port)
                .database(database)
                .user(user)
                .password(password)
                .table(table)
                .dimension(model.dimension())
                .metadataStorageConfig(metaConfig)
                .build();
    }

    @Singleton
    EmbeddingModel embeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }

    @Singleton
    ChatLanguageModel chatModel(
            @Value('${localai.chat.url}') String url,
            @Value('${localai.chat.model}') String model,
            @Value('${localai.chat.temperature:0.7}') double temperature
    ) {
        return LocalAiChatModel.builder()
                .baseUrl(url)
                .modelName(model)
                .temperature(temperature)
                .timeout(Duration.ofMinutes(1))
                .build();
    }


}
