package dev.monosoul.shmonitoring.service

import dev.monosoul.shmonitoring.model.TeamName
import dev.monosoul.shmonitoring.model.WarningMessage

interface NotificationService {
    fun notifyTeam(team: TeamName, warning: WarningMessage)
}
