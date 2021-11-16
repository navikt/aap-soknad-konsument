package no.nav.aap.søknadkonsument.joark

import no.nav.aap.søknadkonsument.config.Constants.JOARK
import no.nav.aap.søknadkonsument.rest.AbstractRestConfig.Companion.correlatingFilterFunction
import no.nav.aap.søknadkonsument.rest.AbstractRestConfig.Companion.temaFilterFunction
import no.nav.aap.søknadkonsument.rest.tokenx.TokenXFilterFunction
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
    fun webClientJoark(builder: WebClient.Builder, cfg: JoarkConfig, tokenXFilterFunction: TokenXFilterFunction, env: Environment): WebClient {
        return builder
            .clientConnector(ReactorClientHttpConnector(HttpClient.create().wiretap(isDevOrLocal(env))))
            .baseUrl(cfg.baseUri.toString())
            .filter(correlatingFilterFunction())
            .filter(temaFilterFunction())
            .filter(tokenXFilterFunction)
            .build()
    }
}