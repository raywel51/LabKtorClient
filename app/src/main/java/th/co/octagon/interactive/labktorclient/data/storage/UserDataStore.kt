package th.co.octagon.interactive.labktorclient.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

data class UserCredentials(val username: String?, val password: String?)

object UserDataStore {
    private const val USER_PREFERENCES_NAME = "user_preferences"

    private object PreferencesKeys {
        val USERNAME = stringPreferencesKey("username")
        val PASSWORD = stringPreferencesKey("password")
        val LANG = stringPreferencesKey("lang")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_PREFERENCES_NAME)

    fun init(context: Context) {
        // Initialization logic if needed
    }

    suspend fun saveUserCredentials(context: Context, username: String, password: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USERNAME] = username
            preferences[PreferencesKeys.PASSWORD] = password
        }
    }

    suspend fun saveLang(context: Context, lang: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANG] = lang
        }
    }

    private fun getStringFlow(context: Context, key: Preferences.Key<String>): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[key] ?: ""
        }
    }

    fun getUserCredentialsFlow(context: Context): Flow<UserCredentials> {
        val usernameFlow = getStringFlow(context, PreferencesKeys.USERNAME)
        val passwordFlow = getStringFlow(context, PreferencesKeys.PASSWORD)

        return usernameFlow.combine(passwordFlow) { username, password ->
            UserCredentials(username, password)
        }
    }

    fun getLangFlow(context: Context): Flow<String?> {
        return getStringFlow(context, PreferencesKeys.LANG)
    }
}
