package no.nav.aap.søknadkonsument.error

import no.nav.aap.util.AuthContext
import no.nav.security.token.support.core.exceptions.JwtTokenMissingException
import no.nav.security.token.support.spring.validation.interceptor.JwtTokenUnauthorizedException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.WebClientResponseException.BadRequest
import org.springframework.web.reactive.function.client.WebClientResponseException.Forbidden
import org.springframework.web.reactive.function.client.WebClientResponseException.NotFound
import org.springframework.web.reactive.function.client.WebClientResponseException.Unauthorized
import org.zalando.problem.Problem
import org.zalando.problem.Status.BAD_REQUEST
import org.zalando.problem.Status.INTERNAL_SERVER_ERROR
import org.zalando.problem.Status.NOT_FOUND
import org.zalando.problem.Status.UNAUTHORIZED
import org.zalando.problem.spring.web.advice.ProblemHandling


@ControllerAdvice
class AAPKonsumentExceptionHandler(val authContext: AuthContext) : ProblemHandling {

    @ExceptionHandler(JwtTokenUnauthorizedException::class, JwtTokenMissingException::class)
    fun handleMissingOrExpiredToken(e: java.lang.Exception, req: NativeWebRequest): ResponseEntity<Problem> =
        create(UNAUTHORIZED, e, req)

    @ExceptionHandler(WebClientResponseException::class)
    fun handleWebClientResponseException(e: WebClientResponseException, req: NativeWebRequest): ResponseEntity<Problem> {
        return when (e) {
            is BadRequest -> create(BAD_REQUEST,e,req)
            is Forbidden, is Unauthorized -> create(UNAUTHORIZED,e,req)
            is NotFound -> create(NOT_FOUND,e,req)
            else -> create(INTERNAL_SERVER_ERROR,e,req)
        }
    }

    override fun toString() = "${javaClass.simpleName} [authContext=$authContext]"
}