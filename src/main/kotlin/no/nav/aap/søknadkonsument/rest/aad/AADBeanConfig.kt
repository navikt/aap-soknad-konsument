package no.nav.aap.søknadkonsument.rest.aad

import no.nav.security.token.support.client.core.ClientProperties
import no.nav.security.token.support.client.spring.ClientConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.URI


@Configuration
class AADBeanConfig {
    @Bean
    fun configMatcher() = object : AADConfigMatcher {
        override fun findProperties(configs: ClientConfigurationProperties, uri: URI): ClientProperties? {
            return configs.registration["clientcredentials"]
        }
    }
}