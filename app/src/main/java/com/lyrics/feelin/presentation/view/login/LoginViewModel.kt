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

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authManager: AuthManager
) : ViewModel() {
    // TODO(@이대근): LoginErrorCode가 아니라 로그인 에러를 통째로 뷰에 넘겨서, 다이얼로그를 표시할 수 있도록 해야 한다. 2025.10.13.
    private val _loginErrorCode = MutableStateFlow<Int?>(null)
    val loginErrorCode = _loginErrorCode.asStateFlow()

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
                    // TODO(@이대근): 홈 화면에 대한 라우팅 신호 전달
                }
                .onFailure {
                    Log.e(TAG, "login failure", it)

                    if (it is FeelinServerException) {
                        if (it.description.errorCode == "02000") {
                            // TODO(@이대근): 회원가입 화면에 대한 라우팅 신호 전달
                            return@onFailure
                        }
                    }

                    _loginErrorCode.value = -1 // 임의값, 프로퍼티 주석 참고
                }
        }
    }

    fun continueWithoutLogin() {
        // 뷰에서 화면 이동 작업만 해도 될 경우 메소드 삭제
    }
}
