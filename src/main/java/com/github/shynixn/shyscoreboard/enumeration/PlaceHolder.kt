package com.github.shynixn.shyscoreboard.enumeration

import com.github.shynixn.mcutils.common.placeholder.PlaceHolderService
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*

enum class PlaceHolder(
    val text: String,
    val f: ((Player?, Map<String, Any>) -> String?),
) {
    PLAYER_NAME("player_name", { p, _ -> p?.name }),
    PARAM_1("param_1", { _, context ->
        val item = context["0"] as String?
        item
    }),
    PARAM_2("param_2", { _, context ->
        val item = context["1"] as String?
        item
    });

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
                placeHolderService.register(placeHolder.getFullPlaceHolder(plugin)) { player, context ->
                    placeHolder.f.invoke(player, context)
                }
            }
        }
    }
}
