package com.lyrics.feelin.core.data.datasource.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * 인증 토큰을 저장하고 관리하는 로컬 데이터 소스
 *
 * **저장 방식:**
 * - DataStore Preferences 사용 (모든 토큰 및 사용자 정보)
 * - 앱 전용 디렉토리에 저장되어 다른 앱 접근 불가
 * - Flow 기반 반응형 데이터 제공
 *
 * **보안 수준:**
 * - Android 샌드박싱으로 기본 보호
 * - 일반 커뮤니티 앱에 적합한 보안 수준
 */
@Singleton
class AuthLocalDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    // DataStore: 모든 인증 관련 데이터
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = DATASTORE_NAME
    )

    private val dataStore: DataStore<Preferences> = context.dataStore

    // ========== Access Token ==========

    suspend fun saveAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[KEY_ACCESS_TOKEN] = token
        }
    }

    fun getAccessTokenFlow(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[KEY_ACCESS_TOKEN]
        }
    }

    suspend fun getAccessToken(): String? {
        return getAccessTokenFlow().first()
    }

    suspend fun clearAccessToken() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_ACCESS_TOKEN)
        }
    }

    // ========== Refresh Token ==========

    suspend fun saveRefreshToken(token: String) {
        dataStore.edit { preferences ->
            preferences[KEY_REFRESH_TOKEN] = token
        }
    }

    fun getRefreshTokenFlow(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[KEY_REFRESH_TOKEN]
        }
    }

    suspend fun getRefreshToken(): String? {
        return getRefreshTokenFlow().first()
    }

    suspend fun clearRefreshToken() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_REFRESH_TOKEN)
        }
    }

    // ========== User ID ==========

    suspend fun saveUserId(userId: Long) {
        dataStore.edit { preferences ->
            preferences[KEY_USER_ID] = userId
        }
    }

    fun getUserIdFlow(): Flow<Long?> {
        return dataStore.data.map { preferences ->
            preferences[KEY_USER_ID]
        }
    }

    suspend fun getUserId(): Long? {
        return getUserIdFlow().first()
    }

    suspend fun clearUserId() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_USER_ID)
        }
    }

    // ========== 전체 삭제 ==========

    suspend fun clearAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    // ========== OAuth Provider ==========

    suspend fun saveOAuthProvider(provider: String) {
        dataStore.edit { preferences ->
            preferences[KEY_OAUTH_PROVIDER] = provider
        }
    }

    fun getOAuthProviderFlow(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[KEY_OAUTH_PROVIDER]
        }
    }

    suspend fun getOAuthProvider(): String? {
        return getOAuthProviderFlow().first()
    }

    suspend fun clearOAuthProvider() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_OAUTH_PROVIDER)
        }
    }

    // ========== OAuth Tokens (Kakao/Google SDK 토큰) ==========

    suspend fun saveOAuthAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[KEY_OAUTH_ACCESS_TOKEN] = token
        }
    }

    fun getOAuthAccessTokenFlow(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[KEY_OAUTH_ACCESS_TOKEN]
        }
    }

    suspend fun getOAuthAccessToken(): String? {
        return getOAuthAccessTokenFlow().first()
    }

    suspend fun saveOAuthRefreshToken(token: String) {
        dataStore.edit { preferences ->
            preferences[KEY_OAUTH_REFRESH_TOKEN] = token
        }
    }

    fun getOAuthRefreshTokenFlow(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[KEY_OAUTH_REFRESH_TOKEN]
        }
    }

    suspend fun getOAuthRefreshToken(): String? {
        return getOAuthRefreshTokenFlow().first()
    }

    suspend fun clearOAuthTokens() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_OAUTH_ACCESS_TOKEN)
            preferences.remove(KEY_OAUTH_REFRESH_TOKEN)
        }
    }

    companion object {
        private const val DATASTORE_NAME = "auth_datastore"

        // Backend JWT 토큰
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val KEY_USER_ID = longPreferencesKey("user_id")

        // OAuth 관련
        private val KEY_OAUTH_PROVIDER = stringPreferencesKey("oauth_provider")
        private val KEY_OAUTH_ACCESS_TOKEN = stringPreferencesKey("oauth_access_token")
        private val KEY_OAUTH_REFRESH_TOKEN = stringPreferencesKey("oauth_refresh_token")
    }
}
