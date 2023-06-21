package dev.monosoul.shmonitoring.kotlinxserialization

interface NotificationService {
    fun notifyTeam(team: TeamName, warning: WarningMessage)
}
