package com.lyrics.feelin.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SignUpData(
    val socialAccessToken: String,
    val authProvider: OAuthProvider,
    val nickname: String,
    val profileCharacter: ProfileType,
    val gender: Gender?,
    val birthYear: String?,
    val terms: List<TermAgreementStatus>,
    val isAdmin: Boolean = false,
)
