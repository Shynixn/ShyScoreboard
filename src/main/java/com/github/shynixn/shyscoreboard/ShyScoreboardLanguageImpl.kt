package com.github.shynixn.shyscoreboard

import com.github.shynixn.mcutils.common.language.LanguageItem
import com.github.shynixn.shyscoreboard.contract.ShyScoreboardLanguage

class ShyScoreboardLanguageImpl : ShyScoreboardLanguage {
 override val names: List<String>
  get() = listOf("en_us")
 override var shyScoreboardPlayerNotFoundMessage = LanguageItem("[&9ShyScoreboard&f] &cPlayer %1$1s not found.")

 override var shyScoreboardNoPermissionCommand = LanguageItem("[&9ShyScoreboard&f] &cYou do not have permission to execute this command.")

 override var shyScoreboardReloadCommandHint = LanguageItem("Reloads all scoreboards and configuration.")

 override var shyScoreboardReloadMessage = LanguageItem("[&9ShyScoreboard&f] Reloaded all scoreboards and configuration.")

 override var shyScoreboardCommonErrorMessage = LanguageItem("[&9ShyScoreboard&f]&c A problem occurred. Check the console log for details.")

 override var shyScoreboardCommandSenderHasToBePlayer = LanguageItem("[&9ShyScoreboard&f] The command sender has to be a player if you do not specify the optional player argument.")

 override var shyScoreboardCommandUsage = LanguageItem("[&9ShyScoreboard&f] Use /shyscoreboard help to see more info about the plugin.")

 override var shyScoreboardCommandDescription = LanguageItem("[&9ShyScoreboard&f] All commands for the ShyScoreboard plugin.")

 override var shyScoreboardAddCommandHint = LanguageItem("Adds a scoreboard to a player.")

 override var shyScoreboardSetCommandHint = LanguageItem("Sets a scoreboard to a player.")

 override var shyScoreboardRemoveCommandHint = LanguageItem("Removes a scoreboard from a player.")

 override var shyScoreboardNotFoundMessage = LanguageItem("[&9ShyScoreboard&f] &cScoreboard %1$1s not found.")

 override var shyScoreboardNoPermissionToScoreboardCommand = LanguageItem("[&9ShyScoreboard&f] &cYou do not have permission to this scoreboard.")

 override var shyScoreboardAddedMessage = LanguageItem("[&9ShyScoreboard&f] Added the scoreboard %1$1s to the player %2$1s.")

 override var shyScoreboardRemovedMessage = LanguageItem("[&9ShyScoreboard&f] Removed the scoreboard %1$1s from the player %2$1s.")

 override var shyScoreboardUpdateCommandHint = LanguageItem("Updates the placeholder of the scoreboard.")

 override var shyScoreboardUpdatedMessage = LanguageItem("[&9ShyScoreboard&f] Updated the scoreboard.")

 override var shyScoreboardBooleanNotFoundMessage = LanguageItem("[&9ShyScoreboard&f]&c Only true and false are allowed as values.")
}
