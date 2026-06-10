package com.lyrics.feelin.presentation.view.login

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginErrorTest {
    @Test
    fun backendErrorRequiresDescriptionAndKeepsCode() {
        val error: LoginError = LoginError.BackendError(
            description = "서버 오류",
            code = "05000",
        )

        assertTrue(error is LoginError.BackendError)
        assertEquals("서버 오류", (error as LoginError.BackendError).description)
        assertEquals("05000", error.code)
    }

    @Test
    fun oauthErrorKeepsOAuthFailureTypeAndCode() {
        val error: LoginError = LoginError.OAuthError(
            type = LoginErrorType.OAUTH_CLIENT,
            code = "Cancelled",
        )

        assertTrue(error is LoginError.OAuthError)
        assertEquals(LoginErrorType.OAUTH_CLIENT, (error as LoginError.OAuthError).type)
        assertEquals("Cancelled", error.code)
    }
}
