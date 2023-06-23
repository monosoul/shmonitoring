package dev.monosoul.shmonitoring.persistence

import dev.monosoul.shmonitoring.generated.tables.Events
import dev.monosoul.shmonitoring.generated.tables.records.EventsRecord
import dev.monosoul.shmonitoring.model.ServiceStatus
import dev.monosoul.shmonitoring.model.ShmonitoringEventRequest
import org.jooq.DSLContext
import java.time.Clock
import java.time.LocalDateTime
import java.util.UUID

class EventsRepository(
    private val db: DSLContext,
    private val clock: Clock = Clock.systemUTC(),
    private val generateId: () -> UUID = UUID::randomUUID,
) {
    fun save(event: ShmonitoringEventRequest<ServiceStatus>) {
        db.insertInto(Events.EVENTS)
            .set(event.toRecord())
            .execute()
    }

    private fun ShmonitoringEventRequest<*>.toRecord() = EventsRecord(
        generateId(),
        hostName.value,
        serviceName.value,
        owningTeamName.value,
        timestamp,
        LocalDateTime.now(clock),
    )
}
