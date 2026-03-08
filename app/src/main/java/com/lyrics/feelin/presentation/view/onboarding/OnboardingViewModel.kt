package com.lyrics.feelin.presentation.view.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyrics.feelin.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun saveGenderAndBirthYear(gender: String, birthYear: Int) {
        viewModelScope.launch {
            userRepository.saveGenderAndBirthYear(gender, birthYear)
        }
    }

    fun saveProfile(nickname: String, profileIndex: Int) {
        viewModelScope.launch {
            userRepository.saveProfile(nickname, profileIndex)
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            userRepository.completeOnboarding()
        }
    }
}
