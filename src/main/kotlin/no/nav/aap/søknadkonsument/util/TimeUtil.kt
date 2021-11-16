package no.nav.aap.søknadkonsument.util

import java.time.Instant.ofEpochMilli
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


object TimeUtil {
    fun localDateTime(date: Date): LocalDateTime? {
        return ofEpochMilli(date.time)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }
}