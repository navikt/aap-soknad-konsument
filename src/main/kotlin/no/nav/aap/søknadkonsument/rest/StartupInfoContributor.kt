
import org.springframework.boot.actuate.info.Info
import org.springframework.boot.actuate.info.InfoContributor
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class StartupInfoContributor(val ctx: ApplicationContext) : InfoContributor {
    override fun contribute(builder: Info.Builder) {
        builder.withDetail("startup-time", mapOf("Startup time" to ctx.startupDate))
    }
}