package com.github.shynixn.shyscoreboard.contract

import org.bukkit.entity.Player

interface ShyScoreboard {
    /**
     * Name of the meta.
     */
    val name: String

    /**
     * Gets or sets the title.
     */
    var title: String

    /**
     * Gets or sets the scoreboard lines.
     */
    var lines: List<String>

    /**
     * Gets the player using this scoreboard.
     */
    val player: Player

    /**
     * Is this scoreboard disposed.
     */
    val isDisposed: Boolean

    /**
     * Performs an immediate update. If you have set a short update interval when creating this scoreboard, you do not need to send update.
     */
    fun update(respawn: Boolean = false)

    /**
     * Disposes this scoreboard permanently.
     */
    fun close()
}
