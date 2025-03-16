package com.github.shynixn.shyscoreboard.impl.service

import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mccoroutine.folia.ticks
import com.github.shynixn.mcutils.common.repository.CacheRepository
import com.github.shynixn.mcutils.worldguard.WorldGuardService
import com.github.shynixn.shyscoreboard.contract.ScoreboardFactory
import com.github.shynixn.shyscoreboard.contract.ScoreboardService
import com.github.shynixn.shyscoreboard.contract.ShyScoreboard
import com.github.shynixn.shyscoreboard.entity.ShyScoreboardMeta
import com.github.shynixn.shyscoreboard.entity.ShyScoreboardSettings
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
    private val scoreboardFactory: ScoreboardFactory,
    private val worldGuardService: WorldGuardService
) : ScoreboardService {
    private val scoreboardCache = HashMap<Player, ShyScoreboard>()
    private val commandScoreboards = HashMap<Player, HashSet<String>>()
    private var isDisposed = false

    init {
        plugin.launch {
            while (!isDisposed) {
                reloadActiveScoreboard()
                delay(settings.checkForChangeChangeSeconds * 1000L)
            }
        }
    }

    /**
     * Adds a new scoreboard.
     */
    override fun addCommandScoreboard(player: Player, name: String) {
        if (!commandScoreboards.containsKey(player)) {
            commandScoreboards[player] = HashSet()
        }

        if (!commandScoreboards[player]!!.contains(name)) {
            commandScoreboards[player]!!.add(name)
            plugin.launch {
                updatePlayerScoreboard(player)
            }
        }
    }

    /**
     * Removes a new scoreboard.
     */
    override fun removeCommandScoreboard(player: Player, name: String) {
        if (commandScoreboards.containsKey(player)) {
            commandScoreboards[player]!!.remove(name)
            plugin.launch {
                updatePlayerScoreboard(player)
            }
        }
    }

    /**
     * Reloads all scoreboards and configuration.
     */
    override suspend fun reload() {
        repository.clearCache()
        commandScoreboards.clear()
        repository.getAll()
        val players = scoreboardCache.keys.toTypedArray()

        for (player in players) {
            clearData(player)
        }
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
        val flags = withContext(plugin.globalRegionDispatcher) {
            val flagValue =
                worldGuardService.getFlagValue<String>(player, settings.worldGuardFlag, player.location)
            val flags = ArrayList<String>()
            if (flagValue != null) {
                flags.add(flagValue)
            }
            flags
        }
        val allScoreboardMetas = repository.getAll()
        updatePlayerScoreboard(player, allScoreboardMetas, flags)
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
        commandScoreboards.clear()
        isDisposed = true
    }

    private fun updatePlayerScoreboard(
        player: Player, allScoreboardMetas: List<ShyScoreboardMeta>, regionFlags: List<String>
    ) {
        val possibleScoreboardMetas = HashSet<ShyScoreboardMeta>()

        // Check first if there are commandScoreboards
        val priorityScoreboards = commandScoreboards[player]
        if (priorityScoreboards != null) {
            for (priorityScoreboard in priorityScoreboards) {
                val matchingScoreboard = allScoreboardMetas.firstOrNull { e -> e.name.equals(priorityScoreboard, true) }
                if (matchingScoreboard != null) {
                    possibleScoreboardMetas.add(matchingScoreboard)
                }
            }
        }

        // Then check WorldGuard region flags.
        if (possibleScoreboardMetas.isEmpty()) {
            for (scoreboard in allScoreboardMetas.asSequence()
                .filter { e -> e.type == ShyScoreboardType.WORLDGUARD && regionFlags.contains(e.name) }) {
                possibleScoreboardMetas.add(scoreboard)
            }
        }

        // Only take a look at global scoreboards if empty.
        if (possibleScoreboardMetas.isEmpty()) {
            for (scoreboard in allScoreboardMetas.asSequence().filter { e -> e.type == ShyScoreboardType.GLOBAL }) {
                val permission = "${settings.dynScoreboardPermission}${scoreboard.name}"

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

    private suspend fun reloadActiveScoreboard() {
        val playerContainers = withContext(plugin.globalRegionDispatcher) {
            val chunked = ArrayList(Bukkit.getOnlinePlayers()).chunked(30)
            var shouldWait = false
            val result = ArrayList<Pair<Player, List<String>>>()

            for (chunk in chunked) {
                if (shouldWait) {
                    delay(1.ticks)
                }

                for (player in chunk) {
                    val flagValue =
                        worldGuardService.getFlagValue<String>(player, settings.worldGuardFlag, player.location)
                    val flags = ArrayList<String>()
                    if (flagValue != null) {
                        flags.add(flagValue)
                    }
                    result.add(player to flags)
                }

                shouldWait = true
            }
            result
        }

        val allScoreboardMetas = repository.getAll()
        for (playerContainer in playerContainers) {
            updatePlayerScoreboard(playerContainer.first, allScoreboardMetas, playerContainer.second)
        }
    }
}
