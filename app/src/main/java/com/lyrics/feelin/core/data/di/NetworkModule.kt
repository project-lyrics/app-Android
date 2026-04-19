package com.lyrics.feelin.core.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.lyrics.feelin.BuildConfig
import com.lyrics.feelin.core.data.datasource.remote.AuthApiService
import com.lyrics.feelin.core.data.interceptor.AppVersionInterceptor
import com.lyrics.feelin.core.data.interceptor.AuthInterceptor
import com.lyrics.feelin.core.data.interceptor.DeviceIdInterceptor
import com.lyrics.feelin.core.data.interceptor.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        appVersionInterceptor: AppVersionInterceptor,
        deviceIdInterceptor: DeviceIdInterceptor,
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BASIC
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
                redactHeader("Authorization")
                redactHeader("Cookie")
                redactHeader("Proxy-Authorization")
                redactHeader("Set-Cookie")
            }

        return OkHttpClient.Builder()
            .addInterceptor(appVersionInterceptor)
            .addInterceptor(deviceIdInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .authenticator(tokenAuthenticator)
            .build()
    }

    // https://github.com/square/retrofit/tree/trunk/retrofit-converters/kotlinx-serialization#kotlinxserialization-converter
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://api.feelinapp.com/") // TODO(@이대근): productFlavor 변수화 필요 2025.10.05.
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
