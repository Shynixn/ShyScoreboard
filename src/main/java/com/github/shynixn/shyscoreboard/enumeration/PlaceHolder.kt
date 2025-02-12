package com.github.shynixn.shyscoreboard.enumeration

import com.github.shynixn.mcutils.common.placeholder.PlaceHolderService
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*

enum class PlaceHolder(
    val text: String,
    val f: ((Player?) -> String?),
) {
    PLAYER_NAME("player_name", { p -> p?.name });

    fun getFullPlaceHolder(plugin: Plugin): String {
        return "%${plugin.name.lowercase(Locale.ENGLISH)}_${text}%"
    }

    companion object {
        /**
         * Registers all placeHolder. Overrides previously registered placeholders.
         */
        fun registerAll(
            plugin: Plugin,
            placeHolderService: PlaceHolderService,
        ) {
            for (placeHolder in PlaceHolder.values()) {
                placeHolderService.register(placeHolder.getFullPlaceHolder(plugin)) { player, _ ->
                    placeHolder.f.invoke(player)
                }
            }
        }
    }
}
