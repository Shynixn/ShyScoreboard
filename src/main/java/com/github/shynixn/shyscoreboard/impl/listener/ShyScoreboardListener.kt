package com.github.shynixn.shyscoreboard.impl.listener

import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.shyscoreboard.contract.ScoreboardService
import kotlinx.coroutines.delay
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin

class ShyScoreboardListener(
    private val joinDelayMs: Long,
    private val plugin: Plugin,
    private val scoreboardService: ScoreboardService
) : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        plugin.launch {
            delay(joinDelayMs)
            if (player.isOnline) {
                scoreboardService.updatePlayerScoreboard(player)
            }
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        scoreboardService.clearData(event.player)
    }
}
