package dev.monosoul.shmonitoring.jackson

interface NotificationService {
    fun notifyTeam(team: TeamName, warning: WarningMessage)
}
