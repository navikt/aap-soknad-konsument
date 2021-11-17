package no.nav.aap.søknadkonsument.rest.aad

import no.nav.security.token.support.client.core.ClientProperties
import no.nav.security.token.support.client.spring.ClientConfigurationProperties
import java.net.URI

@FunctionalInterface
interface AADConfigMatcher {
    fun findProperties(configs: ClientConfigurationProperties, uri: URI): ClientProperties?
}