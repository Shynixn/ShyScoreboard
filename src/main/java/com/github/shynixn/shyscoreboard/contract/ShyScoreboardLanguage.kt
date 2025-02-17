package com.github.shynixn.shyscoreboard.contract

import com.github.shynixn.mcutils.common.language.LanguageItem
import com.github.shynixn.mcutils.common.language.LanguageProvider

interface ShyScoreboardLanguage : LanguageProvider {
  var playerNotFoundMessage: LanguageItem

  var noPermissionCommand: LanguageItem

  var reloadCommandHint: LanguageItem

  var reloadMessage: LanguageItem

  var commonErrorMessage: LanguageItem

  var commandSenderHasToBePlayer: LanguageItem

  var scoreboardCommandUsage: LanguageItem

  var scoreboardCommandDescription: LanguageItem

  var scoreboardAddCommandHint: LanguageItem

  var scoreboardRemoveCommandHint: LanguageItem

  var scoreboardNotFoundMessage: LanguageItem

  var scoreboardNoPermissionToScoreboardCommand: LanguageItem

  var scoreboardAddedMessage: LanguageItem

  var scoreboardRemovedMessage: LanguageItem

  var scoreboardUpdateCommandHint: LanguageItem

  var scoreboardUpdatedMessage: LanguageItem
}
