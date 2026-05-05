package com.lyrics.feelin.core.domain.model

/**
 * 회원가입 약관 UI와 서버 전송 payload를 함께 정의한다.
 *
 * enum 선언 순서는 OnboardingTermsScreen 표시 순서이자
 * SignUpData.terms 생성 순서로 그대로 사용되므로 서버 계약 변경 없이 재정렬하지 않는다.
 */
enum class SignUpTerm(
    val title: String,
    /**
     * 서버가 전달받는, 사용자가 동의한 약관의 URL
     *
     * 서버 약관 동의 payload는 기존 Notion 원본 URL 계약을 유지한다.
     * */
    val agreement: String,
    /**
     * 실제로 웹뷰에 표시하는 URL
     *
     * WebView에 WebViewClient를 할당해도
     * Notion 원본 URL을 직접 열면 리다이렉트 안내 화면이 간헐적으로 노출될 수 있어,
     * 서버 전송용 agreement와 화면 표시용 URL을 분리한다.
     * */
    val webViewUrl: String = agreement,
    val required: Boolean = true,
) {
    AGE_AGREEMENT(
        title = "만 14세 이상 가입 동의",
        agreement = "",
    ),
    SERVICE_USAGE(
        title = "서비스 이용약관 동의",
        agreement = "https://www.notion.so/Feelin-424aa52fb951444fa95f3966672ec670?pvs=4",
        webViewUrl = "https://zircon-taste-62f.notion.site/Feelin-424aa52fb951444fa95f3966672ec670",
    ),
    PERSONAL_INFO(
        title = "개인정보처리방침 동의",
        agreement = "https://www.notion.so/Feelin-2f586ef1b7c947d89ad8cac8a83b61d1?pvs=4",
        webViewUrl = "https://zircon-taste-62f.notion.site/Feelin-2f586ef1b7c947d89ad8cac8a83b61d1",
    );

    fun toAgreementStatus(agree: Boolean): TermAgreementStatus {
        return TermAgreementStatus(
            agree = agree,
            title = title,
            agreement = agreement,
        )
    }
}
