package com.github.shynixn.shyscoreboard.impl

import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mcutils.common.placeholder.PlaceHolderService
import com.github.shynixn.mcutils.packet.api.PacketService
import com.github.shynixn.mcutils.packet.api.packet.PacketOutScoreBoardDestroy
import com.github.shynixn.mcutils.packet.api.packet.PacketOutScoreBoardSpawn
import com.github.shynixn.mcutils.packet.api.packet.PacketOutScoreBoardUpdate
import com.github.shynixn.shyscoreboard.contract.ShyScoreboard
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class ShyScoreboardImpl(
    private val id: String,
    override val name: String,
    private var playerParam: Player?,
    /**
     * Gets or sets the title.
     */
    override var title: String,

    /**
     * Gets or sets the scoreboard lines.
     */
    override var lines: List<String>,

    /**
     * How often refreshed.
     */
    private var refreshMilliSeconds: Long,

    /**
     * PacketService.
     */
    private var packetService: PacketService,
    /**
     * PlaceHolder service.
     */
    private var placeHolderService: PlaceHolderService,
    /**
     * Plugin
     */
    private var plugin: Plugin
) : ShyScoreboard {
    /**
     * Gets the player using this scoreboard.
     */
    override val player: Player
        get() {
            checkDisposed()
            return playerParam!!
        }

    /**
     * Is this scoreboard disposed.
     */
    override var isDisposed: Boolean = false

    init {
        plugin.launch {
            sendSpawnPacket()

            while (!isDisposed) {
                updateAsync()
                delay(refreshMilliSeconds)
            }
        }
    }


    /**
     * Performs an immediate update. If you have set a short update interval when creating this scoreboard, you do not need to send update.
     */
    override fun update(respawn: Boolean) {
        checkDisposed()
        plugin.launch {
            if (respawn) {
                sendDestroyPacket()
                sendSpawnPacket()
            } else {
                updateAsync()
            }
        }
    }

    /**
     * Disposes this scoreboard permanently.
     */
    override fun close() {
        if (playerParam != null) {
            sendDestroyPacket()
        }
        isDisposed = true
        playerParam = null
    }

    private suspend fun updateAsync() {
        val updatePair = resolveTitleAndLines()

        if (isDisposed) {
            return
        }

        packetService.sendPacketOutScoreboardUpdate(
            player,
            PacketOutScoreBoardUpdate(id, updatePair.first, updatePair.second)
        )
    }

    private fun sendDestroyPacket() {
        if (isDisposed) {
            return
        }

        packetService.sendPacketOutScoreBoardDestroy(playerParam!!, PacketOutScoreBoardDestroy(id))
    }

    private suspend fun sendSpawnPacket() {
        val initialPair = resolveTitleAndLines()

        if (isDisposed) {
            return
        }

        packetService.sendPacketOutScoreboardSpawn(
            player,
            PacketOutScoreBoardSpawn(id, initialPair.first, initialPair.second)
        )
    }

    private suspend fun resolveTitleAndLines(): Pair<String, List<String>> {
        return withContext(plugin.globalRegionDispatcher) {
            if (isDisposed) {
                return@withContext Pair(title, emptyList())
            }
            val finalTitle = placeHolderService.resolvePlaceHolder(title, player)
            val finalLines = lines.map { line -> placeHolderService.resolvePlaceHolder(line, player) }
            Pair(finalTitle, finalLines)
        }
    }

    private fun checkDisposed() {
        if (isDisposed) {
            throw IllegalArgumentException("ShyScoreboard is already disposed!")
        }
    }
}
