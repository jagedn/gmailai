package es.edn.gmailai.controllers

import io.micronaut.serde.annotation.Serdeable

@Serdeable
class SearchRequest {

    String question

}
