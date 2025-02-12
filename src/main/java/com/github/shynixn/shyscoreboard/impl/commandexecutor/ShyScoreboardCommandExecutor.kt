package com.github.shynixn.shyscoreboard.impl.commandexecutor

import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mcutils.common.CoroutineExecutor
import com.github.shynixn.mcutils.common.chat.ChatMessageService
import com.github.shynixn.mcutils.common.command.CommandBuilder
import com.github.shynixn.mcutils.common.command.Validator
import com.github.shynixn.mcutils.common.language.reloadTranslation
import com.github.shynixn.mcutils.common.language.sendPluginMessage
import com.github.shynixn.mcutils.common.repository.CacheRepository
import com.github.shynixn.shyscoreboard.contract.ScoreboardService
import com.github.shynixn.shyscoreboard.contract.ShyScoreboardLanguage
import com.github.shynixn.shyscoreboard.entity.ShyScoreboardMeta
import com.github.shynixn.shyscoreboard.entity.ShyScoreboardSettings
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*

class ShyScoreboardCommandExecutor(
    private val settings: ShyScoreboardSettings,
    private val plugin: Plugin,
    private val scoreboardService: ScoreboardService,
    private val language: ShyScoreboardLanguage,
    chatMessageService: ChatMessageService,
    private val repository: CacheRepository<ShyScoreboardMeta>,
) {

    private val coroutineExecutor = object : CoroutineExecutor {
        override fun execute(f: suspend () -> Unit) {
            plugin.launch(plugin.globalRegionDispatcher) {
                f.invoke()
            }
        }
    }

    private val senderHasToBePlayer: () -> String = {
        language.commandSenderHasToBePlayer.text
    }

    private val playerMustExist = object : Validator<Player> {
        override suspend fun transform(
            sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>
        ): Player? {
            try {
                val playerId = openArgs[0]
                val player = Bukkit.getPlayer(playerId)

                if (player != null) {
                    return player
                }
                return Bukkit.getPlayer(UUID.fromString(playerId))
            } catch (e: Exception) {
                return null
            }
        }

        override suspend fun message(sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>): String {
            return language.playerNotFoundMessage.text.format(openArgs[0])
        }
    }

    private val scoreboardTabs: (suspend (CommandSender) -> List<String>) = {
        repository.getAll().map { e -> e.name }
    }

    private val scoreboardMustExist = object : Validator<ShyScoreboardMeta> {
        override suspend fun transform(
            sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>
        ): ShyScoreboardMeta? {
            return repository.getAll().firstOrNull { e -> e.name.equals(openArgs[0], true) }
        }

        override suspend fun message(sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>): String {
            return language.scoreboardNotFoundMessage.text.format(openArgs[0])
        }
    }

    private val onlinePlayerTabs: (suspend (CommandSender) -> List<String>) = {
        Bukkit.getOnlinePlayers().map { e -> e.name }
    }

    init {
        CommandBuilder(plugin, coroutineExecutor, settings.baseCommand, chatMessageService) {
            usage(language.commandUsage.text)
            description(language.commandDescription.text)
            aliases(settings.commandAliases)
            permission(settings.commandPermission)
            permissionMessage(language.noPermissionCommand.text)
            subCommand("add") {
                permission(settings.addPermission)
                toolTip { language.addCommandHint.text }
                builder().argument("scoreboard").validator(scoreboardMustExist)
                    .tabs(scoreboardTabs).executePlayer(senderHasToBePlayer) { player, scoreboardMeta ->
                        plugin.launch {
                            addScoreboardToPlayer(player, scoreboardMeta, player)
                        }
                    }.argument("player").validator(playerMustExist).tabs(onlinePlayerTabs)
                    .execute { commandSender, scoreboardMeta, player ->
                        plugin.launch {
                            addScoreboardToPlayer(commandSender, scoreboardMeta, player)
                        }
                    }
            }
            subCommand("remove") {
                permission(settings.removePermission)
                toolTip { language.removeCommandHint.text }
                builder().argument("scoreboard").validator(scoreboardMustExist)
                    .tabs(scoreboardTabs).executePlayer(senderHasToBePlayer) { player, scoreboardMeta ->
                        plugin.launch {
                            removeScoreboardFromPlayer(player, scoreboardMeta, player)
                        }
                    }.argument("player").validator(playerMustExist).tabs(onlinePlayerTabs)
                    .execute { commandSender, scoreboardMeta, player ->
                        plugin.launch {
                            removeScoreboardFromPlayer(commandSender, scoreboardMeta, player)
                        }
                    }
            }
            subCommand("reload") {
                permission(settings.reloadPermission)
                toolTip {
                    language.reloadCommandHint.text
                }
                builder().execute { sender ->
                    plugin.saveDefaultConfig()
                    plugin.reloadConfig()
                    plugin.reloadTranslation(language)
                    scoreboardService.reload()
                    sender.sendPluginMessage(language.reloadMessage)
                }
            }.helpCommand()
        }.build()
    }

    private fun addScoreboardToPlayer(
        sender: CommandSender,
        scoreboardMeta: ShyScoreboardMeta,
        player: Player
    ) {
        if (!player.hasPermission("${settings.dynScoreboardPermission}${scoreboardMeta.name}")) {
            sender.sendPluginMessage(language.noPermissionToScoreboardCommand)
            return
        }

        scoreboardService.addPriorityScoreboard(player, scoreboardMeta.name)
        sender.sendPluginMessage(language.scoreboardAddedMessage, scoreboardMeta.name, player.name)
    }

    private fun removeScoreboardFromPlayer(
        sender: CommandSender,
        scoreboardMeta: ShyScoreboardMeta,
        player: Player
    ) {
        scoreboardService.removePriorityScoreboard(player, scoreboardMeta.name)
        sender.sendPluginMessage(language.scoreboardRemovedMessage, scoreboardMeta.name, player.name)
    }
}
