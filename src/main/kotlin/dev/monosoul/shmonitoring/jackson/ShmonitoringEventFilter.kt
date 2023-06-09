package dev.monosoul.shmonitoring.jackson

data class ShmonitoringEventFilter(
    val hostName: HostName? = null,
    val serviceName: ServiceName? = null,
    val owningTeamName: TeamName? = null,
)
