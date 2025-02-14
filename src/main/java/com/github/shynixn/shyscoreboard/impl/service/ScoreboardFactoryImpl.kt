package com.github.shynixn.shyscoreboard.impl.service

import com.github.shynixn.mccoroutine.folia.ticks
import com.github.shynixn.mcutils.common.placeholder.PlaceHolderService
import com.github.shynixn.mcutils.packet.api.PacketService
import com.github.shynixn.shyscoreboard.contract.ScoreboardFactory
import com.github.shynixn.shyscoreboard.contract.ShyScoreboard
import com.github.shynixn.shyscoreboard.entity.ShyScoreboardMeta
import com.github.shynixn.shyscoreboard.impl.ShyScoreboardImpl
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*

class ScoreboardFactoryImpl(
    private val plugin: Plugin,
    private val packetService: PacketService,
    private val placeHolderService: PlaceHolderService
) :
    ScoreboardFactory {
    /**
     * Creates a new scoreboard from the given metadata.
     */
    override fun createScoreboard(player: Player, meta: ShyScoreboardMeta): ShyScoreboard {
        val id = UUID.randomUUID().toString()
        val refreshMilliSeconds = meta.refreshTicks.ticks
        val scoreboard = ShyScoreboardImpl(
            id,
            meta.name,
            player,
            meta.title,
            ArrayList(meta.lines),
            refreshMilliSeconds,
            packetService,
            placeHolderService,
            plugin
        )
        return scoreboard
    }
}
