package no.nav.aap.søknadkonsument.joark


data class DokumentInfoId(val dokumentInfoId: String)

data class JoarkResponse(val journalpostId: String, val journalpostferdigstilt: Boolean, val dokumenter: List<DokumentInfoId> )