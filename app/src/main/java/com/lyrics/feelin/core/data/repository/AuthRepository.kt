package com.lyrics.feelin.core.data.repository

import com.lyrics.feelin.core.domain.model.OAuthProvider

interface AuthRepository {
    fun login(provider: OAuthProvider)

    fun logout()
}
