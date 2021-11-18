package no.nav.aap.søknadkonsument.søknad

import no.nav.aap.api.søknad.model.UtenlandsSøknadKafka
import no.nav.aap.søknadkonsument.joark.*
import no.nav.aap.søknadkonsument.util.LoggerUtil
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaSøknadKonsument(val joark: JoarkClient) {
    private val log = LoggerUtil.getLogger(javaClass)
    var value: UtenlandsSøknadKafka? = null

    @KafkaListener(topics = ["#{'\${utenlands.topic:aap.aap-utland-soknad-sendt.v1}'}"],  groupId = "#{'\${spring.kafka.consumer.group-id}'}")
    fun konsumer(consumerRecord: ConsumerRecord<String, UtenlandsSøknadKafka>) {
        value = consumerRecord.value()
        val key = consumerRecord.key();
        log.trace("WOHOO, fikk søknad $value")
        val id = joark.opprettJournalpost(Journalpost(tilleggsopplysninger = tillegg(), dokumenter = docs(),tema = "AAP", behandlingstema = "AAP", tittel="jalla", avsenderMottaker = AvsenderMottaker(key,navn="Gurba"), bruker = Bruker(key)))
        log.info("WOHOO, fikk arkivert $id")
    }

    private fun tillegg(): List<Tilleggsopplysning> {
       return listOf(Tilleggsopplysning("a","b"))
    }

    private fun docs(): List<Dokument> {
        return listOf(Dokument("a","b", listOf(DokumentVariant("a","b","c"))))
    }
}