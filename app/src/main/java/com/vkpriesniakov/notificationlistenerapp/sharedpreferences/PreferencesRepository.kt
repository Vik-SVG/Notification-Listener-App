package com.vkpriesniakov.notificationlistenerapp.sharedpreferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import com.vkpriesniakov.notificationlistenerapp.model.FilterTypes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PreferencesRepository(val context: Context) {

     val dataStore: DataStore<Preferences> = context.createDataStore(name = "Filter")

     object PreferencesKeys {
        val CHOSEN_FILTER = stringPreferencesKey("chosen_string_filter")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val choseFilter = preferences[PreferencesKeys.CHOSEN_FILTER] ?: FilterTypes.ALL.name
        UserPreferences(choseFilter)
    }

    suspend fun updateChosenFilter(filter:FilterTypes){
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CHOSEN_FILTER] = filter.name
        }
    }

}


data class UserPreferences(val filterType: String)