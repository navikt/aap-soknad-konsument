package no.nav.aap.søknadkonsument.rest

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import no.nav.aap.rest.ActuatorIgnoringTraceRequestFilter
import no.nav.aap.rest.tokenx.TokenXConfigMatcher
import no.nav.aap.rest.tokenx.TokenXFilterFunction
import no.nav.aap.rest.tokenx.TokenXJacksonModule
import no.nav.aap.util.AuthContext
import no.nav.aap.util.StartupInfoContributor
import no.nav.boot.conditionals.ConditionalOnDevOrLocal
import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenService
import no.nav.security.token.support.client.spring.ClientConfigurationProperties
import no.nav.security.token.support.core.context.TokenValidationContextHolder
import org.springframework.boot.actuate.trace.http.HttpExchangeTracer
import org.springframework.boot.actuate.trace.http.HttpTraceRepository
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.kafka.listener.CommonLoggingErrorHandler
import org.zalando.problem.jackson.ProblemModule
import java.net.URI


@Configuration
class FellesRestBeanConfig {
    @Bean
    fun customizer(): Jackson2ObjectMapperBuilderCustomizer =
        Jackson2ObjectMapperBuilderCustomizer { b: Jackson2ObjectMapperBuilder ->
            b.modules(ProblemModule(), JavaTimeModule(), TokenXJacksonModule(),KotlinModule.Builder().build())
        }

    @Bean
    @ConditionalOnDevOrLocal
    fun httpTraceRepository(): HttpTraceRepository = InMemoryHttpTraceRepository()

    @Bean
    fun swagger() =
        OpenAPI().info(
                Info().title("AAP søknadfordeler")
                    .description("Fordeling av søknader")
                    .version("v0.0.1")
                    .license(License().name("MIT").url("http://www.nav.no")))

    @Bean
    fun errorHandler(): CommonLoggingErrorHandler  = CommonLoggingErrorHandler()

    @Bean
    fun configMatcher() = object : TokenXConfigMatcher {
        override fun findProperties(configs: ClientConfigurationProperties, uri: URI) = configs.registration["clientcredentials"]
    }
    @Bean
    fun authContext(ctxHolder: TokenValidationContextHolder) = AuthContext(ctxHolder)

    @Bean
    fun tokenXFilterFunction(configs: ClientConfigurationProperties, service: OAuth2AccessTokenService, matcher: TokenXConfigMatcher, authContext: AuthContext) = TokenXFilterFunction(configs, service, matcher, authContext)

    @Bean
    @ConditionalOnDevOrLocal
    fun actuatorIgnoringTraceRequestFilter(repo: HttpTraceRepository, tracer: HttpExchangeTracer?) = ActuatorIgnoringTraceRequestFilter(repo,tracer)

    @Bean
    fun startupInfoContributor(ctx: ApplicationContext) =  StartupInfoContributor(ctx)

}