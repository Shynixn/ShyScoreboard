package com.github.shynixn.shyscoreboard.contract

import org.bukkit.entity.Player

interface ScoreboardService : AutoCloseable {
    /**
     * Reloads all scoreboards and configuration.
     */
    suspend fun reload()

    /**
     * Clears all allocated data from this player.
     */
    fun clearData(player: Player)

    /**
     * Checks registered scoreboards for a player and may apply one according to settings.
     */
    suspend fun updatePlayerScoreboard(player: Player)

    /**
     * Gets the scoreboard of a player.
     */
    fun getScoreboardFromPlayer(player: Player): ShyScoreboard?

    /**
     * Adds a new scoreboard.
     */
    fun addCommandScoreboard(player: Player, name: String)

    /**
     * Removes a new scoreboard.
     */
    fun removeCommandScoreboard(player: Player, name: String)
}
