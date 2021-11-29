package no.nav.aap.søknadkonsument.rest.aad

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.core.util.VersionUtil.versionFor
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenResponse

class OIDCResponseModule : Module() {
    override fun setupModule(context: SetupContext?) {
        SimpleModule()
            .setMixInAnnotation(OAuth2AccessTokenResponse::class.java, IgnoreUnknownMixin::class.java)
            .setupModule(context)
    }

    override fun getModuleName() = OIDCResponseModule::class.java.simpleName

    @JsonIgnoreProperties(ignoreUnknown = true)
    private interface IgnoreUnknownMixin
    override fun version(): Version? = versionFor(OIDCResponseModule::class.java)
}