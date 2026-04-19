package com.lyrics.feelin.core.data.interceptor

import com.lyrics.feelin.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.Response

/** 모든 API 요청에 App-version 헤더를 자동으로 추가합니다. */
@Singleton
class AppVersionInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // TODO(@이대근): 2025.11.02 기준으로 iOS 앱의 버전만 맞추어져 있습니다. 이를 우회하기 위해 임시로 고정된 버전을 입력합니다. 이 부분을 서버 팀과 논의해야 합니다.
        val appVersion = if (false) BuildConfig.VERSION_NAME else "1.0.3"

        // Authorization 헤더 추가
        val authenticatedRequest = originalRequest.newBuilder()
            .header("App-Version", appVersion)
            .build()

        return chain.proceed(authenticatedRequest)
    }
}
