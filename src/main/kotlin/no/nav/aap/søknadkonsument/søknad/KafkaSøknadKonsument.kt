package no.nav.aap.søknadkonsument.søknad

import no.nav.aap.api.felles.Fødselsnummer
import no.nav.aap.api.felles.UtenlandsSøknadKafka
import no.nav.aap.joark.AvsenderMottaker
import no.nav.aap.joark.Bruker
import no.nav.aap.joark.Dokument
import no.nav.aap.joark.DokumentVariant
import no.nav.aap.joark.Journalpost
import no.nav.aap.søknadkonsument.joark.JoarkClient
import no.nav.aap.søknadkonsument.joark.pdf.PDFGeneratorClient
import no.nav.aap.util.LoggerUtil
import no.nav.aap.util.MDCUtil
import no.nav.aap.util.MDCUtil.NAV_CALL_ID
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component

@Component
class KafkaSøknadKonsument(val joark: JoarkClient, val pdfGen: PDFGeneratorClient) {
    private val log = LoggerUtil.getLogger(javaClass)

    @KafkaListener(
            topics = ["#{'\${utenlands.topic:aap.aap-utland-soknad-sendt.v1}'}"],
            groupId = "#{'\${spring.kafka.consumer.group-id}'}")
    fun konsumer(consumerRecord: ConsumerRecord<String, UtenlandsSøknadKafka>,
                 @Header(NAV_CALL_ID) callId: String) {
        MDCUtil.toMDC(NAV_CALL_ID, callId)
        val fnr = Fødselsnummer(consumerRecord.key())
        val søknad = consumerRecord.value()
        log.trace("WOHOO, fikk søknad $fnr -> $søknad")
        val id = joark.opprettJournalpost(
                Journalpost(
                        dokumenter = docs(søknad),
                        tittel = "Søknad om å beholde AAP ved opphold i utlandet",
                        avsenderMottaker = AvsenderMottaker(id = fnr, navn = søknad.fulltNavn), bruker = Bruker(fnr)))
        log.info("WOHOO, fikk arkivert $id")
    }

    private fun docs(søknad: UtenlandsSøknadKafka): List<Dokument> =
        listOf(
                Dokument(
                        "Søknad om å beholde AAP ved opphold i utlandet",
                        "NAV 11-03.07",
                        listOf(DokumentVariant(fysiskDokument = pdfGen.generate(søknad)))))

}