package no.nav.aap.api.felles

import com.fasterxml.jackson.annotation.JsonValue

data class Fødselsnummer(@JsonValue @JvmField val fnr: String)