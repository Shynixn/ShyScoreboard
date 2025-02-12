package com.github.shynixn.shyscoreboard.contract

import com.github.shynixn.shyscoreboard.entity.ShyScoreboardMeta
import org.bukkit.entity.Player

interface ScoreboardFactory {
    /**
     * Creates a new scoreboard from the given metadata.
     */
    fun createScoreboard(player: Player, meta: ShyScoreboardMeta): ShyScoreboard
}
