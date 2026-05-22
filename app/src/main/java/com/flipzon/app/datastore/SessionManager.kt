package com.flipzon.app.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "flipzon_session"
private val Context.sessionDataStore by preferencesDataStore(name = DATASTORE_NAME)

object SessionKeys {
    val USER_ID = intPreferencesKey("user_id")
    val EMAIL = stringPreferencesKey("email")
    val FIRST_NAME = stringPreferencesKey("first_name")
    val LAST_NAME = stringPreferencesKey("last_name")
    val IMAGE = stringPreferencesKey("image")
}

data class UserSession(
    val userId: Int = -1,
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val image: String = ""
) {
    val isValid: Boolean get() = userId > 0 && email.isNotBlank()
}

class SessionManager(private val context: Context) {
    val sessionFlow: Flow<UserSession> = context.sessionDataStore.data.map { prefs ->
        UserSession(
            userId = prefs[SessionKeys.USER_ID] ?: -1,
            email = prefs[SessionKeys.EMAIL] ?: "",
            firstName = prefs[SessionKeys.FIRST_NAME] ?: "",
            lastName = prefs[SessionKeys.LAST_NAME] ?: "",
            image = prefs[SessionKeys.IMAGE] ?: ""
        )
    }

    suspend fun saveSession(userId: Int, email: String, firstName: String, lastName: String, image: String) {
        context.sessionDataStore.edit { prefs ->
            prefs[SessionKeys.USER_ID] = userId
            prefs[SessionKeys.EMAIL] = email
            prefs[SessionKeys.FIRST_NAME] = firstName
            prefs[SessionKeys.LAST_NAME] = lastName
            prefs[SessionKeys.IMAGE] = image
        }
    }

    suspend fun clearSession() {
        context.sessionDataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
