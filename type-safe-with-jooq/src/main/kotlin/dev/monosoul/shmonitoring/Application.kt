package dev.monosoul.shmonitoring

import dev.monosoul.shmonitoring.model.HostName
import dev.monosoul.shmonitoring.model.NumberOfProcesses
import dev.monosoul.shmonitoring.model.ServiceName
import dev.monosoul.shmonitoring.model.ServiceStatus
import dev.monosoul.shmonitoring.model.ShmonitoringEventFilter
import dev.monosoul.shmonitoring.model.ShmonitoringEventRequest
import dev.monosoul.shmonitoring.model.TeamName
import dev.monosoul.shmonitoring.persistence.EventsRepository
import org.flywaydb.core.Flyway
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.postgresql.ds.PGSimpleDataSource
import org.testcontainers.containers.PostgreSQLContainer
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit.MICROS

fun main() {
    PostgreSQLContainer("postgres:14.4-alpine")
        .also { it.start() }
        .use { postgresContainer ->
            val datasource = PGSimpleDataSource().apply {
                setURL(postgresContainer.jdbcUrl)
                user = postgresContainer.username
                password = postgresContainer.password
            }

            Flyway.configure()
                .dataSource(datasource)
                .load()
                .migrate()

            val repository = EventsRepository(
                db = DSL.using(datasource, SQLDialect.POSTGRES)
            )

            val request = ShmonitoringEventRequest(
                LocalDateTime.now().withPostgresPrecision(),
                HostName("DeathStar1"),
                ServiceName("Laser-beam"),
                TeamName("Imperial troops"),
                ServiceStatus.Up(
                    upTime = Duration.ofMillis(1000),
                    numberOfProcesses = NumberOfProcesses(2),
                )
            )

            repository.save(request)

            val response = repository.find(
                ShmonitoringEventFilter(hostName = request.hostName, serviceName = request.serviceName)
            ).first()

            println(response)
            println(response.base == request)
        }
}

private fun LocalDateTime.withPostgresPrecision() = truncatedTo(MICROS)
