package no.nav.aap.søknadkonsument.rest

import no.nav.aap.rest.tokenx.TokenXConfigMatcher
import no.nav.aap.util.AuthContext
import no.nav.aap.util.LoggerUtil
import no.nav.boot.conditionals.EnvUtil
import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenService
import no.nav.security.token.support.client.spring.ClientConfigurationProperties
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFunction
import reactor.core.publisher.Mono

@Component
class AADFilterFunction  (
        private val configs: ClientConfigurationProperties,
        private val service: OAuth2AccessTokenService,
        private val matcher: TokenXConfigMatcher) : ExchangeFilterFunction {

    private val log = LoggerUtil.getLogger(javaClass)
    private val secureLog = LoggerUtil.getSecureLogger();
    override fun filter(req: ClientRequest, next: ExchangeFunction): Mono<ClientResponse> {
        val url = req.url()
        log.trace("Sjekker token exchange for {}", url)
        val cfg = matcher.findProperties(configs, url)
        if (cfg != null) {
            log.trace(EnvUtil.CONFIDENTIAL, "Gjør token exchange for {} med konfig {}", url, cfg)
            val token = service.getAccessToken(cfg).accessToken
            log.trace("Token exchange for {} OK", url)
            log.trace(EnvUtil.CONFIDENTIAL,"Token er {}",token)
            return next.exchange(
                    ClientRequest.from(req).header(
                            HttpHeaders.AUTHORIZATION,
                            AuthContext.bearerToken(token)).build())
        }
        log.trace("Ingen token exchange for {}", url)
        return next.exchange(ClientRequest.from(req).build())
    }

    override fun toString() = "${javaClass.simpleName} [[configs=$configs,service=$service,matcher=$matcher]"
}