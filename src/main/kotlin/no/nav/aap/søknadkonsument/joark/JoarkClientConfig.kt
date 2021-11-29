package no.nav.aap.søknadkonsument.joark

import no.nav.aap.rest.AbstractWebClientAdapter.Companion.correlatingFilterFunction
import no.nav.aap.rest.AbstractWebClientAdapter.Companion.temaFilterFunction
import no.nav.aap.søknadkonsument.rest.aad.AADFilterFunction
import no.nav.aap.util.Constants.JOARK
import no.nav.boot.conditionals.EnvUtil.isDevOrLocal
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@Configuration
class JoarkClientConfig  {
    @Qualifier(JOARK)
    @Bean
    fun webClientJoark(builder: WebClient.Builder, cfg: JoarkConfig, aadFilterFunction: AADFilterFunction, env: Environment) =
         builder
            .clientConnector(ReactorClientHttpConnector(HttpClient.create().wiretap(isDevOrLocal(env))))
            .baseUrl(cfg.baseUri.toString())
            .filter(correlatingFilterFunction())
            .filter(temaFilterFunction())
            .filter(aadFilterFunction)
            .build()
}