
import org.springframework.boot.actuate.info.Info
import org.springframework.boot.actuate.info.InfoContributor
import org.springframework.stereotype.Component
import java.lang.management.ManagementFactory.getRuntimeMXBean
import java.time.Instant.ofEpochMilli
import java.time.ZoneId.systemDefault

@Component
class StartupInfoContributor : InfoContributor {
    override fun contribute(builder: Info.Builder) {
        builder.withDetail(
            "nais", mapOf(
                "Startup time" to
                ofEpochMilli(getRuntimeMXBean().startTime).atZone(systemDefault()).toLocalDateTime(),
            )
        )
    }
}