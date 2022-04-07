package no.nav.aap.søknadkonsument.rest

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.netty.handler.logging.LogLevel.TRACE
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import no.nav.aap.rest.AbstractWebClientAdapter.Companion.correlatingFilterFunction
import no.nav.aap.rest.ActuatorIgnoringTraceRequestFilter
import no.nav.aap.rest.tokenx.TokenXJacksonModule
import no.nav.aap.util.AuthContext
import no.nav.aap.util.StartupInfoContributor
import no.nav.boot.conditionals.ConditionalOnDevOrLocal
import no.nav.boot.conditionals.EnvUtil
import no.nav.security.token.support.client.core.ClientProperties
import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenService
import no.nav.security.token.support.client.spring.ClientConfigurationProperties
import no.nav.security.token.support.client.spring.oauth2.ClientConfigurationPropertiesMatcher
import no.nav.security.token.support.core.context.TokenValidationContextHolder
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.trace.http.HttpExchangeTracer
import org.springframework.boot.actuate.trace.http.HttpTraceRepository
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.boot.info.BuildProperties
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.kafka.listener.CommonLoggingErrorHandler
import org.zalando.problem.jackson.ProblemModule
import reactor.netty.http.client.HttpClient
import reactor.netty.transport.logging.AdvancedByteBufFormat.TEXTUAL
import java.net.URI
import java.util.*


@Configuration
class FellesRestBeanConfig(@Value("\${spring.application.name}") val applicationName: String) {
    @Bean
    fun customizer(): Jackson2ObjectMapperBuilderCustomizer =
        Jackson2ObjectMapperBuilderCustomizer { b: Jackson2ObjectMapperBuilder ->
            b.modules(ProblemModule(), JavaTimeModule(), TokenXJacksonModule(), KotlinModule.Builder().build())
        }

    @Bean
    @ConditionalOnDevOrLocal
    fun httpTraceRepository(): HttpTraceRepository = InMemoryHttpTraceRepository()

    @Bean
    fun swagger(p: BuildProperties) =
        OpenAPI().info(
                Info().title("AAP søknadfordeler")
                    .description("Fordeling av søknader")
                    .version(p.version)
                    .license(License().name("MIT").url("http://www.nav.no")))

    @Bean
    fun errorHandler() = CommonLoggingErrorHandler()

    @Bean
    fun configMatcherProxy() = object : ClientConfigurationPropertiesMatcher {
        override fun findProperties(configs: ClientConfigurationProperties, uri: URI): Optional<ClientProperties> {
            return Optional.ofNullable(configs.registration["clientcredentials-proxy"])
        }
    }

    @Bean
    fun authContext(ctxHolder: TokenValidationContextHolder) = AuthContext(ctxHolder)

    @Bean
    fun aadFilterFunction(configs: ClientConfigurationProperties,
                          service: OAuth2AccessTokenService,
                          matcher: ClientConfigurationPropertiesMatcher,
                          authContext: AuthContext) = AADFilterFunction(configs, service, matcher)

    @Bean
    @ConditionalOnDevOrLocal
    fun actuatorIgnoringTraceRequestFilter(repo: HttpTraceRepository, tracer: HttpExchangeTracer?) =
        ActuatorIgnoringTraceRequestFilter(repo, tracer)

    @Bean
    fun startupInfoContributor(ctx: ApplicationContext) = StartupInfoContributor(ctx)
    @Bean
    fun webClientCustomizer(env: Environment) =
        WebClientCustomizer { b ->
            b.clientConnector(ReactorClientHttpConnector(client(env)))
                .filter(correlatingFilterFunction(applicationName))
        }

    private fun client(env: Environment) =
        if (EnvUtil.isDevOrLocal(env))
            HttpClient.create().wiretap(javaClass.canonicalName, TRACE, TEXTUAL)
        else HttpClient.create()
}