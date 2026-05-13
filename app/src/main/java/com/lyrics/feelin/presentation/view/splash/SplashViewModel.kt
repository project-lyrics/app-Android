package com.lyrics.feelin.presentation.view.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyrics.feelin.core.data.repository.AuthRepository
import com.lyrics.feelin.core.data.repository.RestoreSessionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface SplashUiState {
    data object CheckingSession : SplashUiState
    data object NavigateToMain : SplashUiState
    data class NavigateToLogin(val autoLoginFailedErrorCode: String?) : SplashUiState
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _splashUiState = MutableStateFlow<SplashUiState>(SplashUiState.CheckingSession)
    val splashUiState = _splashUiState.asStateFlow()

    init {
        restoreSession()
    }

    private fun restoreSession() {
        viewModelScope.launch {
            val nextState = when (val restoreSessionResult = authRepository.restoreSession()) {
                RestoreSessionResult.Authenticated -> SplashUiState.NavigateToMain
                RestoreSessionResult.Unauthenticated -> {
                    SplashUiState.NavigateToLogin(autoLoginFailedErrorCode = null)
                }
                is RestoreSessionResult.Failed -> {
                    SplashUiState.NavigateToLogin(autoLoginFailedErrorCode = restoreSessionResult.errorCode)
                }
            }
            _splashUiState.value = nextState
        }
    }
}
