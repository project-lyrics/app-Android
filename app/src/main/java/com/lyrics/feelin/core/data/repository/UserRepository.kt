package com.lyrics.feelin.core.data.repository

import com.lyrics.feelin.core.data.datasource.local.UserData
import com.lyrics.feelin.core.data.datasource.local.UserPreferencesDataStore
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class UserRepository @Inject constructor(
    private val userPreferencesDataStore: UserPreferencesDataStore
) {
    val userData: Flow<UserData> = userPreferencesDataStore.userData

    val isLoggedIn: Flow<Boolean> = userPreferencesDataStore.isLoggedIn

    suspend fun saveGenderAndBirthYear(gender: String, birthYear: Int) {
        userPreferencesDataStore.saveGenderAndBirthYear(gender, birthYear)
    }

    suspend fun saveProfile(nickname: String, profileIndex: Int) {
        userPreferencesDataStore.saveProfile(nickname, profileIndex)
    }

    suspend fun completeOnboarding() {
        userPreferencesDataStore.completeOnboarding()
    }

    suspend fun logout() {
        userPreferencesDataStore.logout()
    }
}
