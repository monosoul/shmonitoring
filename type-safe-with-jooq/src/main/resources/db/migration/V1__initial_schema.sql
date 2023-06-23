CREATE TABLE events
(
    id                 UUID PRIMARY KEY,
    host_name          TEXT      NOT NULL,
    service_name       TEXT      NOT NULL,
    owning_team_name   TEXT      NOT NULL,
    "timestamp"        TIMESTAMP NOT NULL,
    received_timestamp TIMESTAMP NOT NULL,
    service_status     JSONB     NOT NULL
);
