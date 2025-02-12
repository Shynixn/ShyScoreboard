package com.github.shynixn.shyscoreboard.impl.service

import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mccoroutine.folia.ticks
import com.github.shynixn.mcutils.packet.api.PacketService
import com.github.shynixn.mcutils.packet.api.packet.PacketOutScoreBoardSpawn
import com.github.shynixn.shyscoreboard.contract.ScoreboardFactory
import com.github.shynixn.shyscoreboard.contract.ShyScoreboard
import com.github.shynixn.shyscoreboard.entity.ShyScoreboardMeta
import com.github.shynixn.shyscoreboard.impl.ShyScoreboardImpl
import kotlinx.coroutines.delay
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*

class ScoreboardFactoryImpl(private val plugin: Plugin, private val packetService: PacketService) :
    ScoreboardFactory {
    /**
     * Creates a new scoreboard from the given metadata.
     */
    override fun createScoreboard(player: Player, meta: ShyScoreboardMeta): ShyScoreboard {
        val id = UUID.randomUUID().toString()
        val scoreboard = ShyScoreboardImpl(id, meta.name, player, meta.title, ArrayList(meta.lines), packetService)
        val ticks = meta.refreshTicks.ticks
        packetService.sendPacketOutScoreboardSpawn(player, PacketOutScoreBoardSpawn(id))

        plugin.launch {
            while (!scoreboard.isDisposed) {
                delay(ticks)
                scoreboard.update()
            }
        }

        return scoreboard
    }

    private fun getPlaceHolderResolvedTitleAndLines(player: Player, title: String, lines: List<String>)  {

    }
}
