package dev.monosoul.shmonitoring.kotlinxserialization

import dev.monosoul.shmonitoring.Validated
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID

@Serializable
@JvmInline
value class HostName(val value: String) {
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

@Serializable
@JvmInline
value class ServiceName(val value: String)

@Serializable
@JvmInline
value class TeamName(val value: String)

@Serializable
@JvmInline
value class WarningMessage(val value: String)

@Serializable
@JvmInline
value class NumberOfProcesses(val value: Int)

@Serializable
@JvmInline
value class EventId(@Serializable(with = UUIDSerializer::class) val value: UUID)

private object UUIDSerializer : KSerializer<UUID> {
    override val descriptor = PrimitiveSerialDescriptor("java.util.UUID", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): UUID = UUID.fromString(decoder.decodeString())
    override fun serialize(encoder: Encoder, value: UUID) = encoder.encodeString(value.toString())
}
