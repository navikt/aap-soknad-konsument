
import org.springframework.boot.actuate.info.Info
import org.springframework.boot.actuate.info.InfoContributor
import org.springframework.stereotype.Component
import java.lang.management.ManagementFactory.getRuntimeMXBean
import java.time.Instant
import java.time.ZoneId
import java.util.Map

@Component
class StartupInfoContributor : InfoContributor {
    override fun contribute(builder: Info.Builder) {
        builder.withDetail(
            "nais", Map.of(
                "Startup time",
                Instant.ofEpochMilli(getRuntimeMXBean().startTime).atZone(ZoneId.systemDefault())
                    .toLocalDateTime(),
            )
        )
    }
}