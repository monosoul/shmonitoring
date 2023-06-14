package dev.monosoul.shmonitoring.delegation

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id
import com.fasterxml.jackson.annotation.JsonTypeName
import java.time.Duration

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
sealed class ServiceStatus {
    abstract override fun equals(other: Any?): Boolean
    abstract override fun hashCode(): Int
    abstract override fun toString(): String

    @JsonTypeName("up")
    data class Up(
        val upTime: Duration,
        val numberOfProcesses: NumberOfProcesses,
    ) : ServiceStatus()

    @JsonTypeName("warning")
    data class Warning(val message: WarningMessage) : ServiceStatus()

    @JsonTypeName("down")
    object Down : ServiceStatus() {
        override fun equals(other: Any?): Boolean = javaClass == other?.javaClass
        override fun hashCode(): Int = javaClass.hashCode()
        override fun toString() = "Down()"
    }
}
