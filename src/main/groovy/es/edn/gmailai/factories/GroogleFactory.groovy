package es.edn.gmailai.factories

import es.edn.groogle.GmailService
import es.edn.groogle.Groogle
import es.edn.groogle.GroogleBuilder
import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
@Context
class GroogleFactory {

    @Singleton
    Groogle groogle(){
        GroogleBuilder.build {
            withOAuthCredentials {
                applicationName 'gmailai'
                scopes "https://mail.google.com/"
                usingCredentials "client_secret.json"
                storeCredentials true
            }
        }
    }

    @Singleton
    GmailService gmailService(Groogle groogle){
        groogle.service(GmailService)
    }

}
