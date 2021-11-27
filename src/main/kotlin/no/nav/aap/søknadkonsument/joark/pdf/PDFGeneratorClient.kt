package no.nav.aap.søknadkonsument.joark.pdf

import no.nav.aap.api.søknad.model.UtenlandsSøknadKafka
import org.springframework.stereotype.Component
import java.util.Base64.getEncoder

@Component
class PDFGeneratorClient(val adapter: PDFGeneratorAdapter)  {
    fun generate(søknad: UtenlandsSøknadKafka)  = getEncoder().encodeToString(adapter.generate(søknad))
}