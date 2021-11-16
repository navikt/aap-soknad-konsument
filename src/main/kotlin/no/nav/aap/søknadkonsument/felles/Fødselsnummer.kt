package no.nav.aap.søknadkonsument.felles

import com.fasterxml.jackson.annotation.JsonValue

data class Fødselsnummer(@JsonValue val fnr: String)