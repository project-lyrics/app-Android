package com.lyrics.feelin.core.data.datasource.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private val Context.deviceIdDataStore: DataStore<Preferences> by preferencesDataStore(name = "device_id")

@Singleton
class DeviceIdDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    suspend fun getOrCreate(): String {
        var deviceId: String? = null

        context.deviceIdDataStore.edit { preferences ->
            deviceId = preferences[DEVICE_ID_KEY]
                ?: UUID.randomUUID().toString().also { generatedDeviceId ->
                    preferences[DEVICE_ID_KEY] = generatedDeviceId
                }
        }

        return checkNotNull(deviceId)
    }

    companion object {
        private val DEVICE_ID_KEY = stringPreferencesKey("device_id")
    }
}
