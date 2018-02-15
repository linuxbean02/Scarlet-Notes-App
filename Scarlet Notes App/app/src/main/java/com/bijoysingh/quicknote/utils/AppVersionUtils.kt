package com.bijoysingh.quicknote.utils

import android.content.Context
import com.bijoysingh.quicknote.BuildConfig
import com.bijoysingh.quicknote.activities.sheets.WhatsNewItemsBottomSheet
import com.bijoysingh.quicknote.database.Note
import com.github.bijoysingh.starter.prefs.DataStore

const val KEY_LAST_KNOWN_APP_VERSION = "KEY_LAST_KNOWN_APP_VERSION"
const val KEY_LAST_SHOWN_WHATS_NEW = "KEY_LAST_SHOWN_WHATS_NEW"

fun getCurrentVersionCode(): Int {
  return BuildConfig.VERSION_CODE
}

/**
 * Returns app version if it's guaranteed the user an app version. (Stored in the app version variable)
 * If the user has notes it is assumed that the user was at-least at the last version. returns : -1
 * If nothing can be concluded it's 0 (assumes new user)
 */
fun getLastUsedAppVersionCode(context: Context, dataStore: DataStore): Int {
  val appVersion = dataStore.get(KEY_LAST_KNOWN_APP_VERSION, 0)
  return when {
    appVersion > 0 -> appVersion
    Note.db(context).count > 0 -> -1
    else -> 0
  }
}

fun shouldShowWhatsNewSheet(context: Context, dataStore: DataStore): Boolean {
  val lastShownWhatsNew = dataStore.get(KEY_LAST_SHOWN_WHATS_NEW, 0)
  if (lastShownWhatsNew >= WhatsNewItemsBottomSheet.WHATS_NEW_UID) {
    // Already shown the latest
    return false
  }

  val lastUsedAppVersion = getLastUsedAppVersionCode(context, dataStore)

  // Update the values independent of the decision
  dataStore.put(KEY_LAST_SHOWN_WHATS_NEW, WhatsNewItemsBottomSheet.WHATS_NEW_UID)
  dataStore.put(KEY_LAST_KNOWN_APP_VERSION, getCurrentVersionCode())

  // New users don't need to see the whats new screen
  return lastUsedAppVersion != 0
}