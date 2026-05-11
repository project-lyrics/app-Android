package com.lyrics.feelin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyrics.feelin.core.data.repository.AuthRepository
import com.lyrics.feelin.navigation.FeelinDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface MainUiState {
    data object CheckingSession : MainUiState
    data class Ready(val startDestination: String) : MainUiState
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _mainUiState = MutableStateFlow<MainUiState>(MainUiState.CheckingSession)
    val mainUiState = _mainUiState.asStateFlow()

    init {
        restoreSession()
    }

    private fun restoreSession() {
        viewModelScope.launch {
            val startDestination = authRepository.restoreSession()
                .fold(
                    onSuccess = { FeelinDestination.MainGraph.route },
                    onFailure = { FeelinDestination.OnboardingGraph.route },
                )
            _mainUiState.value = MainUiState.Ready(startDestination = startDestination)
        }
    }
}
