package com.lyrics.feelin.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class OAuthProvider {
    KAKAO,
    GOOGLE;

    companion object {
        fun fromString(value: String): OAuthProvider? {
            return entries.find { it.name.equals(value, ignoreCase = true) }
        }
    }
}
