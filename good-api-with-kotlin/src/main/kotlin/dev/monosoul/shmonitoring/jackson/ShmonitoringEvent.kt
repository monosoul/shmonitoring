package dev.monosoul.shmonitoring.jackson

import com.fasterxml.jackson.annotation.JsonUnwrapped
import java.time.LocalDateTime

data class ShmonitoringEventRequest<out T : ServiceStatus>(
    val timestamp: LocalDateTime,
    val hostName: HostName,
    val serviceName: ServiceName,
    val owningTeamName: TeamName,
    val status: T,
)

data class ShmonitoringEventResponse<out T : ServiceStatus>(
    @field:JsonUnwrapped
    val base: ShmonitoringEventRequest<T>,
    val receivedTimestamp: LocalDateTime,
    val id: EventId
)
