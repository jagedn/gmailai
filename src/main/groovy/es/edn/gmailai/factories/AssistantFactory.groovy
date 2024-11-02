package es.edn.gmailai.factories

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import es.edn.gmailai.MyAssistant;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;

@Factory
class AssistantFactory {

    @Singleton
    ContentRetriever contentRetriever(
            EmbeddingStore<TextSegment> embeddingStore,
            EmbeddingModel embeddingModel,
            @Value('${localai.chat.max-results:10}')Integer maxResults){
        return new EmbeddingStoreContentRetriever(embeddingStore, embeddingModel, maxResults);
    }

    @Singleton
    MyAssistant myAssistant(ChatLanguageModel chatModel,
                            ContentRetriever contentRetriever){
        return AiServices.builder(MyAssistant.class)
                .chatLanguageModel(chatModel)
                .contentRetriever(contentRetriever)
                .build();
    }
}
