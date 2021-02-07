package com.vkpriesniakov.notificationlistenerapp.sharedpreferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PreferencesRepository(val context: Context) {

     val dataStore: DataStore<Preferences> = context.createDataStore(name = "Filter")

     object PreferencesKeys {
        val CHOSEN_FILTER = intPreferencesKey("chosen_filter")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val choseFilter = preferences[PreferencesKeys.CHOSEN_FILTER] ?: 0
        UserPreferences(choseFilter)
    }

    suspend fun updateChosenFilter(filter:Int){
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CHOSEN_FILTER] = filter
        }
    }

}


data class UserPreferences(val filterType: Int)