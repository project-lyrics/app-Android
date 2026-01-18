package com.lyrics.feelin.presentation.view.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyrics.feelin.core.data.datasource.local.UserPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userPreferencesDataStore: UserPreferencesDataStore
) : ViewModel() {

    fun saveGenderAndBirthYear(gender: String, birthYear: Int) {
        viewModelScope.launch {
            userPreferencesDataStore.saveGenderAndBirthYear(gender, birthYear)
        }
    }

    fun saveProfile(nickname: String, profileIndex: Int) {
        viewModelScope.launch {
            userPreferencesDataStore.saveProfile(nickname, profileIndex)
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            userPreferencesDataStore.completeOnboarding()
        }
    }
}
