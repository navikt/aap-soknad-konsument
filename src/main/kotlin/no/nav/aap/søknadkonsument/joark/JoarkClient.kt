package no.nav.aap.søknadkonsument.joark

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Component
class JoarkClient(private val client: WebClient) {

    fun opprettJournalpost(journalpost: Journalpost): String {
        return client.post()
            .contentType(APPLICATION_JSON)
            .bodyValue(journalpost)
            .retrieve()
            .onStatus({ obj: HttpStatus -> obj.isError }) { obj: ClientResponse -> obj.createException() }
            .bodyToMono<JoarkResponse>()
            .block()!!.journalpostId
    }
}