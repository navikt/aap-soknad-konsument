package no.nav.aap.søknadkonsument.søknad

import no.nav.aap.api.søknad.model.UtenlandsSøknadKafka
import no.nav.aap.søknadkonsument.joark.*
import no.nav.aap.søknadkonsument.joark.pdf.PDFGeneratorClient
import no.nav.aap.søknadkonsument.util.LoggerUtil
import no.nav.aap.søknadkonsument.util.MDCUtil
import no.nav.aap.søknadkonsument.util.MDCUtil.NAV_CALL_ID
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component
import java.util.*

@Component
class KafkaSøknadKonsument(val joark: JoarkClient, val pdfGen: PDFGeneratorClient) {
    private val log = LoggerUtil.getLogger(javaClass)

    @KafkaListener(topics = ["#{'\${utenlands.topic:aap.aap-utland-soknad-sendt.v1}'}"],  groupId = "#{'\${spring.kafka.consumer.group-id}'}")
    fun konsumer(consumerRecord: ConsumerRecord<String, UtenlandsSøknadKafka>,@Header(NAV_CALL_ID)  callId: String) {
        MDCUtil.toMDC(NAV_CALL_ID,callId)
        val søknad = consumerRecord.value()
        val fnr = consumerRecord.key();
        log.trace("WOHOO, fikk søknad $søknad")
        val id = joark.opprettJournalpost(Journalpost(tilleggsopplysninger = tillegg(), dokumenter = docs(søknad),tema = "AAP", tittel="jalla", avsenderMottaker = AvsenderMottaker(fnr,navn="Gurba"), bruker = Bruker(fnr)))
        log.info("WOHOO, fikk arkivert $id")
    }

    private fun tillegg(): List<Tilleggsopplysning> {
       return listOf(Tilleggsopplysning("a","b"))
    }

    private fun docs(søknad: UtenlandsSøknadKafka): List<Dokument> {
        return listOf(Dokument("a","b", listOf(
            DokumentVariant(
                "a",
                Base64.getEncoder().encodeToString(pdfGen.generate(søknad)),
                "c"
            ))))
    }
}