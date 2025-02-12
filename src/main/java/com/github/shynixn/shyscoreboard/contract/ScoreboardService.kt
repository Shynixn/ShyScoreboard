package com.github.shynixn.shyscoreboard.contract

import org.bukkit.entity.Player

interface ScoreboardService : AutoCloseable {
    /**
     * Reloads all scoreboards and configuration.
     */
    suspend fun reload()

    /**
     * Gets the current scoreboard from the given player or null.
     */
    fun getScoreboardFromPlayer(player: Player): ShyScoreboard?

    /**
     * Clears all allocated data from this player.
     */
    fun clearData(player: Player)

    /**
     * Checks registered scoreboards for a player and may apply one according to settings.
     */
    suspend fun updatePlayerScoreboard(player: Player)
}
