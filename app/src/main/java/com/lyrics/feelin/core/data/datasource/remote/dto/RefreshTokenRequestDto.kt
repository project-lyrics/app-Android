package com.lyrics.feelin.core.data.datasource.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequestDto(
    val refreshToken: String
)
