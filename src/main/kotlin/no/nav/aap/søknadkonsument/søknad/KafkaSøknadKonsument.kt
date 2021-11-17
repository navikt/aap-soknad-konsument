package no.nav.aap.søknadkonsument.søknad

import no.nav.aap.api.søknad.model.UtenlandsSøknadKafka.UtenlandsSøknadKafka
import no.nav.aap.søknadkonsument.joark.JoarkClient
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
        log.info("WOHOO, fikk søknad $value")
       // joark.opprettJournalpost(Journalpost())
    }
}