package dev.monosoul.shmonitoring.kotlinxserialization

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.time.Duration
import java.time.LocalDateTime
import java.util.UUID

fun main() {
    val objectMapper = Json {
        prettyPrint = true
    }

    // flattened on serialization
    println(
        objectMapper.encodeToString(
            serializer = UnwrappingJsonSerializer(ServiceStatus.serializer()),
            value = ShmonitoringEventResponse(
                base = ShmonitoringEventRequest(
                    LocalDateTime.now(),
                    HostName("DeathStar1"),
                    ServiceName("Laser-beam"),
                    TeamName("Imperial troops"),
                    ServiceStatus.Up(
                        upTime = Duration.ofMillis(1000),
                        numberOfProcesses = NumberOfProcesses(2),
                    )
                ),
                receivedTimestamp = LocalDateTime.now(),
                id = EventId(UUID.randomUUID()),
            )
        )
    )

    // check request instances deserialized from the same input with status down are equal
    // language=JSON
    val input = """
        {
          "timestamp" : "2023-06-01T14:50:57.281480213",
          "hostName" : "DeathStar1",
          "serviceName" : "Laser-beam",
          "owningTeamName" : "Imperial troops",
          "status" : {
            "type" : "down"
          }
        }
    """.trimIndent()

    val instance1 = objectMapper.decodeFromString<ShmonitoringEventRequest<ServiceStatus>>(input)
    val instance2 = objectMapper.decodeFromString<ShmonitoringEventRequest<ServiceStatus>>(input)

    println(
        instance1 == instance2
    )
}
