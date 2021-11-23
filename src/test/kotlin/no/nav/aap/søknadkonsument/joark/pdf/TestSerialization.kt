package no.nav.aap.søknadkonsument.joark.pdf

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.neovisionaries.i18n.CountryCode
import no.nav.aap.api.søknad.model.Navn
import no.nav.aap.søknadkonsument.felles.Fødselsnummer
import no.nav.aap.søknadkonsument.felles.Periode
import org.junit.jupiter.api.Test
import java.time.LocalDate

class TestSerialization {
    @Test
    public fun serialize() {
        var o = ObjectMapper()
        o.registerModule(JavaTimeModule())
        val data = PDFData(Fødselsnummer("11111111111"),CountryCode.UK,Navn("A","B","C"), Periode(LocalDate.now(), LocalDate.now().plusDays(1)))
        println(o.writerWithDefaultPrettyPrinter().writeValueAsString(data))
    }
}