package es.edn.gmailai.controllers

import dev.langchain4j.data.document.Document
import dev.langchain4j.data.document.Metadata
import dev.langchain4j.data.document.parser.TextDocumentParser
import dev.langchain4j.data.document.transformer.jsoup.HtmlToTextDocumentTransformer
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.model.embedding.EmbeddingModel
import dev.langchain4j.store.embedding.EmbeddingStore
import es.edn.groogle.GmailService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/gmail")
class GmailController {


    private final GmailService gmailService
    private final EmbeddingStore<TextSegment> embeddingStore
    private final EmbeddingModel embeddingModel

    private final TextDocumentParser textDocumentParser
    private final HtmlToTextDocumentTransformer htmlToTextTransformer

    GmailController(GmailService gmailService, EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {
        this.gmailService = gmailService
        this.embeddingStore = embeddingStore
        this.embeddingModel = embeddingModel
        textDocumentParser = new TextDocumentParser()
        htmlToTextTransformer = new HtmlToTextDocumentTransformer()
    }

    @Get("/between/{afterStr}/{beforeStr}")
    int read(String afterStr, String beforeStr){
        int count=0

        gmailService.eachMessage({
            after afterStr
            before beforeStr
        },{

            var map = headers.collectEntries({
                var key = it.name.toString().toLowerCase()
                var value = it.value.toString()
                Map.of(key, value)
            }) as Map<String, String>

            var subject = map.subject ?: 'sin asunto'
            var from = map.from ?: 'desconocido'
            var date = map.date ?: '01/01/1999'

            var text = body.length() ? body : "sin body"
            var html = Document.from("<html>$body</html>")

            var plain = htmlToTextTransformer.transform(html).text()

            var embedded = TextSegment.from("""            
            recibido el '$date' , remitente '$from', asunto '$subject', body:              
            $plain
            """.stripIndent(), Metadata.from(map))

            var embedding = embeddingModel.embed(embedded)

            embeddingStore.add(embedding.content(), embedded)

            println subject
            count++
        })
        count
    }
}
