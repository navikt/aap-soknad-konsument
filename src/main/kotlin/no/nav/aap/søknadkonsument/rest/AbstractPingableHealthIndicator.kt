package no.nav.aap.søknadkonsument.rest

import no.nav.aap.søknadkonsument.util.LoggerUtil.getLogger
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator

abstract class AbstractPingableHealthIndicator(private val pingable: Pingable) : HealthIndicator {
    private val log = getLogger(javaClass)
    override fun health(): Health {
        return try {
            log.trace("Pinger {} på {}", pingable.name(), pingable.pingEndpoint())
            pingable.ping()
            up()
        } catch (e: Exception) {
            down(e)
        }
    }

    private fun up(): Health {
        return Health.up()
            .withDetail(pingable.name(), pingable.pingEndpoint())
            .build()
    }

    private fun down(e: Exception): Health {
        log.warn("Kunne ikke pinge {} på {}", pingable.name(), pingable.pingEndpoint(), e)
        return Health.down()
            .withDetail(pingable.name(), pingable.pingEndpoint())
            .withException(e)
            .build()
    }

    override fun toString() = "${javaClass.simpleName} [pingable=$pingable]"
}