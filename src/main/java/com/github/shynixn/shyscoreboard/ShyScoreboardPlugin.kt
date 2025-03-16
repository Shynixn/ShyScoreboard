package com.github.shynixn.shyscoreboard

import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mccoroutine.folia.mcCoroutineConfiguration
import com.github.shynixn.mcutils.common.ChatColor
import com.github.shynixn.mcutils.common.Version
import com.github.shynixn.mcutils.common.checkIfFoliaIsLoadable
import com.github.shynixn.mcutils.common.di.DependencyInjectionModule
import com.github.shynixn.mcutils.common.language.reloadTranslation
import com.github.shynixn.mcutils.common.placeholder.PlaceHolderService
import com.github.shynixn.shyscoreboard.contract.ScoreboardService
import com.github.shynixn.shyscoreboard.entity.ShyScoreboardSettings
import com.github.shynixn.shyscoreboard.enumeration.PlaceHolder
import com.github.shynixn.shyscoreboard.impl.commandexecutor.ShyScoreboardCommandExecutor
import com.github.shynixn.shyscoreboard.impl.listener.ShyScoreboardListener
import com.github.shynixn.shyscoreboard.impl.tmp.WorldGuardService
import com.github.shynixn.shyscoreboard.impl.tmp.WorldGuardServiceImpl
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

class ShyScoreboardPlugin : JavaPlugin() {
    private val prefix: String = ChatColor.BLUE.toString() + "[ShyScoreboard] " + ChatColor.WHITE
    private var module: DependencyInjectionModule? = null
    private var worldGuardService: WorldGuardService? = null

    private

    companion object {
        private val areLegacyVersionsIncluded: Boolean by lazy {
            try {
                Class.forName("com.github.shynixn.shyscoreboard.lib.com.github.shynixn.mcutils.packet.nms.v1_8_R3.PacketSendServiceImpl")
                true
            } catch (e: ClassNotFoundException) {
                false
            }
        }
    }

    override fun onEnable() {
        Bukkit.getServer().consoleSender.sendMessage(prefix + ChatColor.GREEN + "Loading ShyScoreboard ...")
        this.saveDefaultConfig()
        this.reloadConfig()
        val versions = if (areLegacyVersionsIncluded) {
            listOf(
                Version.VERSION_1_8_R3,
                Version.VERSION_1_9_R2,
                Version.VERSION_1_10_R1,
                Version.VERSION_1_11_R1,
                Version.VERSION_1_12_R1,
                Version.VERSION_1_13_R1,
                Version.VERSION_1_13_R2,
                Version.VERSION_1_14_R1,
                Version.VERSION_1_15_R1,
                Version.VERSION_1_16_R1,
                Version.VERSION_1_16_R2,
                Version.VERSION_1_16_R3,
                Version.VERSION_1_17_R1,
                Version.VERSION_1_18_R1,
                Version.VERSION_1_18_R2,
                Version.VERSION_1_19_R1,
                Version.VERSION_1_19_R2,
                Version.VERSION_1_19_R3,
                Version.VERSION_1_20_R1,
                Version.VERSION_1_20_R2,
                Version.VERSION_1_20_R3,
                Version.VERSION_1_20_R4,
                Version.VERSION_1_21_R1,
                Version.VERSION_1_21_R2,
                Version.VERSION_1_21_R3,
            )
        } else {
            listOf(Version.VERSION_1_21_R3)
        }

        if (!Version.serverVersion.isCompatible(*versions.toTypedArray())) {
            logger.log(Level.SEVERE, "================================================")
            logger.log(Level.SEVERE, "ShyScoreboard does not support your server version")
            logger.log(Level.SEVERE, "Install v" + versions[0].from + " - v" + versions[versions.size - 1].to)
            logger.log(Level.SEVERE, "Need support for a particular version? Go to https://www.patreon.com/Shynixn")
            logger.log(Level.SEVERE, "Plugin gets now disabled!")
            logger.log(Level.SEVERE, "================================================")
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }

        logger.log(Level.INFO, "Loaded NMS version ${Version.serverVersion}.")

        if (mcCoroutineConfiguration.isFoliaLoaded && !checkIfFoliaIsLoadable()) {
            logger.log(Level.SEVERE, "================================================")
            logger.log(Level.SEVERE, "ShyScoreboard for Folia requires ShyScoreboard-Premium-Folia.jar")
            logger.log(Level.SEVERE, "Go to https://www.patreon.com/Shynixn to download it.")
            logger.log(Level.SEVERE, "Plugin gets now disabled!")
            logger.log(Level.SEVERE, "================================================")
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }

        // Register Language
        val language = ShyScoreboardLanguageImpl()
        reloadTranslation(language)
        logger.log(Level.INFO, "Loaded language file.")

        // Module
        val plugin = this
        val settings = ShyScoreboardSettings { settings ->
            settings.joinDelaySeconds = plugin.config.getInt("global.joinDelaySeconds")
            settings.checkForChangeChangeSeconds = plugin.config.getInt("global.checkForChangeSeconds")
        }
        settings.reload()
        this.module = ShyScoreboardDependencyInjectionModule(this, settings, language, worldGuardService!!).build()

        // Register PlaceHolders
        PlaceHolder.registerAll(
            this,
            this.module!!.getService<PlaceHolderService>(),
        )

        // Register Listeners
        Bukkit.getPluginManager().registerEvents(module!!.getService<ShyScoreboardListener>(), this)

        // Register CommandExecutor
        module!!.getService<ShyScoreboardCommandExecutor>()
        val scoreboardService = module!!.getService<ScoreboardService>()
        plugin.launch {
            scoreboardService.reload()
            Bukkit.getServer().consoleSender.sendMessage(prefix + ChatColor.GREEN + "Enabled ShyScoreboard " + plugin.description.version + " by Shynixn")
        }
    }

    override fun onLoad() {
        // Register Flags
        worldGuardService = WorldGuardServiceImpl(this)
        worldGuardService!!.registerFlag("shyscoreboard", String::class.java)
    }

    override fun onDisable() {
        if (module == null) {
            return
        }

        module!!.close()
        module = null
    }
}
