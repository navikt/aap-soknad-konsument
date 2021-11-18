package no.nav.aap.søknadkonsument.joark.pdf

import no.nav.aap.api.søknad.model.UtenlandsSøknadKafka
import org.springframework.stereotype.Service

@Service
class PDFGenerator {

    fun  generate(søknad: UtenlandsSøknadKafka) : ByteArray {
        return byteArrayOf()  // TODO
    }
}