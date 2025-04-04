package com.github.shynixn.shyscoreboard

import com.github.shynixn.mcutils.common.language.LanguageItem
import com.github.shynixn.shyscoreboard.contract.ShyScoreboardLanguage

class ShyScoreboardLanguageImpl : ShyScoreboardLanguage {
 override val names: List<String>
  get() = listOf("en_us")
 override var playerNotFoundMessage = LanguageItem("[&9ShyScoreboard&f] &cPlayer %1$1s not found.")

 override var noPermissionCommand = LanguageItem("[&9ShyScoreboard&f] &cYou do not have permission to execute this command.")

 override var reloadCommandHint = LanguageItem("Reloads all scoreboards and configuration.")

 override var reloadMessage = LanguageItem("[&9ShyScoreboard&f] Reloaded all scoreboards and configuration.")

 override var commonErrorMessage = LanguageItem("[&9ShyScoreboard&f]&c A problem occurred. Check the console log for details.")

 override var commandSenderHasToBePlayer = LanguageItem("[&9ShyScoreboard&f] The command sender has to be a player if you do not specify the optional player argument.")

 override var scoreboardCommandUsage = LanguageItem("[&9ShyScoreboard&f] Use /shyscoreboard help to see more info about the plugin.")

 override var scoreboardCommandDescription = LanguageItem("[&9ShyScoreboard&f] All commands for the ShyScoreboard plugin.")

 override var scoreboardAddCommandHint = LanguageItem("Adds a scoreboard to a player.")

 override var scoreboardRemoveCommandHint = LanguageItem("Removes a scoreboard from a player.")

 override var scoreboardNotFoundMessage = LanguageItem("[&9ShyScoreboard&f] &cScoreboard %1$1s not found.")

 override var scoreboardNoPermissionToScoreboardCommand = LanguageItem("[&9ShyScoreboard&f] &cYou do not have permission to this scoreboard.")

 override var scoreboardAddedMessage = LanguageItem("[&9ShyScoreboard&f] Added the scoreboard %1$1s to the player %2$1s.")

 override var scoreboardRemovedMessage = LanguageItem("[&9ShyScoreboard&f] Removed the scoreboard %1$1s from the player %2$1s.")

 override var scoreboardUpdateCommandHint = LanguageItem("Updates the placeholder of the scoreboard.")

 override var scoreboardUpdatedMessage = LanguageItem("[&9ShyScoreboard&f] Updated the scoreboard.")
}
