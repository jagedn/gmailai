package es.edn.gmailai.controllers

import es.edn.gmailai.MyAssistant
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post

@Controller("/assistant")
class AssistantController {

    private final MyAssistant assistant

    AssistantController(MyAssistant assistant) {
        this.assistant = assistant
    }

    @Post("/")
    String chat(String question){
        assistant.chat(question)
    }


}
