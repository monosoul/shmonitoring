package dev.monosoul.shmonitoring.kotlinxserialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind.STRING
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import java.time.LocalDateTime

@Serializable
data class ShmonitoringEventRequest<out T : ServiceStatus>(
    @Serializable(with = LocalDateTimeSerializer::class)
    val timestamp: LocalDateTime,
    val hostName: HostName,
    val serviceName: ServiceName,
    val owningTeamName: TeamName,
    val status: T,
)

@Serializable
data class ShmonitoringEventResponse<out T : ServiceStatus>(
    val base: ShmonitoringEventRequest<T>,
    @Serializable(with = LocalDateTimeSerializer::class)
    val receivedTimestamp: LocalDateTime,
    val id: EventId
)

private object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor = PrimitiveSerialDescriptor("java.time.LocalDateTime", STRING)
    override fun deserialize(decoder: Decoder): LocalDateTime = LocalDateTime.parse(decoder.decodeString())
    override fun serialize(encoder: Encoder, value: LocalDateTime) = encoder.encodeString(value.toString())
}

class UnwrappingJsonSerializer<T : ServiceStatus>(
    statusSerializer: KSerializer<T>
) : JsonTransformingSerializer<ShmonitoringEventResponse<T>>(
    ShmonitoringEventResponse.serializer(statusSerializer)
) {
    override fun transformSerialize(element: JsonElement) = buildJsonObject {
        element.jsonObject.forEach { (propertyName, value) ->
            if (propertyName == ShmonitoringEventResponse<*>::base.name) {
                value.jsonObject.forEach(::put)
            } else {
                put(propertyName, value)
            }
        }
    }
}
