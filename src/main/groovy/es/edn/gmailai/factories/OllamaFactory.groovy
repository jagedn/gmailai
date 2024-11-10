package es.edn.gmailai.factories

import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.ollama.OllamaChatModel
import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Requires
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton

import java.time.Duration

@Requires(env = "ollama")
@Factory
@Context
class OllamaFactory {

    @Singleton
    ChatLanguageModel chatModel(
            @Value('${ollama.url}') String url,
            @Value('${ollama.chat.model}') String model,
            @Value('${ollama.chat.temperature:0.6}') double temperature
    ) {
        return OllamaChatModel.builder()
                .baseUrl(url)
                .modelName(model)
                .temperature(temperature)
                .timeout(Duration.ofMinutes(10)).build();
    }

}
