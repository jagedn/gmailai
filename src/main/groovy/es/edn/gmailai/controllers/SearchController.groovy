package es.edn.gmailai.controllers

import dev.langchain4j.rag.content.retriever.ContentRetriever
import dev.langchain4j.rag.query.Query
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post

@Controller("/search")
class SearchController {

    private final ContentRetriever contentRetriever

    SearchController(ContentRetriever contentRetriever) {
        this.contentRetriever = contentRetriever
    }

    @Post
    List<SearchResult> search(@Body SearchRequest request) {
        var query = Query.from(request.question)
        var list = contentRetriever.retrieve(query).collect { content ->
            var id = content.textSegment().metadata().getString("id")
            var subject = content.textSegment().metadata().getString("subject")
            var from = content.textSegment().metadata().getString("from")
            var date = content.textSegment().metadata().getString("fecha")
            var body = content.textSegment().text().take(500)
            new SearchResult(id: id, subject: subject, body: body, date: date, from: from)
        }
        return list ?: [new SearchResult(subject: "-", body: "Sin resultados")]
    }

}
