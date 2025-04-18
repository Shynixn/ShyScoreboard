package com.github.shynixn.shyscoreboard.contract

import com.github.shynixn.mcutils.common.language.LanguageItem
import com.github.shynixn.mcutils.common.language.LanguageProvider

interface ShyScoreboardLanguage : LanguageProvider {
  var shyScoreboardPlayerNotFoundMessage: LanguageItem

  var shyScoreboardNoPermissionCommand: LanguageItem

  var shyScoreboardReloadCommandHint: LanguageItem

  var shyScoreboardReloadMessage: LanguageItem

  var shyScoreboardCommonErrorMessage: LanguageItem

  var shyScoreboardCommandSenderHasToBePlayer: LanguageItem

  var shyScoreboardCommandUsage: LanguageItem

  var shyScoreboardCommandDescription: LanguageItem

  var shyScoreboardAddCommandHint: LanguageItem

  var shyScoreboardSetCommandHint: LanguageItem

  var shyScoreboardRemoveCommandHint: LanguageItem

  var shyScoreboardNotFoundMessage: LanguageItem

  var shyScoreboardNoPermissionToScoreboardCommand: LanguageItem

  var shyScoreboardAddedMessage: LanguageItem

  var shyScoreboardRemovedMessage: LanguageItem

  var shyScoreboardUpdateCommandHint: LanguageItem

  var shyScoreboardUpdatedMessage: LanguageItem

  var shyScoreboardBooleanNotFoundMessage: LanguageItem
}
