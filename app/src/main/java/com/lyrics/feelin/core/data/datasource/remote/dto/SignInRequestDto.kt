package com.lyrics.feelin.core.data.datasource.remote.dto

import com.lyrics.feelin.core.domain.model.OAuthProvider
import kotlinx.serialization.Serializable

@Serializable
data class SignInRequestDto(
    val socialAccessToken: String,
    val authProvider: OAuthProvider,
)
