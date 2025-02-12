package com.github.shynixn.shyscoreboard.impl.service

import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mcutils.common.repository.CacheRepository
import com.github.shynixn.shyscoreboard.contract.ScoreboardFactory
import com.github.shynixn.shyscoreboard.contract.ScoreboardService
import com.github.shynixn.shyscoreboard.contract.ShyScoreboard
import com.github.shynixn.shyscoreboard.entity.ShyScoreboardMeta
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class ScoreboardServiceImpl(
    private val repository: CacheRepository<ShyScoreboardMeta>,
    private val plugin: Plugin,
    private var checkForPermissionChangeSeconds: Int,
    private val scoreboardFactory: ScoreboardFactory
) :
    ScoreboardService {
    private val scoreboardCache = HashMap<Player, ShyScoreboard>()
    private val commandScoreboards = HashMap<Player, HashSet<String>>()
    private var isDisposed = false

    init {
        plugin.launch {
            while (!isDisposed) {
                reloadActiveScoreboard()
                delay(checkForPermissionChangeSeconds * 1000L)
            }
        }
    }

    /**
     * Reloads all scoreboards and configuration.
     */
    override suspend fun reload() {
        repository.clearCache()
        commandScoreboards.clear()
        val players = commandScoreboards.keys.toTypedArray()

        for (player in players) {
            clearData(player)
            updatePlayerScoreboard(player)
        }
    }

    /**
     * Gets the current scoreboard from the given player or null.
     */
    override fun getScoreboardFromPlayer(player: Player): ShyScoreboard? {
        return scoreboardCache[player]
    }

    /**
     * Clears all allocated data from this player.
     */
    override fun clearData(player: Player) {
        val scoreboard = scoreboardCache.remove(player)
        scoreboard?.close()
        commandScoreboards.remove(player)
    }

    /**
     * Checks registered scoreboards for a player and may apply one according to settings.
     */
    override suspend fun updatePlayerScoreboard(player: Player) {
        val allScoreboardMetas = repository.getAll()
        val possibleScoreboardMetas = ArrayList<ShyScoreboardMeta>()

        // Check first if there are commandScoreboards
        val commandScoreboards = commandScoreboards[player]
        if (commandScoreboards != null) {
            for (commandScoreboard in commandScoreboards) {
                val matchingScoreboard = allScoreboardMetas.firstOrNull { e -> e.name.equals(commandScoreboard, true) }
                if (matchingScoreboard != null) {
                    possibleScoreboardMetas.add(matchingScoreboard)
                }
            }
        }

        // Only take a look at global scoreboards if empty.
        if (possibleScoreboardMetas.isEmpty()) {
            for (scoreboard in allScoreboardMetas) {
                val permission = "shyscoreboard.scoreboard.${scoreboard.name}"

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
        commandScoreboards.clear()
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
