
package no.nav.aap.søknadkonsument.søknad.model

import com.neovisionaries.i18n.CountryCode
import no.nav.aap.søknadkonsument.felles.Periode

data class UtenlandsSøknadView(val land: CountryCode, val periode: Periode)
fun UtenlandsSøknadView.toKafkaObject(fnr: String) = UtenlandsSøknadKafka(fnr, land, periode)
data class UtenlandsSøknadKafka(val fnr: String, val land: CountryCode, val periode: Periode)