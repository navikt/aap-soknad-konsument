package no.nav.aap.api.oppslag

import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("fornavn", "mellomnavn", "etternavn")
data class Navn(val fornavn: String?,val mellomnavn: String?,val etternavn: String?)