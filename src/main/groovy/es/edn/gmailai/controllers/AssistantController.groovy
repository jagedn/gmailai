package es.edn.gmailai.controllers

import dev.langchain4j.rag.content.retriever.ContentRetriever
import es.edn.gmailai.MyAssistant
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn

@Controller("/assistant")
class AssistantController {

    private final MyAssistant assistant
    private final ContentRetriever contentRetriever

    AssistantController(MyAssistant assistant, ContentRetriever contentRetriever) {
        this.assistant = assistant
        this.contentRetriever = contentRetriever
    }

    @ExecuteOn(TaskExecutors.BLOCKING)
    @Post("/free")
    String chat(@Body SearchRequest request) {
        assistant.freeChat(request.question)
    }


    @ExecuteOn(TaskExecutors.BLOCKING)
    @Post("/assist")
    String assist(@Body SearchRequest request) {
        assistant.assist(request.question)
    }

}
