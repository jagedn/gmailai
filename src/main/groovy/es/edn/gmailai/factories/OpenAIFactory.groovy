package es.edn.gmailai.factories

import dev.langchain4j.model.azure.AzureOpenAiChatModel
import dev.langchain4j.model.azure.AzureOpenAiStreamingChatModel
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.chat.StreamingChatLanguageModel
import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Requires
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton

@Requires(env = "openai")
@Factory
@Context
class OpenAIFactory {

    @Singleton
    ChatLanguageModel chatModel(@Value('${openai.endpoint}') String endpoint,
                                @Value('${openai.key}') String key,
                                @Value('${openai.chat-deployment}') String deployment) {
        return AzureOpenAiChatModel.builder()
                .apiKey(key)
                .endpoint(endpoint)
                .deploymentName(deployment)
                .build();
    }


    @Singleton
    StreamingChatLanguageModel streamingChatLanguageModel(@Value('${openai.endpoint}') String endpoint,
                                                          @Value('${openai.key}') String key,
                                                          @Value('${openai.chat-deployment}') String deployment) {
        return AzureOpenAiStreamingChatModel.builder()
                .apiKey(key)
                .endpoint(endpoint)
                .deploymentName(deployment)
                .build();

    }
}
