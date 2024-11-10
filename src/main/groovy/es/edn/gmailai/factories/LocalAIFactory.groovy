package es.edn.gmailai.factories

import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.chat.StreamingChatLanguageModel
import dev.langchain4j.model.localai.LocalAiChatModel
import dev.langchain4j.model.localai.LocalAiStreamingChatModel
import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Requires
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton

import java.time.Duration

@Requires(env = "localai")
@Factory
@Context
class LocalAIFactory {

    @Singleton
    ChatLanguageModel chatModel(
            @Value('${localai.url}') String url,
            @Value('${localai.chat.model}') String model,
            @Value('${localai.chat.temperature:0.2}') double temperature
    ) {
        return LocalAiChatModel.builder()
                .baseUrl(url)
                .modelName(model)
                .temperature(temperature)
                .timeout(Duration.ofMinutes(10))
                .build()
    }

    @Singleton
    StreamingChatLanguageModel streamingChatLanguageModel(@Value('${localai.url}') String url,
                                                          @Value('${localai.chat.model}') String model,
                                                          @Value('${localai.chat.temperature:0.2}') double temperature) {
        return LocalAiStreamingChatModel.builder()
                .baseUrl(url)
                .modelName(model)
                .temperature(temperature)
                .timeout(Duration.ofMinutes(10))
                .build()
    }
}
