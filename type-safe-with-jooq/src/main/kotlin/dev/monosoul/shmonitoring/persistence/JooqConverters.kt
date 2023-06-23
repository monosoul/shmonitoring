package dev.monosoul.shmonitoring.persistence

import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import dev.monosoul.shmonitoring.model.EventId
import dev.monosoul.shmonitoring.model.HostName
import dev.monosoul.shmonitoring.model.ServiceName
import dev.monosoul.shmonitoring.model.ServiceStatus
import dev.monosoul.shmonitoring.model.TeamName
import org.jooq.Converter
import org.jooq.JSONB
import kotlin.reflect.KClass

object JooqConverters {

    private val JACKSON_OBJECT_MAPPER = jsonMapper {
        addModule(kotlinModule())
        addModule(JavaTimeModule())
        disable(WRITE_DURATIONS_AS_TIMESTAMPS)
        disable(WRITE_DATES_AS_TIMESTAMPS)
    }

    private val classToConverter = buildMap {
        converterOf(::EventId, EventId::value)
        converterOf(::HostName, HostName::value)
        converterOf(::ServiceName, ServiceName::value)
        converterOf(::TeamName, TeamName::value)
        converterOfJsonbTo<ServiceStatus>()
    }

    private inline fun <reified To> ConvertersMap.converterOfJsonbTo() =
        converterOf<JSONB, To>(
            fromConverter = { JACKSON_OBJECT_MAPPER.readValue(it.data()) },
            toConverter = { JSONB.valueOf(JACKSON_OBJECT_MAPPER.writeValueAsString(it)) },
        )

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
