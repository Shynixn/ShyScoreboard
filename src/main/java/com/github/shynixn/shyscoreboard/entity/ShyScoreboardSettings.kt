package com.github.shynixn.shyscoreboard.entity

import com.github.shynixn.shyscoreboard.enumeration.Permission

class ShyScoreboardSettings(private val reloadFun: (ShyScoreboardSettings) -> Unit) {
    /**
     * Delay when joining the server.
     */
    var joinDelaySeconds = 3

    /**
     * Permission change seconds.
     */
    var checkForChangeChangeSeconds = 5

    /**
     * Base Command.
     */
    var baseCommand: String = "shyscoreboard"

    /**
     * Worldguard flag.
     */
    var worldGuardFlag: String = "shyscoreboard"

    /**
     * Command aliases.
     */
    var commandAliases: List<String> = ArrayList()


    var commandPermission: String = Permission.COMMAND.text


    var reloadPermission: String = Permission.RELOAD.text

    var dynScoreboardPermission: String = Permission.DYN_SCOREBOARD.text

    var addPermission: String = Permission.ADD.text

    var setPermission: String = Permission.SET.text

    var removePermission: String = Permission.REMOVE.text

    var updatePermission: String = Permission.UPDATE.text

    var defaultScoreboards: List<Pair<String, String>> = listOf(
        "scoreboard/sample_scoreboard.yml" to "sample_scoreboard.yml"
    )

    /**
     * Reloads the config.
     */
    fun reload() {
        reloadFun.invoke(this)
    }
}
