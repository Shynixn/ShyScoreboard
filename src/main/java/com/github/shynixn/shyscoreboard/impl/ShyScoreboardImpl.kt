package com.github.shynixn.shyscoreboard.impl

import com.github.shynixn.mcutils.packet.api.PacketService
import com.github.shynixn.mcutils.packet.api.packet.PacketOutScoreBoardDestroy
import com.github.shynixn.shyscoreboard.contract.ShyScoreboard
import org.bukkit.entity.Player

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
     * PacketService.
     */
    private var packetService: PacketService?,
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

    /**
     * Performs an immediate update. If you have set a short update interval when creating this scoreboard, you do not need to send update.
     */
    override fun update() {
        checkDisposed()

        TODO("Not yet implemented")
    }

    /**
     * Disposes this scoreboard permanently.
     */
    override fun close() {
        if (packetService != null && playerParam != null) {
            packetService!!.sendPacketOutScoreBoardDestroy(playerParam!!, PacketOutScoreBoardDestroy(id))
        }
        isDisposed = true
        playerParam = null
        packetService = null
    }

    private fun checkDisposed() {
        if (isDisposed) {
            throw IllegalArgumentException("ShyScoreboard is already disposed!")
        }
    }
}
