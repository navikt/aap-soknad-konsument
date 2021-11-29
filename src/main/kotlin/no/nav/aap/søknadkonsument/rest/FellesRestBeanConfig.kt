package no.nav.aap.søknadkonsument.rest

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import no.nav.aap.søknadkonsument.rest.aad.OIDCResponseModule
import no.nav.aap.util.AuthContext
import no.nav.aap.util.TimeUtil
import no.nav.boot.conditionals.ConditionalOnDevOrLocal
import no.nav.security.token.support.core.context.TokenValidationContextHolder
import org.springframework.boot.actuate.info.InfoContributor
import org.springframework.boot.actuate.trace.http.HttpExchangeTracer
import org.springframework.boot.actuate.trace.http.HttpTraceRepository
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository
import org.springframework.boot.actuate.web.trace.servlet.HttpTraceFilter
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.stereotype.Component
import org.zalando.problem.jackson.ProblemModule
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest


@Configuration
class FellesRestBeanConfig {
    @Bean
    fun customizer(): Jackson2ObjectMapperBuilderCustomizer =
        Jackson2ObjectMapperBuilderCustomizer { b: Jackson2ObjectMapperBuilder ->
            b.modules(ProblemModule(), JavaTimeModule(), OIDCResponseModule(), KotlinModule.Builder().build())
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


    @ConditionalOnDevOrLocal
    class ActuatorIgnoringTraceRequestFilter(repository: HttpTraceRepository?, tracer: HttpExchangeTracer?) :
        HttpTraceFilter(repository, tracer) {
        @Throws(ServletException::class)
        override fun shouldNotFilter(request: HttpServletRequest): Boolean {
            return request.servletPath.contains("actuator") || request.servletPath.contains("swagger")
        }
    }

    @Bean
    fun authContext(ctxHolder: TokenValidationContextHolder) = AuthContext(ctxHolder)


    @Component
    class StartupInfoContributor(val ctx: ApplicationContext) : InfoContributor {
        override fun contribute(builder: org.springframework.boot.actuate.info.Info.Builder) {
            builder.withDetail(
                    "extra-info", mapOf(
                    "Startup time" to TimeUtil.format(ctx.startupDate)))
        }
    }
}