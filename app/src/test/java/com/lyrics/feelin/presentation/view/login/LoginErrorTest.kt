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
    fun nonBackendErrorKeepsFailureTypeAndCode() {
        val error: LoginError = LoginError.NonBackendError(
            type = LoginErrorType.OAUTH_CLIENT,
            code = "Cancelled",
        )

        assertTrue(error is LoginError.NonBackendError)
        assertEquals(LoginErrorType.OAUTH_CLIENT, (error as LoginError.NonBackendError).type)
        assertEquals("Cancelled", error.code)
    }
}
