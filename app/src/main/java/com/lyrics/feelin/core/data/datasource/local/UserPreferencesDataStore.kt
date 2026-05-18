package com.lyrics.feelin.core.data.datasource.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val GENDER = stringPreferencesKey("gender")
        val BIRTH_YEAR = intPreferencesKey("birth_year")
        val NICKNAME = stringPreferencesKey("nickname")
        val PROFILE_INDEX = intPreferencesKey("profile_index")
        val IS_LOGGED_IN = stringPreferencesKey("is_logged_in")
    }

    val userData: Flow<UserData> = context.dataStore.data.map { preferences ->
        UserData(
            gender = preferences[Keys.GENDER],
            birthYear = preferences[Keys.BIRTH_YEAR],
            nickname = preferences[Keys.NICKNAME],
            profileIndex = preferences[Keys.PROFILE_INDEX],
            isLoggedIn = preferences[Keys.IS_LOGGED_IN] == "true"
        )
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[Keys.IS_LOGGED_IN] == "true"
    }

    suspend fun saveGenderAndBirthYear(gender: String, birthYear: Int) {
        context.dataStore.edit { preferences ->
            preferences[Keys.GENDER] = gender
            preferences[Keys.BIRTH_YEAR] = birthYear
        }
    }

    suspend fun saveProfile(nickname: String, profileIndex: Int) {
        context.dataStore.edit { preferences ->
            preferences[Keys.NICKNAME] = nickname
            preferences[Keys.PROFILE_INDEX] = profileIndex
        }
    }

    suspend fun completeOnboarding() {
        context.dataStore.edit { preferences ->
            preferences[Keys.IS_LOGGED_IN] = "true"
        }
    }

    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

// TODO(@이대근): 도메인 모델로 격상 및 실제 서버에서 받는 사용자 데이터를 저장 가능하도록 형식 변경 2026.05.14.
data class UserData(
    val gender: String? = null,
    val birthYear: Int? = null,
    val nickname: String? = null,
    val profileIndex: Int? = null,
    val isLoggedIn: Boolean = false
)
