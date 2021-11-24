package no.nav.aap.søknadkonsument.joark.pdf

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.neovisionaries.i18n.CountryCode
import no.nav.aap.api.søknad.model.Navn
import no.nav.aap.søknadkonsument.felles.Fødselsnummer
import no.nav.aap.søknadkonsument.felles.Periode
import java.time.LocalDate

data class PDFData (val fødselsnummer: Fødselsnummer,
                    @JsonIgnore val land: CountryCode,
                    @get:JsonUnwrapped val navn: Navn?,
                    @get:JsonUnwrapped val periode: Periode,
                    @get:JsonFormat(shape = Shape.STRING, pattern = "dd.MM.yyyy") val dato: LocalDate = LocalDate.now())  {

    @JsonProperty("land")
   fun land() : String {
      return land.toLocale().displayCountry
    }
}