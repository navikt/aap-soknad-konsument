package no.nav.aap.søknadkonsument.joark

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.net.URI

@ConfigurationProperties(prefix = "joark")
class JoarkConfig @ConstructorBinding constructor(
    val baseUri: URI,
)