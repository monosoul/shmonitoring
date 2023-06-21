package dev.monosoul.shmonitoring.jackson

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonCreator.Mode.DELEGATING
import com.fasterxml.jackson.annotation.JsonValue
import dev.monosoul.shmonitoring.Validated
import java.util.UUID

data class HostName @JsonCreator(mode = DELEGATING) constructor(@JsonValue val value: String) {
    init {
        validate(value)?.throwIfInvalid()
    }

    companion object {
        private val HOSTNAME_REGEX = "^DeathStar\\d+$".toRegex()
        private fun validate(value: String) = if (!HOSTNAME_REGEX.matches(value)) {
            Validated.Invalid<HostName>(
                "Host name [$value] is invalid. Should have the following format: \"DeathStar<number>\""
            )
        } else null

        fun validated(value: String): Validated<HostName> = validate(value) ?: Validated.Valid(HostName(value))
    }
}

data class ServiceName @JsonCreator(mode = DELEGATING) constructor(@JsonValue val value: String)

data class TeamName @JsonCreator(mode = DELEGATING) constructor(@JsonValue val value: String)

data class WarningMessage @JsonCreator(mode = DELEGATING) constructor(@JsonValue val value: String)

data class NumberOfProcesses @JsonCreator(mode = DELEGATING) constructor(@JsonValue val value: Int)

data class EventId @JsonCreator(mode = DELEGATING) constructor(@JsonValue val value: UUID)
