package no.nav.aap.søknadkonsument.joark.pdf

import no.nav.aap.api.søknad.model.UtenlandsSøknadKafka
import no.nav.aap.søknadkonsument.config.Constants.PDFGEN
import no.nav.aap.søknadkonsument.rest.AbstractWebClientAdapter
import no.nav.aap.søknadkonsument.util.LoggerUtil
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Component
class PDFGeneratorAdapter(@Qualifier(PDFGEN) client: WebClient, val cf: PDFGeneratorConfig) : AbstractWebClientAdapter(client,cf){
    private val log = LoggerUtil.getLogger(javaClass)

    fun  generate(søknad: UtenlandsSøknadKafka) : ByteArray {
        log.debug("Creating PDF from $søknad via ${cf.baseUri}")
        return webClient.post()
            .uri { it.path(cf.path).build() }
            .contentType(APPLICATION_JSON)
            .bodyValue(søknad)
            .retrieve()
            .onStatus({ obj: HttpStatus -> obj.isError }) { obj: ClientResponse -> obj.createException() }
            .bodyToMono<ByteArray>()
            .block() ?: throw RuntimeException("PDF could not be generated")

    }
}