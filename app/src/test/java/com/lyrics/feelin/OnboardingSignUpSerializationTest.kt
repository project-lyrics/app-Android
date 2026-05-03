package com.lyrics.feelin

import com.lyrics.feelin.core.domain.model.OAuthProvider
import com.lyrics.feelin.core.domain.model.ProfileType
import com.lyrics.feelin.core.domain.model.SignUpData
import com.lyrics.feelin.core.domain.model.SignUpTerm
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OnboardingSignUpSerializationTest {
    @Test
    fun `signup data serializes profile type as camel case`() {
        val signUpData = SignUpData(
            socialAccessToken = "token",
            authProvider = OAuthProvider.KAKAO,
            nickname = "feelin",
            profileCharacter = ProfileType.SHORT_HAIR,
            gender = null,
            birthYear = null,
            terms = listOf(SignUpTerm.AGE_AGREEMENT.toAgreementStatus(agree = true)),
        )

        val serialized = Json.encodeToString(SignUpData.serializer(), signUpData)

        assertTrue(serialized.contains("\"profileCharacter\":\"shortHair\""))
    }

    @Test
    fun `signup terms map to server payload in enum order`() {
        val agreements = SignUpTerm.entries.map { it.toAgreementStatus(agree = true) }

        assertEquals("만 14세 이상 가입 동의", agreements[0].title)
        assertEquals("", agreements[0].agreement)
        assertEquals("서비스 이용약관 동의", agreements[1].title)
        assertEquals(
            "https://www.notion.so/Feelin-424aa52fb951444fa95f3966672ec670?pvs=4",
            agreements[1].agreement,
        )
        assertEquals("개인정보처리방침 동의", agreements[2].title)
        assertEquals(
            "https://www.notion.so/Feelin-2f586ef1b7c947d89ad8cac8a83b61d1?pvs=4",
            agreements[2].agreement,
        )
    }
}
