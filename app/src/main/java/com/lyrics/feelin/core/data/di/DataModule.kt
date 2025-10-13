package com.lyrics.feelin.core.data.di

import android.content.Context
import com.lyrics.feelin.core.data.datasource.sdk.KakaoAuthDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

/**
 * Data Layer 의존성 주입 모듈
 *
 * **제공하는 싱글톤:**
 * - AuthLocalDataSource: 영속성 저장소
 * - AuthManager: 토큰/상태 관리자
 *
 * **멀티모듈 전환 시:**
 * - :core:data 모듈로 이동
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideKakaoAuthDataSource(
        @ApplicationContext context: Context
    ): KakaoAuthDataSource = KakaoAuthDataSource(context)
}
