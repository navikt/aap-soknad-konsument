
package no.nav.aap.api.søknad.model

import com.neovisionaries.i18n.CountryCode

data class UtenlandsSøknadKafka(val fnr: String, val land: CountryCode, val periode: Periode, val navn: Navn?)

data class Navn(val fornavn: String?,val mellomnavn: String?,val etternavn: String?) {
    fun navn() : String {
        return listOfNotNull(fornavn, mellomnavn, etternavn).joinToString(separator = " ").trim()
    }
}