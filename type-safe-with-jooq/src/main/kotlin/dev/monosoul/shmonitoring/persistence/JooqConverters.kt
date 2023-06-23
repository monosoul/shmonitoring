package dev.monosoul.shmonitoring.persistence

import dev.monosoul.shmonitoring.model.EventId
import dev.monosoul.shmonitoring.model.HostName
import dev.monosoul.shmonitoring.model.ServiceName
import dev.monosoul.shmonitoring.model.TeamName
import org.jooq.Converter
import kotlin.reflect.KClass

object JooqConverters {

    private val classToConverter = buildMap {
        converterOf(::EventId, EventId::value)
        converterOf(::HostName, HostName::value)
        converterOf(::ServiceName, ServiceName::value)
        converterOf(::TeamName, TeamName::value)
    }

    @Suppress("UNCHECKED_CAST")
    fun <From, To> get(type: KClass<*>) = classToConverter[type] as Converter<From, To>

    inline fun <From, reified To> get() = get<From, To>(To::class)

    private inline fun <reified From, reified To> ConvertersMap.converterOf(
        noinline fromConverter: (From) -> To,
        noinline toConverter: (To) -> From,
    ) {
        this[To::class] = Converter.of(
            From::class.java,
            To::class.java,
            { it?.let(fromConverter) },
            { it?.let(toConverter) },
        )
    }
}

private typealias ConvertersMap = MutableMap<KClass<*>, Converter<*, *>>
