package com.github.shynixn.shyscoreboard.entity

import com.github.shynixn.mcutils.common.repository.Element
import com.github.shynixn.shyscoreboard.enumeration.ShyScoreboardType

class ShyScoreboardMeta : Element {
    /**
     * Unique Identifier of the element.
     */
    override var name: String = ""

    /**
     * Global type.
     */
    var type: ShyScoreboardType = ShyScoreboardType.GLOBAL

    /**
     * Priority.
     */
    var priority: Int = 1

    /**
     * How often this scoreboard is updated.
     */
    var refreshTicks: Int = 20 * 10

    /**
     * Scoreboard title.
     */
    var title: String = ""

    /**
     * Scoreboard lines.
     */
    var lines: List<String> = emptyList()
}
