package no.nav.aap.søknadkonsument.joark.pdf

import no.nav.aap.rest.AbstractWebClientAdapter.Companion.correlatingFilterFunction
import no.nav.aap.søknadkonsument.joark.pdf.PDFGeneratorConfig.Companion.PDFGEN
import no.nav.boot.conditionals.EnvUtil.isDevOrLocal
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient


@Configuration
class PdfGeneratorkClientConfig {
    @Value("\${spring.application.name}")
    private lateinit var applicationName: String

    @Qualifier(PDFGEN)
    @Bean
    fun webClientPdfGen(builder: WebClient.Builder, cfg: PDFGeneratorConfig, env: Environment) =
        builder
            .codecs { c -> c.defaultCodecs().maxInMemorySize(50 * 1024 * 1024) }
            .clientConnector(ReactorClientHttpConnector(HttpClient.create().wiretap(isDevOrLocal(env))))
            .baseUrl(cfg.baseUri.toString())
            .filter(correlatingFilterFunction(applicationName))
            .build()
}