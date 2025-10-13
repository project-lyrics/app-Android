package com.lyrics.feelin.presentation.view.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyrics.feelin.core.data.datasource.remote.dto.exception.FeelinServerException
import com.lyrics.feelin.core.data.manager.AuthManager
import com.lyrics.feelin.core.data.repository.AuthRepository
import com.lyrics.feelin.core.domain.model.OAuthProvider
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

    fun kakaoLogin() {
        viewModelScope.launch {
            authRepository.login(provider = OAuthProvider.KAKAO).onSuccess {
                // TODO(@이대근): 메인 화면 내비게이션 신호를 뷰로 전송 2025.10.13.
            }.onFailure {
                Log.d(TAG, "kakaoLogin: ${it.message}", it)
                when (it) {
                    is FeelinServerException -> {
                        // TODO(@이대근): loginErrorCode TODO 참조 2025.10.13.
                    }

                    else -> {
                        // 로그 기록하고 간단한 다이얼로그만 던지기??? (예외 타입을 너무 크게 잡은 것 같다)
                    }
                }
            }
        }
    }

    fun googleLogin() {
        TODO("Not yet implemented")
    }

    fun continueWithoutLogin() {
        // 뷰에서 화면 이동 작업만 해도 될 경우 메소드 삭제
    }
}
