package com.github.shynixn.shyscoreboard.impl.service

import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mcutils.common.repository.CacheRepository
import com.github.shynixn.shyscoreboard.contract.ScoreboardFactory
import com.github.shynixn.shyscoreboard.contract.ScoreboardService
import com.github.shynixn.shyscoreboard.contract.ShyScoreboard
import com.github.shynixn.shyscoreboard.entity.ShyScoreboardMeta
import com.github.shynixn.shyscoreboard.entity.ShyScoreboardSettings
import com.github.shynixn.shyscoreboard.enumeration.Permission
import com.github.shynixn.shyscoreboard.enumeration.ShyScoreboardType
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class ScoreboardServiceImpl(
    private val repository: CacheRepository<ShyScoreboardMeta>,
    private val plugin: Plugin,
    private var settings: ShyScoreboardSettings,
    private val scoreboardFactory: ScoreboardFactory
) :
    ScoreboardService {
    private val scoreboardCache = HashMap<Player, ShyScoreboard>()
    private val priorityScoreboard = HashMap<Player, HashSet<String>>()
    private var isDisposed = false

    init {
        plugin.launch {
            while (!isDisposed) {
                reloadActiveScoreboard()
                delay(settings.checkForPermissionChangeSeconds * 1000L)
            }
        }
    }

    /**
     * Adds a new scoreboard.
     */
    override fun addPriorityScoreboard(player: Player, name: String) {
        if (!priorityScoreboard.containsKey(player)) {
            priorityScoreboard[player] = HashSet()
        }

        priorityScoreboard[player]!!.add(name)
    }

    /**
     * Removes a new scoreboard.
     */
    override fun removePriorityScoreboard(player: Player, name: String) {
        if (priorityScoreboard.containsKey(player)) {
            priorityScoreboard[player]!!.remove(name)
        }
    }

    /**
     * Reloads all scoreboards and configuration.
     */
    override suspend fun reload() {
        repository.clearCache()
        priorityScoreboard.clear()
        val players = priorityScoreboard.keys.toTypedArray()

        for (player in players) {
            clearData(player)
            updatePlayerScoreboard(player)
        }
    }

    /**
     * Clears all allocated data from this player.
     */
    override fun clearData(player: Player) {
        val scoreboard = scoreboardCache.remove(player)
        scoreboard?.close()
        priorityScoreboard.remove(player)
    }

    /**
     * Checks registered scoreboards for a player and may apply one according to settings.
     */
    override suspend fun updatePlayerScoreboard(player: Player) {
        val allScoreboardMetas = repository.getAll()
        val possibleScoreboardMetas = HashSet<ShyScoreboardMeta>()

        // Check first if there are commandScoreboards
        val priorityScoreboards = priorityScoreboard[player]
        if (priorityScoreboards != null) {
            for (priorityScoreboard in priorityScoreboards) {
                val matchingScoreboard = allScoreboardMetas.firstOrNull { e -> e.name.equals(priorityScoreboard, true) }
                if (matchingScoreboard != null) {
                    possibleScoreboardMetas.add(matchingScoreboard)
                }
            }
        }

        // Only take a look at global scoreboards if empty.
        if (possibleScoreboardMetas.isEmpty()) {
            for (scoreboard in allScoreboardMetas.asSequence().filter { e -> e.type == ShyScoreboardType.GLOBAL }) {
                val permission = "${Permission.DYN_SCOREBOARD.text}${scoreboard.name}"

                if (player.hasPermission(permission)) {
                    possibleScoreboardMetas.add(scoreboard)
                }
            }
        }

        // Select scoreboard with the highest priority.
        val selectedScoreboardMeta = possibleScoreboardMetas.minByOrNull { e -> e.priority }
        val previousScoreboard = scoreboardCache[player]

        if (previousScoreboard != null) {
            if (selectedScoreboardMeta == null) {
                // The player has no longer a scoreboard.
                previousScoreboard.close()
                scoreboardCache.remove(player)
                return
            }

            if (previousScoreboard.name == selectedScoreboardMeta.name) {
                // Ignore, scoreboard has not changed.
                return
            }

            // The scoreboard is different
            previousScoreboard.close()
            scoreboardCache.remove(player)
        }

        if (selectedScoreboardMeta != null) {
            val scoreboard = scoreboardFactory.createScoreboard(player, selectedScoreboardMeta)
            scoreboardCache[player] = scoreboard
        }
    }

    /**
     * Gets the scoreboard of a player.
     */
    override fun getScoreboardFromPlayer(player: Player): ShyScoreboard? {
        return scoreboardCache[player]
    }

    /**
     * Closes this resource, relinquishing any underlying resources.
     * This method is invoked automatically on objects managed by the
     * `try`-with-resources statement.
     *
     */
    override fun close() {
        val scoreboards = scoreboardCache.values.toTypedArray()
        for (scoreboard in scoreboards) {
            scoreboards.clone()
        }
        scoreboardCache.clear()
        priorityScoreboard.clear()
        isDisposed = true
    }

    private suspend fun reloadActiveScoreboard() {
        val players = withContext(plugin.globalRegionDispatcher) {
            ArrayList(Bukkit.getOnlinePlayers())
        }

        for (player in players) {
            updatePlayerScoreboard(player)
        }
    }
}
