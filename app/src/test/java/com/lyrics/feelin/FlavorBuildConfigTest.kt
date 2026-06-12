package com.lyrics.feelin

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FlavorBuildConfigTest {

    @Test
    fun flavorBuildConfig_matchesExpectedEnvironment() {
        val expected = when (BuildConfig.FLAVOR) {
            DEV_FLAVOR -> ExpectedFlavorConfig(
                applicationId = "com.lyrics.feelin.dev",
                baseUrl = "http://dev.feelinapp.com/"
            )
            STAGING_FLAVOR -> ExpectedFlavorConfig(
                applicationId = "com.lyrics.feelin.qa",
                baseUrl = "http://dev.feelinapp.com/"
            )
            PROD_FLAVOR -> ExpectedFlavorConfig(
                applicationId = "com.lyrics.feelin",
                baseUrl = "http://api.feelinapp.com/"
            )
            else -> error("Unexpected flavor: ${BuildConfig.FLAVOR}")
        }

        assertEquals(expected.applicationId, BuildConfig.APPLICATION_ID)
        assertEquals(expected.baseUrl, BuildConfig.BASE_URL)
    }

    @Test
    fun kakaoNativeAppKey_isInjectedForRequestedVariant() {
        assertTrue(
            "Kakao native app key must be injected for ${BuildConfig.FLAVOR}",
            BuildConfig.KAKAO_NATIVE_APP_KEY.isNotBlank()
        )
    }

    private data class ExpectedFlavorConfig(
        val applicationId: String,
        val baseUrl: String
    )

    private companion object {
        const val DEV_FLAVOR = "dev"
        const val STAGING_FLAVOR = "staging"
        const val PROD_FLAVOR = "prod"
    }
}
