package es.edn.gmailai.factories


import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.rag.content.retriever.ContentRetriever
import dev.langchain4j.service.AiServices
import es.edn.gmailai.MyAssistant
import es.edn.groogle.GmailService
import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
@Context
class AssistantFactory {

    @Singleton
    MyAssistant myAssistant(ChatLanguageModel chatModel,
                            ContentRetriever contentRetriever,
                            GmailService gmailService) {
        return AiServices.builder(MyAssistant.class)
                .chatLanguageModel(chatModel)
                .contentRetriever(contentRetriever)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
        //.tools(new GmailTool(gmailService))
                .build();
    }
}
