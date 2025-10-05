package com.lyrics.feelin.core.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.lyrics.feelin.core.data.datasource.remote.AuthApiService
import com.lyrics.feelin.core.data.interceptor.AuthInterceptor
import com.lyrics.feelin.core.data.interceptor.TokenAuthenticator
import com.lyrics.feelin.core.data.manager.AuthManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator)
            .build()
    }

    // https://github.com/square/retrofit/tree/trunk/retrofit-converters/kotlinx-serialization#kotlinxserialization-converter
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://dev.feelinapp.com/") // TODO(@이대근): productFlavor 변수화 필요 2025.10.05.
            .addConverterFactory(
                Json.asConverterFactory(
                    "application/json; charset=UTF-8".toMediaType()
                )
            )
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }
}