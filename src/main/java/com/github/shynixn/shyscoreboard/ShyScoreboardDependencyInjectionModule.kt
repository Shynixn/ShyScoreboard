package com.github.shynixn.shyscoreboard

import com.github.shynixn.fasterxml.jackson.core.type.TypeReference
import com.github.shynixn.mcutils.common.ConfigurationService
import com.github.shynixn.mcutils.common.ConfigurationServiceImpl
import com.github.shynixn.mcutils.common.CoroutinePlugin
import com.github.shynixn.mcutils.common.chat.ChatMessageService
import com.github.shynixn.mcutils.common.di.DependencyInjectionModule
import com.github.shynixn.mcutils.common.placeholder.PlaceHolderService
import com.github.shynixn.mcutils.common.repository.CacheRepository
import com.github.shynixn.mcutils.common.repository.CachedRepositoryImpl
import com.github.shynixn.mcutils.common.repository.Repository
import com.github.shynixn.mcutils.common.repository.YamlFileRepositoryImpl
import com.github.shynixn.mcutils.packet.api.PacketService
import com.github.shynixn.mcutils.packet.impl.service.ChatMessageServiceImpl
import com.github.shynixn.mcutils.packet.impl.service.PacketServiceImpl
import com.github.shynixn.mcutils.worldguard.WorldGuardService
import com.github.shynixn.shyscoreboard.contract.ScoreboardFactory
import com.github.shynixn.shyscoreboard.contract.ScoreboardService
import com.github.shynixn.shyscoreboard.contract.ShyScoreboardLanguage
import com.github.shynixn.shyscoreboard.entity.ShyScoreboardMeta
import com.github.shynixn.shyscoreboard.entity.ShyScoreboardSettings
import com.github.shynixn.shyscoreboard.impl.commandexecutor.ShyScoreboardCommandExecutor
import com.github.shynixn.shyscoreboard.impl.listener.ShyScoreboardListener
import com.github.shynixn.shyscoreboard.impl.service.ScoreboardFactoryImpl
import com.github.shynixn.shyscoreboard.impl.service.ScoreboardServiceImpl
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.ServicePriority

class ShyScoreboardDependencyInjectionModule(
    private val plugin: Plugin,
    private val settings: ShyScoreboardSettings,
    private val language: ShyScoreboardLanguage,
    private val worldGuardService: WorldGuardService,
    private val placeHolderService: PlaceHolderService
) {
    fun build(): DependencyInjectionModule {
        val module = DependencyInjectionModule()

        // Params
        module.addService<Plugin>(plugin)
        module.addService<CoroutinePlugin>(plugin)
        module.addService<ShyScoreboardLanguage>(language)
        module.addService<ShyScoreboardSettings>(settings)
        module.addService<PlaceHolderService>(placeHolderService)
        module.addService<WorldGuardService>(worldGuardService)

        // Repositories
        val templateRepositoryImpl = YamlFileRepositoryImpl<ShyScoreboardMeta>(
            plugin,
            "scoreboard",
            plugin.dataFolder.toPath().resolve("scoreboard"),
            settings.defaultScoreboards,
            emptyList(),
            object : TypeReference<ShyScoreboardMeta>() {})
        val cacheTemplateRepository = CachedRepositoryImpl(templateRepositoryImpl)
        module.addService<Repository<ShyScoreboardMeta>>(cacheTemplateRepository)
        module.addService<CacheRepository<ShyScoreboardMeta>>(cacheTemplateRepository)

        // Services
        module.addService<ShyScoreboardCommandExecutor> {
            ShyScoreboardCommandExecutor(
                module.getService(),
                module.getService(),
                module.getService(),
                module.getService(),
                module.getService(),
                module.getService(),
                module.getService()
            )
        }
        module.addService<ShyScoreboardListener> {
            ShyScoreboardListener(module.getService(), module.getService(), module.getService())
        }
        module.addService<ScoreboardFactory> {
            ScoreboardFactoryImpl(module.getService(), module.getService(), module.getService())
        }
        module.addService<ScoreboardService> {
            ScoreboardServiceImpl(
                module.getService(), module.getService(), module.getService(), module.getService(), module.getService()
            )
        }
        module.addService<ConfigurationService> {
            ConfigurationServiceImpl(module.getService())
        }
        module.addService<PacketService> {
            PacketServiceImpl(module.getService())
        }
        module.addService<ChatMessageService> {
            ChatMessageServiceImpl(module.getService(), module.getService())
        }

        // Developer Api
        Bukkit.getServicesManager().register(
            ScoreboardService::class.java, module.getService<ScoreboardService>(), plugin, ServicePriority.Normal
        )
        Bukkit.getServicesManager().register(
            ScoreboardFactory::class.java, module.getService<ScoreboardFactory>(), plugin, ServicePriority.Normal
        )

        return module
    }
}
