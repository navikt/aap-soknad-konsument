package no.nav.aap.søknadkonsument.søknad

import no.nav.aap.søknadkonsument.søknad.model.UtenlandsSøknadKafka
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener

class KafkaSøknadKonsument {
    var value: UtenlandsSøknadKafka? = null

    @KafkaListener(topics = ["#{'\${utenlands.topic:aap.aap-utland-soknad-sendt.v1}'}"])
    fun konsumer(consumerRecord: ConsumerRecord<String, UtenlandsSøknadKafka>) {
        value = consumerRecord.value()
    }
}