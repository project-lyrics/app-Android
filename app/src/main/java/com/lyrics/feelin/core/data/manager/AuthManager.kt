package com.lyrics.feelin.core.data.manager

import com.lyrics.feelin.core.data.datasource.local.AuthLocalDataSource
import com.lyrics.feelin.core.domain.model.OAuthProvider
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 인증 상태 및 토큰을 관리하는 중앙 싱글톤 매니저
 *
 * **책임:**
 * - 메모리 캐싱: 빠른 접근을 위한 StateFlow 제공
 * - 영속성 위임: AuthLocalDataSource를 통한 저장/로드
 * - 동기화: 메모리 상태와 저장소 간 일관성 유지
 * - OAuth 토큰 관리: SDK 토큰과 Backend JWT 토큰 모두 관리
 *
 * **멀티모듈 전환 시:**
 * - :core:data 모듈로 이동
 * - AuthLocalDataSource도 함께 이동
 */
@Singleton
class AuthManager @Inject constructor(
    private val authLocalDataSource: AuthLocalDataSource
) {
    private val managerScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    internal val initializationComplete = CompletableDeferred<Unit>()

    // ========== 자체 백엔드 서버 JWT (메모리 캐시) ==========

    private val _accessToken = MutableStateFlow<String?>(null)
    val accessToken: StateFlow<String?> = _accessToken.asStateFlow()

    private val _refreshToken = MutableStateFlow<String?>(null)
    val refreshToken: StateFlow<String?> = _refreshToken.asStateFlow()

    private val _userId = MutableStateFlow<Long?>(null)
    val userId: StateFlow<Long?> = _userId.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // ========== OAuth 제공자 및 토큰 (메모리 캐시) ==========

    private val _oauthProvider = MutableStateFlow<OAuthProvider?>(null)
    val oauthProvider: StateFlow<OAuthProvider?> = _oauthProvider.asStateFlow()

    private val _oauthAccessToken = MutableStateFlow<String?>(null)
    val oauthAccessToken: StateFlow<String?> = _oauthAccessToken.asStateFlow()

    private val _oauthRefreshToken = MutableStateFlow<String?>(null)
    val oauthRefreshToken: StateFlow<String?> = _oauthRefreshToken.asStateFlow()

    // ========== 초기화 ==========

    init {
        // 앱 시작 시 저장된 토큰 로드
        loadTokensFromStorage()
    }

    /**
     * 저장소에서 토큰 복원 (앱 시작 시 자동 호출)
     */
    private fun loadTokensFromStorage() {
        managerScope.launch {
            try {
                // Backend JWT 토큰 로드
                val accessToken = authLocalDataSource.getAccessToken()
                _accessToken.value = accessToken

                val refreshToken = authLocalDataSource.getRefreshToken()
                _refreshToken.value = refreshToken

                val userId = authLocalDataSource.getUserId()
                _userId.value = userId

                // OAuth 정보 로드
                val oauthProviderString = authLocalDataSource.getOAuthProvider()
                _oauthProvider.value = oauthProviderString?.let {
                    OAuthProvider.fromString(it)
                }

                val oauthAccessToken = authLocalDataSource.getOAuthAccessToken()
                _oauthAccessToken.value = oauthAccessToken

                val oauthRefreshToken = authLocalDataSource.getOAuthRefreshToken()
                _oauthRefreshToken.value = oauthRefreshToken

                // 로그인 상태 판단: Refresh Token 존재 여부로 결정
                _isLoggedIn.value = refreshToken != null
            } finally {
                initializationComplete.complete(Unit)
            }
        }
    }

    // ========== 토큰 저장 ==========

    /**
     * 로그인 성공 시 토큰 저장 (Backend JWT + OAuth 정보)
     *
     * @param accessToken Backend JWT 액세스 토큰
     * @param refreshToken Backend JWT 리프레시 토큰
     * @param userId 사용자 ID
     * @param oauthProvider OAuth 제공자 (KAKAO, GOOGLE 등)
     * @param oauthAccessToken OAuth SDK 액세스 토큰 (옵션)
     * @param oauthRefreshToken OAuth SDK 리프레시 토큰 (옵션)
     */
    suspend fun saveAllToken(
        accessToken: String,
        refreshToken: String,
        userId: Long? = null,
        oauthProvider: OAuthProvider? = null,
        oauthAccessToken: String? = null,
        oauthRefreshToken: String? = null
    ) {
        // 1. 메모리 캐시 업데이트 - Backend JWT
        _accessToken.value = accessToken
        _refreshToken.value = refreshToken
        _userId.value = userId
        _isLoggedIn.value = true

        // 2. 메모리 캐시 업데이트 - OAuth
        _oauthProvider.value = oauthProvider
        _oauthAccessToken.value = oauthAccessToken
        _oauthRefreshToken.value = oauthRefreshToken

        // 3. 영속성 저장 - Backend JWT
        authLocalDataSource.saveAccessToken(accessToken)
        authLocalDataSource.saveRefreshToken(refreshToken)
        userId?.let { authLocalDataSource.saveUserId(it) }

        // 4. 영속성 저장 - OAuth
        oauthProvider?.let { authLocalDataSource.saveOAuthProvider(it.name) }
        oauthAccessToken?.let { authLocalDataSource.saveOAuthAccessToken(it) }
        oauthRefreshToken?.let { authLocalDataSource.saveOAuthRefreshToken(it) }
    }

    suspend fun saveServerToken(
        accessToken: String,
        refreshToken: String,
        userId: Long? = null
    ) {
        // 1. 메모리 캐시 업데이트 - Backend JWT
        _accessToken.value = accessToken
        _refreshToken.value = refreshToken
        _userId.value = userId
        _isLoggedIn.value = true

        // 2. 영속성 저장 - Backend JWT
        authLocalDataSource.saveAccessToken(accessToken)
        authLocalDataSource.saveRefreshToken(refreshToken)
        userId?.let { authLocalDataSource.saveUserId(it) }
    }

    /**
     * Access Token만 갱신 (Token Refresh 시 사용)
     *
     * @param newAccessToken 새로운 액세스 토큰
     */
    suspend fun updateAccessToken(newAccessToken: String) {
        // 메모리 캐시 업데이트
        _accessToken.value = newAccessToken
        // 영속성 저장
        authLocalDataSource.saveAccessToken(newAccessToken)
    }

    /**
     * Refresh Token만 갱신 (RTR 정책 시 사용)
     *
     * @param newRefreshToken 새로운 리프레시 토큰
     */
    suspend fun updateRefreshToken(newRefreshToken: String) {
        _refreshToken.value = newRefreshToken
        authLocalDataSource.saveRefreshToken(newRefreshToken)
    }

    // ========== 토큰 삭제 ==========

    /**
     * 로그아웃 또는 토큰 만료 시 모든 인증 데이터 삭제
     */
    suspend fun clearTokens() {
        // 1. 메모리 캐시 클리어 - Backend JWT
        _accessToken.value = null
        _refreshToken.value = null
        _userId.value = null
        _isLoggedIn.value = false

        // 2. 메모리 캐시 클리어 - OAuth
        _oauthProvider.value = null
        _oauthAccessToken.value = null
        _oauthRefreshToken.value = null

        // 3. 영속성 저장소 클리어
        authLocalDataSource.clearAll()
    }

    // ========== OAuth 토큰 관리 ==========

    /**
     * OAuth 토큰만 업데이트
     *
     * 아래 상황에서 사용합니다.
     * - SDK 토큰 갱신 시
     * - 우리 서버에 회원가입 하지 않은 유저가 소셜 로그인 시
     *
     * @param oauthAccessToken 새로운 OAuth 액세스 토큰
     * @param oauthRefreshToken 새로운 OAuth 리프레시 토큰 (옵션)
     */
    suspend fun updateOAuthToken(
        oauthAccessToken: String,
        oauthRefreshToken: String? = null
    ) {
        _oauthAccessToken.value = oauthAccessToken
        authLocalDataSource.saveOAuthAccessToken(oauthAccessToken)

        oauthRefreshToken?.let {
            _oauthRefreshToken.value = it
            authLocalDataSource.saveOAuthRefreshToken(it)
        }
    }

    suspend fun updateOAuthProvider(provider: OAuthProvider) {
        _oauthProvider.value = provider
        authLocalDataSource.saveOAuthProvider(provider.name)
    }

    // ========== 유틸리티 ==========

    fun hasValidAccessToken(): Boolean {
        return !_accessToken.value.isNullOrEmpty()
    }

    fun hasRefreshToken(): Boolean {
        return !_refreshToken.value.isNullOrEmpty()
    }
}
