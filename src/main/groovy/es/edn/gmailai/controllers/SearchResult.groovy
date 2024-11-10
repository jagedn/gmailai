package es.edn.gmailai.controllers

import io.micronaut.serde.annotation.Serdeable

@Serdeable
class SearchResult {

    String id
    String subject
    String body
    String date
    String from
}
