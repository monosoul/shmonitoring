package dev.monosoul.shmonitoring.kotlinxserialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Duration

@Serializable
sealed class ServiceStatus {
    abstract override fun toString(): String

    @Serializable
    @SerialName("status-up")
    data class Up(
        @Serializable(with = DurationSerializer::class)
        val upTime: Duration,
        val numberOfProcesses: NumberOfProcesses,
    ) : ServiceStatus()

    @Serializable
    @SerialName("status-warning")
    data class Warning(val message: WarningMessage) : ServiceStatus()

    @Serializable
    @SerialName("status-down")
    object Down : ServiceStatus() {
        override fun toString() = "Down()"
    }
}

private object DurationSerializer : KSerializer<Duration> {
    override val descriptor = PrimitiveSerialDescriptor("java.time.Duration", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): Duration = Duration.parse(decoder.decodeString())
    override fun serialize(encoder: Encoder, value: Duration) = encoder.encodeString(value.toString())
}
