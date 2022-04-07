package no.nav.aap.søknadkonsument.joark

import no.nav.aap.health.AbstractPingableHealthIndicator
import no.nav.aap.rest.AbstractWebClientAdapter.Companion.temaFilterFunction
import no.nav.aap.søknadkonsument.rest.AADFilterFunction
import no.nav.aap.util.Constants.JOARK
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class JoarkClientConfig {

    @Qualifier(JOARK)
    @Bean
    fun webClientJoark(builder: WebClient.Builder,
                       cfg: JoarkConfig,
                       aadFilterFunction: AADFilterFunction,
                       env: Environment) =
        builder
            .baseUrl(cfg.baseUri.toString())
            .filter(temaFilterFunction())
            .filter(aadFilterFunction)
            .build()

    @Bean
    fun joarkHealthIndicator(adapter: JoarkClientAdapter) = object : AbstractPingableHealthIndicator(adapter) {
    }
}