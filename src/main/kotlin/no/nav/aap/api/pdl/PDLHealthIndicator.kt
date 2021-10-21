package no.nav.aap.api.pdl

import no.nav.aap.api.rest.AbstractPingableHealthIndicator
import org.springframework.stereotype.Component

@Component
class PDLHealthIndicator(adapter: PDLWebClientAdapter) : AbstractPingableHealthIndicator(adapter)