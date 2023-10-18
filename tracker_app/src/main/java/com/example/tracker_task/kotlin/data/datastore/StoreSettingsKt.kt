package com.example.tracker_task.kotlin.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoreSettingsKt @Inject constructor(
    private val context: Context
) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = "tracking"
        )
        val IS_TRACKING = booleanPreferencesKey("is_tracking")
    }

    fun getIsTracking(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_TRACKING] ?: false
        }
    }

    suspend fun setIsTracking(isTracking: Boolean) {
        context.dataStore.edit { pref ->
            pref[IS_TRACKING] = isTracking
        }
    }
}