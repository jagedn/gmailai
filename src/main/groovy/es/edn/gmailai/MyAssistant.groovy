package es.edn.gmailai

import dev.langchain4j.service.SystemMessage

interface MyAssistant {

    String freeChat(String question);

    @SystemMessage(fromResource = "/system-prompt-template.txt")
    String assist(String question);

}
