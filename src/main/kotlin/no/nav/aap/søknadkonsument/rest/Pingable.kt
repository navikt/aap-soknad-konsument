package no.nav.aap.søknadkonsument.rest

import java.net.URI

interface Pingable {
    fun ping()
    fun pingEndpoint(): URI
    fun name(): String
}