
package no.nav.aap.api.søknad.model

import com.neovisionaries.i18n.CountryCode
import no.nav.aap.api.felles.Fødselsnummer

data class UtenlandsSøknadKafka(val søker: Søker, val land: CountryCode, val periode: Periode)

data class Søker(val fnr: Fødselsnummer, val navn: Navn?)

data class Navn(val fornavn: String?,val mellomnavn: String?,val etternavn: String?) {
    fun navn() : String {
        return listOfNotNull(fornavn, mellomnavn, etternavn).joinToString(separator = " ").trim()
    }
}