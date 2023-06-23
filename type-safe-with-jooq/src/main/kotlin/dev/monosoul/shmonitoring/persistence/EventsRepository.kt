package dev.monosoul.shmonitoring.persistence

import dev.monosoul.shmonitoring.generated.tables.records.EventsRecord
import dev.monosoul.shmonitoring.generated.tables.references.EVENTS
import dev.monosoul.shmonitoring.model.EventId
import dev.monosoul.shmonitoring.model.ServiceStatus
import dev.monosoul.shmonitoring.model.ShmonitoringEventFilter
import dev.monosoul.shmonitoring.model.ShmonitoringEventRequest
import dev.monosoul.shmonitoring.model.ShmonitoringEventResponse
import org.jooq.DSLContext
import org.jooq.impl.DSL
import java.time.Clock
import java.time.LocalDateTime
import java.util.UUID

class EventsRepository(
    private val db: DSLContext,
    private val clock: Clock = Clock.systemUTC(),
    private val generateId: () -> EventId = { EventId(UUID.randomUUID()) },
) {
    fun save(event: ShmonitoringEventRequest<ServiceStatus>) {
        db.insertInto(EVENTS)
            .set(event.toRecord())
            .execute()
    }

    fun find(filter: ShmonitoringEventFilter): List<ShmonitoringEventResponse<ServiceStatus>> =
        db.selectFrom(EVENTS)
            .where(filter.toCondition())
            .fetch { it.toResponse() }

    private fun ShmonitoringEventFilter.toCondition() = DSL.and(
        listOfNotNull(
            hostName?.let(EVENTS.HOST_NAME::eq),
            serviceName?.let(EVENTS.SERVICE_NAME::eq),
            owningTeamName?.let(EVENTS.OWNING_TEAM_NAME::eq),
        )
    )

    private fun EventsRecord.toResponse() = ShmonitoringEventResponse(
        base = ShmonitoringEventRequest(timestamp, hostName, serviceName, owningTeamName, serviceStatus),
        receivedTimestamp = receivedTimestamp,
        id = id,
    )

    private fun ShmonitoringEventRequest<*>.toRecord() = EventsRecord(
        generateId(),
        hostName,
        serviceName,
        owningTeamName,
        timestamp,
        LocalDateTime.now(clock),
        status,
    )
}
