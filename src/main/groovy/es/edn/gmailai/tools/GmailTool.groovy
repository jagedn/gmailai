package es.edn.gmailai.tools

import dev.langchain4j.agent.tool.P
import dev.langchain4j.agent.tool.Tool
import es.edn.groogle.GmailService

class GmailTool {

    private final GmailService gmailService

    GmailTool(GmailService gmailService) {
        this.gmailService = gmailService
    }

    @Tool("envia un texto a un correo electronico")
    String sendEmail(@P("email") String email, @P("texto") String texto) {
        texto = "enviar un correo a $email con el resument $texto"
        gmailService.sendEmail {
            from "me"
            to "groovy.groogle@gmail.com"
            subject "desde la tool"
            body texto
        }
        "se ha enviado un resumen a $email"
    }


}
