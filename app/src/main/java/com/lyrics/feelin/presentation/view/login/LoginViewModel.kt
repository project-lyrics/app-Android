package com.lyrics.feelin.presentation.view.login

import androidx.lifecycle.ViewModel
import com.lyrics.feelin.core.domain.model.OAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {
    private val _loginErrorCode = MutableStateFlow<Int?>(null)
    val loginErrorCode = _loginErrorCode.asStateFlow()

    private val _lastOAuthProvider = MutableStateFlow<OAuthProvider?>(null)
    val lastOauthProvider = _lastOAuthProvider.asStateFlow()

    fun kakaoLogin() {
        TODO("Not yet implemented")
    }

    fun googleLogin() {
        TODO("Not yet implemented")
    }

    fun continueWithoutLogin() {
        // 뷰에서 화면 이동 작업만 해도 될 경우 메소드 삭제
    }
}
