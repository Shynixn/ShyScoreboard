package com.github.shynixn.shyscoreboard.contract

import com.github.shynixn.mcutils.common.language.LanguageItem
import com.github.shynixn.mcutils.common.language.LanguageProvider

interface ShyScoreboardLanguage : LanguageProvider {
  var playerNotFoundMessage: LanguageItem

  var noPermissionCommand: LanguageItem

  var commandUsage: LanguageItem

  var commandDescription: LanguageItem

  var commandSenderHasToBePlayer: LanguageItem

  var reloadCommandHint: LanguageItem

  var reloadMessage: LanguageItem

  var commonErrorMessage: LanguageItem

  var addCommandHint: LanguageItem

  var removeCommandHint: LanguageItem

  var scoreboardNotFoundMessage: LanguageItem

  var noPermissionToScoreboardCommand: LanguageItem

  var scoreboardAddedMessage: LanguageItem

  var scoreboardRemovedMessage: LanguageItem

  var updateCommandHint: LanguageItem

  var updatedMessage: LanguageItem
}
