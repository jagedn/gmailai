package es.edn.gmailai.controllers

import com.github.javaparser.utils.StringEscapeUtils
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

            var map = headers
            var id = message.id
            var subject = map.find{"subject"=="$it.name".toLowerCase()}?.value ?: 'sin asunto'
            var from = map.find{"from"=="$it.name".toLowerCase()}?.value ?: 'desconocido'
            var date = map.find{"date"=="$it.name".toLowerCase()}?.value ?: '01/01/1999'
            var texto = body
            var plain = htmlToTextTransformer.transform(
                    textDocumentParser.parse( new ByteArrayInputStream(texto.bytes))
            ).text()

            var embedded = TextSegment.from("""
            Asunto: $subject
            De: $from
            Mensaje:
            $plain
            """.stripIndent(), Metadata.from([
                    'id':id,
                    'fecha': date
            ]))

            var embedding = embeddingModel.embed(embedded)

            embeddingStore.add(embedding.content(), embedded)

            count++
        })
        count
    }
}
