package dev.monosoul.shmonitoring.service

import dev.monosoul.shmonitoring.model.ShmonitoringEventFilter
import dev.monosoul.shmonitoring.model.ShmonitoringEventRequest

class ShmonitoringService {
    private val eventsRepository = mutableListOf<ShmonitoringEventRequest<*>>()

    fun save(event: ShmonitoringEventRequest<*>) = eventsRepository.add(event)
    fun find(filter: ShmonitoringEventFilter) = eventsRepository.filter { event ->
        filter.hostName.typeSafeCondition(event.hostName).equals() &&
                filter.serviceName.typeSafeCondition(event.serviceName).equals() &&
                filter.owningTeamName.typeSafeCondition(event.owningTeamName).equals()
//                filter.owningTeamName.typeSafeCondition(event.serviceName).equals() // will fail to compile
    }

    private class TypeSafeCondition<A, B>(val left: A, val right: B)

    private fun <A, B> A.typeSafeCondition(other: B) = TypeSafeCondition(this, other)

    private fun <T> TypeSafeCondition<out T?, T>.equals() = left?.let { it == right } ?: true
}


