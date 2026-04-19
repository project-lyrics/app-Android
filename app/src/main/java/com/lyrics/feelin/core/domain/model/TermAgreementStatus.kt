package com.lyrics.feelin.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TermAgreementStatus(
    var agree: Boolean,
    val title: String,
    val agreement: String,
)
