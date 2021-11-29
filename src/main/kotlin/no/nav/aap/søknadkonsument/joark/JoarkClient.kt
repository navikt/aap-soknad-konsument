package no.nav.aap.søknadkonsument.joark

import no.nav.aap.joark.Journalpost
import org.springframework.stereotype.Component

@Component
class JoarkClient(private val adapter: JoarkClientAdapter) {
    fun opprettJournalpost(journalpost: Journalpost): String? = adapter.opprettJournalpost(journalpost)?.journalpostId
}