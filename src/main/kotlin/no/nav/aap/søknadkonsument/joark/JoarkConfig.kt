package no.nav.aap.søknadkonsument.joark

import no.nav.aap.søknadkonsument.rest.AbstractRestConfig
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.bind.DefaultValue
import java.net.URI

@ConfigurationProperties(prefix = "joark")
class JoarkConfig @ConstructorBinding constructor(
    @DefaultValue(DEFAULT_PING_PATH)  pingPath: String,
    @DefaultValue("true") enabled: Boolean,
    @DefaultValue(DEFAULT_BASE_URI) baseUri: URI) : AbstractRestConfig(baseUri, pingPath, enabled) {

    override fun toString() = "${javaClass.simpleName} [pingPath=$pingPath,enabled=$isEnabled,baseUri=$baseUri]"

    companion object {
       private const val DEFAULT_BASE_URI = "https://aap-fss-proxy.dev-fss-pub.nais.io/joark/aad"
       private const val DEFAULT_PING_PATH = "/"
    }
}