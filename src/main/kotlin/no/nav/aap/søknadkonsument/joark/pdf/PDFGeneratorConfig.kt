package no.nav.aap.søknadkonsument.joark.pdf

import no.nav.aap.søknadkonsument.rest.AbstractRestConfig
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.bind.DefaultValue
import java.net.URI

@ConfigurationProperties(prefix = "pdf")
class PDFGeneratorConfig @ConstructorBinding constructor(
    @DefaultValue(DEFAULT_PING_PATH)  pingPath: String,
    @DefaultValue(DEFAULT_PATH) val  path: String,
    @DefaultValue("true") enabled: Boolean,
    @DefaultValue(DEFAULT_BASE_URI) baseUri: URI) : AbstractRestConfig(baseUri, pingPath, enabled) {

    companion object {
        private const val DEFAULT_BASE_URI = "http://aap-pdfgen"
        private const val DEFAULT_PATH = "api/v1/genpdf/aap-pdfgen/soknad-utland"
        private const val DEFAULT_PING_PATH = "/"
    }
}