package dev.monosoul.shmonitoring.delegation

import java.time.LocalDateTime

interface ShmonitoringEventBase<out T : ServiceStatus> {
    val timestamp: LocalDateTime
    val hostName: HostName
    val serviceName: ServiceName
    val owningTeamName: TeamName
    val status: T
}

data class ShmonitoringEventRequest<out T : ServiceStatus>(
    override val timestamp: LocalDateTime,
    override val hostName: HostName,
    override val serviceName: ServiceName,
    override val owningTeamName: TeamName,
    override val status: T,
) : ShmonitoringEventBase<T>

data class ShmonitoringEventResponse<out T : ServiceStatus>(
    private val base: ShmonitoringEventRequest<T>,
    val receivedTimestamp: LocalDateTime,
    val id: EventId
) : ShmonitoringEventBase<T> by base
