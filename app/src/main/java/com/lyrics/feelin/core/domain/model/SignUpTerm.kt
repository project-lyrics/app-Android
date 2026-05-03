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
     * Compose + WebView에서는 리다이렉트 완료 후의 퍼블릭 URL이 안정적으로 표시되어 분리한다.
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
