package com.lyrics.feelin.core.domain.model

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
