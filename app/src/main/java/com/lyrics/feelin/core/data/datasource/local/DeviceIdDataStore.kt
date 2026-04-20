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
import kotlinx.coroutines.flow.first

private val Context.deviceIdDataStore: DataStore<Preferences> by preferencesDataStore(name = "device_id")

@Singleton
class DeviceIdDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    suspend fun getOrCreate(): String {
        val existingDeviceId = context.deviceIdDataStore.data.first()[DEVICE_ID_KEY]
        if (existingDeviceId != null) {
            return existingDeviceId
        }

        val generatedDeviceId = UUID.randomUUID().toString()
        var storedDeviceId = generatedDeviceId

        context.deviceIdDataStore.edit { preferences ->
            storedDeviceId = preferences[DEVICE_ID_KEY]
                ?: generatedDeviceId.also { preferences[DEVICE_ID_KEY] = it }
        }

        return storedDeviceId
    }

    companion object {
        private val DEVICE_ID_KEY = stringPreferencesKey("device_id")
    }
}
