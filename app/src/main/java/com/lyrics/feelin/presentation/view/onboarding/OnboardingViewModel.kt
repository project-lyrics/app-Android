package com.lyrics.feelin.presentation.view.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyrics.feelin.core.data.datasource.remote.dto.exception.FeelinServerException
import com.lyrics.feelin.core.data.manager.AuthManager
import com.lyrics.feelin.core.data.repository.AuthRepository
import com.lyrics.feelin.core.data.repository.UserRepository
import com.lyrics.feelin.core.domain.model.Gender
import com.lyrics.feelin.core.domain.model.ProfileType
import com.lyrics.feelin.core.domain.model.SignUpData
import com.lyrics.feelin.core.domain.model.SignUpTerm
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "OnboardingViewModel"

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authManager: AuthManager,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _onboardingState = MutableStateFlow(OnboardingState())
    val onboardingState: StateFlow<OnboardingState> = _onboardingState.asStateFlow()

    private val _onboardingUiState = MutableStateFlow<OnboardingUiState>(OnboardingUiState.Idle)
    val onboardingUiState: StateFlow<OnboardingUiState> = _onboardingUiState.asStateFlow()

    fun setAllTermsAgreed(checked: Boolean) {
        _onboardingState.value = _onboardingState.value.copy(
            termAgreements = SignUpTerm.entries.associateWith { checked },
        )
    }

    fun setTermAgreed(term: SignUpTerm, checked: Boolean) {
        _onboardingState.value = _onboardingState.value.copy(
            termAgreements = _onboardingState.value.termAgreements + (term to checked),
        )
    }

    fun saveGenderAndBirthYear(gender: String, birthYear: Int) {
        _onboardingState.value = _onboardingState.value.copy(
            gender = gender,
            birthYear = birthYear,
        )
    }

    fun clearGenderAndBirthYear() {
        _onboardingState.value = _onboardingState.value.copy(
            gender = null,
            birthYear = null,
        )
    }

    fun signUp(nickname: String, profileType: ProfileType) {
        val oauthAccessToken = authManager.oauthAccessToken.value
        val oauthProvider = authManager.oauthProvider.value
        val updatedState = _onboardingState.value.copy(
            nickname = nickname,
            profileType = profileType,
        )

        if (!updatedState.isStartEnabled) {
            _onboardingUiState.value = OnboardingUiState.Error(
                title = "필수 약관 동의가 필요해요.",
                description = "에러코드 [-1]",
            )
            return
        }

        if (oauthAccessToken.isNullOrBlank() || oauthProvider == null) {
            _onboardingUiState.value = OnboardingUiState.Error(
                title = "회원가입에 필요한 로그인 정보가 없어요.",
                description = "에러코드 [-1]",
            )
            return
        }
        _onboardingState.value = updatedState
        _onboardingUiState.value = OnboardingUiState.SigningUp

        viewModelScope.launch {
            val signUpData = SignUpData(
                socialAccessToken = oauthAccessToken,
                authProvider = oauthProvider,
                nickname = updatedState.nickname,
                profileCharacter = updatedState.profileType,
                gender = updatedState.gender?.let { Gender.fromString(it) },
                birthYear = updatedState.birthYear?.toString(),
                terms = SignUpTerm.entries.map { term ->
                    term.toAgreementStatus(agree = updatedState.termAgreements[term] == true)
                },
            )

            authRepository.signUp(signUpData = signUpData)
                .onSuccess {
                    runCatching {
                        syncLocalUserData(
                            nickname = nickname,
                            profileType = profileType,
                            state = updatedState,
                        )
                    }.onFailure { error ->
                        Log.e(TAG, "Failed to sync local onboarding cache after signup", error)
                    }
                    _onboardingUiState.value = OnboardingUiState.SignUpSuccess
                }
                .onFailure(::updateSignUpError)
        }
    }

    fun clearOnboardingUiState() {
        _onboardingUiState.value = OnboardingUiState.Idle
    }

    private suspend fun syncLocalUserData(
        nickname: String,
        profileType: ProfileType,
        state: OnboardingState,
    ) {
        if (state.gender != null && state.birthYear != null) {
            userRepository.saveGenderAndBirthYear(
                gender = state.gender,
                birthYear = state.birthYear,
            )
        }
        userRepository.saveProfile(
            nickname = nickname,
            profileIndex = profileType.ordinal,
        )
        userRepository.completeOnboarding()
    }

    private fun updateSignUpError(throwable: Throwable) {
        _onboardingUiState.value = if (throwable is FeelinServerException) {
            OnboardingUiState.Error(
                title = throwable.description.errorMessage,
                description = "에러코드 [${throwable.description.errorCode}]",
            )
        } else {
            OnboardingUiState.Error(
                title = "회원가입 시도중 오류가 발생했어요.",
                description = "에러코드 [-1]",
            )
        }
    }
}
