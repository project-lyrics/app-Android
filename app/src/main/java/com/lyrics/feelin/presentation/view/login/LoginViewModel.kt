package com.lyrics.feelin.presentation.view.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyrics.feelin.core.data.datasource.remote.dto.exception.FeelinServerException
import com.lyrics.feelin.core.data.manager.AuthManager
import com.lyrics.feelin.core.data.repository.AuthRepository
import com.lyrics.feelin.core.domain.model.OAuthProvider
import com.lyrics.feelin.core.domain.model.OAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "LoginViewModel"
private const val DEFAULT_LOGIN_ERROR_DESCRIPTION = "로그인 시도중 오류가 발생했어요."

enum class LoginErrorType {
    OAUTH_CLIENT,
    OAUTH_SERVER,
    BACKEND_SERVER,
    UNKNOWN,
}

sealed class LoginError {
    data class BackendError(
        val description: String,
        val code: String?,
    ) : LoginError()

    data class OAuthError(
        val type: LoginErrorType,
        val code: String? = null,
    ) : LoginError()
}

sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Success : LoginUiState
    data object SignUpRequired : LoginUiState
    data class Error(val error: LoginError) : LoginUiState
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authManager: AuthManager
) : ViewModel() {
    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginUiState = _loginUiState.asStateFlow()

    private val _lastOAuthProvider = MutableStateFlow<OAuthProvider?>(null)
    val lastOauthProvider = _lastOAuthProvider.asStateFlow()

    fun getLastOAuthProvider() {
        _lastOAuthProvider.value = authManager.oauthProvider.value
    }

    fun login(oAuthProvider: OAuthProvider, token: OAuthToken) {
        viewModelScope.launch {
            authRepository.authenticateWithBackend(provider = oAuthProvider, oauthToken = token)
                .onSuccess {
                    Log.d(TAG, "login success, provider: $oAuthProvider")
                    _loginUiState.value = LoginUiState.Success
                }
                .onFailure {
                    Log.e(TAG, "login failure", it)

                    if (it is FeelinServerException) {
                        if (it.description.errorCode == "02000") {
                            _loginUiState.value = LoginUiState.SignUpRequired
                            return@onFailure
                        }

                        updateLoginError(
                            type = LoginErrorType.BACKEND_SERVER,
                            message = it.description.errorMessage,
                            code = it.description.errorCode,
                        )
                        return@onFailure
                    }

                    updateLoginError(type = LoginErrorType.UNKNOWN)
                }
        }
    }

    fun updateLoginError(type: LoginErrorType, message: String? = null, code: String? = null) {
        _loginUiState.value = LoginUiState.Error(
            error = if (type == LoginErrorType.BACKEND_SERVER) {
                LoginError.BackendError(
                    description = message ?: DEFAULT_LOGIN_ERROR_DESCRIPTION,
                    code = code,
                )
            } else {
                LoginError.OAuthError(
                    type = type,
                    code = code,
                )
            }
        )
    }

    fun clearLoginUiState() {
        _loginUiState.value = LoginUiState.Idle
    }

    fun continueWithoutLogin() {
        // 뷰에서 화면 이동 작업만 해도 될 경우 메소드 삭제
    }
}
