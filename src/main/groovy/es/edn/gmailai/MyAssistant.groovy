package es.edn.gmailai

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

interface MyAssistant {

    @SystemMessage(fromResource = "system-prompt-template.txt")
    String chat(@UserMessage String userMessage);

}
