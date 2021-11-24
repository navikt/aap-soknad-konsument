package no.nav.aap.søknadkonsument.felles

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape
import java.time.LocalDate

data class Periode(@get:JsonFormat(shape = Shape.STRING, pattern = "dd.MM.yyyy") val fom: LocalDate,
                   @get:JsonFormat(shape = Shape.STRING, pattern = "dd.MM.yyyy") val tom: LocalDate?)