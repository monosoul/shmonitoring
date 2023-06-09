package dev.monosoul.shmonitoring.jackson

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.time.Duration
import java.time.LocalDateTime
import java.util.UUID

fun main() {
    val objectMapper = jsonMapper {
        addModule(kotlinModule())
        addModule(JavaTimeModule())
        disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    // flattened on serialization
    println(
        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            ShmonitoringEventResponse(
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
            "type" : "status-down"
          }
        }
    """.trimIndent()

    val instance1 = objectMapper.readValue<ShmonitoringEventRequest<ServiceStatus>>(input)
    val instance2 = objectMapper.readValue<ShmonitoringEventRequest<ServiceStatus>>(input)

    println(
        instance1 == instance2
    )
}
