package no.nav.aap.søknadkonsument.joark.pdf

import no.nav.aap.søknadkonsument.joark.pdf.PDFGeneratorConfig.Companion.PDFGEN
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.web.reactive.function.client.WebClient


@Configuration
class PdfGeneratorkClientConfig {
    @Qualifier(PDFGEN)
    @Bean
    fun webClientPdfGen(builder: WebClient.Builder, cfg: PDFGeneratorConfig, env: Environment) =
        builder
            .codecs { c -> c.defaultCodecs().maxInMemorySize(50 * 1024 * 1024) }
            .baseUrl(cfg.baseUri.toString())
            .build()
}