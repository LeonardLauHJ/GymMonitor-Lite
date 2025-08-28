package com.leonardlau.gymmonitor.gymmonitorliteapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create a DataStore instance to store key-value pairs
private val Context.dataStore by preferencesDataStore(name = "user_preferences")

/**
 * UserPreferences
 * Contains functions for saving and retrieving user-related data locally (such as JWT tokens).
 * Uses Jetpack DataStore (Preferences) for safe, asynchronous storage.
 * This data is persisted even if the app is closed/restarted.
 */
class UserPreferences(
    private val context: Context
) {

    // Define the key for identifying the JWT token in storage
    private val tokenKey = stringPreferencesKey("jwt_token")

    // Value of the saved JWT token (empty string if no JWT token is saved)
    val token: Flow<String> = context.dataStore.data
        .map { prefs -> prefs[tokenKey] ?: "" }

    /**
     * Saves the given JWT token to local storage.
     * Overwrites any previously saved token.
     */
    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[tokenKey] = token
        }
    }

    /**
     * Clears the current JWT token from local storage.
     */
    suspend fun clearToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(tokenKey)
        }
    }
}