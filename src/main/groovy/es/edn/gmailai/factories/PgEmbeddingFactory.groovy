package es.edn.gmailai.factories

import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.model.embedding.EmbeddingModel
import dev.langchain4j.model.embedding.onnx.allminilml6v2q.AllMiniLmL6V2QuantizedEmbeddingModel
import dev.langchain4j.rag.content.retriever.ContentRetriever
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever
import dev.langchain4j.store.embedding.EmbeddingStore
import dev.langchain4j.store.embedding.pgvector.DefaultMetadataStorageConfig
import dev.langchain4j.store.embedding.pgvector.MetadataStorageConfig
import dev.langchain4j.store.embedding.pgvector.MetadataStorageMode
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore
import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton

@Factory
@Context
class PgEmbeddingFactory {

    @Singleton
    EmbeddingModel embeddingModel() {
        new AllMiniLmL6V2QuantizedEmbeddingModel()
    }

    @Singleton
    EmbeddingStore<TextSegment> embeddingStore(
            @Value('${embedding.store.host}') String host,
            @Value('${embedding.store.port:5432}') int port,
            @Value('${embedding.store.database}') String database,
            @Value('${embedding.store.user}') String user,
            @Value('${embedding.store.password}') String password,
            @Value('${embedding.store.table}') String table,
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
    ContentRetriever contentRetriever(
            EmbeddingStore<TextSegment> embeddingStore,
            EmbeddingModel embeddingModel,
            @Value('${search.min-score:0.7}') double minScore,
            @Value('${search.max-results:10}') Integer maxResults) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .minScore(minScore)
                .maxResults(maxResults)
                .build()
    }

}
