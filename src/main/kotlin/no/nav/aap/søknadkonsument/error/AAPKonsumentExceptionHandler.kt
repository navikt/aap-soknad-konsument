package no.nav.aap.søknadkonsument.error

import no.nav.aap.util.AuthContext
import no.nav.security.token.support.core.exceptions.JwtTokenMissingException
import no.nav.security.token.support.spring.validation.interceptor.JwtTokenUnauthorizedException
import org.springframework.core.env.Environment
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.context.request.NativeWebRequest
import org.zalando.problem.Problem
import org.zalando.problem.Status.NOT_FOUND
import org.zalando.problem.Status.UNAUTHORIZED
import org.zalando.problem.spring.web.advice.ProblemHandling


@ControllerAdvice
class AAPKonsumentExceptionHandler(val authContext: AuthContext, private val env: Environment) : ProblemHandling {

    @ExceptionHandler(JwtTokenUnauthorizedException::class, JwtTokenMissingException::class)
    fun handleMissingOrExpiredToken(e: java.lang.Exception, req: NativeWebRequest): ResponseEntity<Problem> =
        create(UNAUTHORIZED, e, req)

    @ExceptionHandler(HttpClientErrorException.NotFound::class)
    fun handleNotFound(e: java.lang.Exception, req: NativeWebRequest): ResponseEntity<Problem> =
        create(NOT_FOUND, e, req)

    override fun toString() = "${javaClass.simpleName} [authContext=$authContext]"
}