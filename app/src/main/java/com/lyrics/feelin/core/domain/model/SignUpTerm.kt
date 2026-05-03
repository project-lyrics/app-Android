package com.lyrics.feelin.core.domain.model

/**
 * 회원가입 약관 UI와 서버 전송 payload를 함께 정의한다.
 *
 * enum 선언 순서는 OnboardingTermsScreen 표시 순서이자
 * SignUpData.terms 생성 순서로 그대로 사용되므로 서버 계약 변경 없이 재정렬하지 않는다.
 */
enum class SignUpTerm(
    val title: String,
    val agreement: String,
    val required: Boolean = true,
) {
    AGE_AGREEMENT(
        title = "만 14세 이상 가입 동의",
        agreement = "",
    ),
    SERVICE_USAGE(
        title = "서비스 이용약관 동의",
        agreement = "https://www.notion.so/Feelin-424aa52fb951444fa95f3966672ec670?pvs=4",
    ),
    PERSONAL_INFO(
        title = "개인정보처리방침 동의",
        agreement = "https://www.notion.so/Feelin-2f586ef1b7c947d89ad8cac8a83b61d1?pvs=4",
    );

    fun toAgreementStatus(agree: Boolean): TermAgreementStatus {
        return TermAgreementStatus(
            agree = agree,
            title = title,
            agreement = agreement,
        )
    }
}
