package no.nav.aap.søknadkonsument.rest

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import no.nav.aap.rest.ActuatorIgnoringTraceRequestFilter
import no.nav.aap.rest.tokenx.TokenXJacksonModule
import no.nav.aap.util.AuthContext
import no.nav.aap.util.StartupInfoContributor
import no.nav.boot.conditionals.ConditionalOnDevOrLocal
import no.nav.security.token.support.client.core.ClientProperties
import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenService
import no.nav.security.token.support.client.spring.ClientConfigurationProperties
import no.nav.security.token.support.client.spring.oauth2.ClientConfigurationPropertiesMatcher
import no.nav.security.token.support.core.context.TokenValidationContextHolder
import org.springframework.boot.actuate.trace.http.HttpExchangeTracer
import org.springframework.boot.actuate.trace.http.HttpTraceRepository
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.boot.info.BuildProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.kafka.listener.CommonLoggingErrorHandler
import org.zalando.problem.jackson.ProblemModule
import java.net.URI
import java.util.*


@Configuration
class FellesRestBeanConfig {
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
    fun configMatcher() = object : ClientConfigurationPropertiesMatcher {
        override fun findProperties(configs: ClientConfigurationProperties, uri: URI): Optional<ClientProperties> {
            return Optional.ofNullable(configs.registration[uri.host.split("\\.".toRegex()).toTypedArray()[0]])
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

}