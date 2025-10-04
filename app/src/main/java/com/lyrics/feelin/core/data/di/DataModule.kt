package com.lyrics.feelin.core.data.di

import com.lyrics.feelin.core.data.datasource.local.AuthLocalDataSource
import com.lyrics.feelin.core.data.manager.AuthManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
    @Singleton
    fun provideAuthLocalDataSource(
        authLocalDataSource: AuthLocalDataSource
    ): AuthLocalDataSource = authLocalDataSource

    @Provides
    @Singleton
    fun provideAuthManager(
        authLocalDataSource: AuthLocalDataSource
    ): AuthManager = AuthManager(authLocalDataSource)
}
