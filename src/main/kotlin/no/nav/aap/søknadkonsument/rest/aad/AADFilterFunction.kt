package no.nav.aap.søknadkonsument.rest.aad

import no.nav.aap.søknadkonsument.util.AuthContext.Companion.bearerToken
import no.nav.aap.søknadkonsument.util.LoggerUtil.getLogger
import no.nav.aap.søknadkonsument.util.LoggerUtil.getSecureLogger
import no.nav.boot.conditionals.EnvUtil.CONFIDENTIAL
import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenService
import no.nav.security.token.support.client.spring.ClientConfigurationProperties
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFunction
import reactor.core.publisher.Mono


@Component
class AADFilterFunction internal constructor(
    private val configs: ClientConfigurationProperties,
    private val service: OAuth2AccessTokenService,
    private val matcher: AADConfigMatcher) : ExchangeFilterFunction {

    private val log = getLogger(javaClass)
    private val secureLog = getSecureLogger();
    override fun filter(req: ClientRequest, next: ExchangeFunction): Mono<ClientResponse> {
        RequestContextHolder.setRequestAttributes(KafkaRequestScopeAttributes())
        val url = req.url()
        log.trace("Sjekker token exchange for {}", url)
        val cfg = matcher.findProperties(configs, url)
        if (cfg != null) {
            log.trace(CONFIDENTIAL, "Gjør token exchange for {} med konfig {}", url, cfg)
            val token = service.getAccessToken(cfg).accessToken
            log.trace("Token exchange for {} OK", url)
            secureLog.trace("Token er {}",token)
            return next.exchange(ClientRequest.from(req).header(AUTHORIZATION, bearerToken(token)).build()
            )
        }
        log.trace("Ingen token exchange for {}", url)
        RequestContextHolder.resetRequestAttributes();
        return next.exchange(ClientRequest.from(req).build())
    }

    override fun toString() = "${javaClass.simpleName} [[configs=$configs,service=$service,matcher=$matcher]"

    class KafkaRequestScopeAttributes : RequestAttributes {
        private val requestAttributeMap: MutableMap<String, Any> = HashMap()
        override fun getAttribute(name: String, scope: Int): Any? {
            return if (scope == RequestAttributes.SCOPE_REQUEST) {
                requestAttributeMap[name]
            } else null
        }

        override fun setAttribute(name: String, value: Any, scope: Int) {
            if (scope == RequestAttributes.SCOPE_REQUEST) {
                requestAttributeMap[name] = value
            }
        }

        override fun removeAttribute(name: String, scope: Int) {
            if (scope == RequestAttributes.SCOPE_REQUEST) {
                requestAttributeMap.remove(name)
            }
        }

        override fun getAttributeNames(scope: Int): Array<String> {
            return if (scope == RequestAttributes.SCOPE_REQUEST) {
                requestAttributeMap.keys.toTypedArray()
            } else arrayOf()
        }

        override fun registerDestructionCallback(name: String, callback: Runnable, scope: Int) {
            // Not Supported
        }

        override fun resolveReference(key: String): Any? {
            // Not supported
            return null
        }

        override fun getSessionMutex(): Any {
            TODO("Not yet implemented")
        }

        override fun getSessionId(): String {
            TODO("Not yet implemented")
        }
    }
}