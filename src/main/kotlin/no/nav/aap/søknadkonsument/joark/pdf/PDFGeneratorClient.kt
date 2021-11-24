package no.nav.aap.søknadkonsument.joark.pdf

import no.nav.aap.api.søknad.model.UtenlandsSøknadKafka
import no.nav.aap.søknadkonsument.rest.RetryAware
import no.nav.aap.søknadkonsument.util.LoggerUtil
import org.springframework.stereotype.Component

@Component
class PDFGeneratorClient(val adapter: PDFGeneratorAdapter) : RetryAware {
    private val log = LoggerUtil.getLogger(javaClass)

    fun  generate(søknad: UtenlandsSøknadKafka) : ByteArray {
        log.debug("Lager PDF fra $søknad.")
        return adapter.generate(søknad)
    }
}