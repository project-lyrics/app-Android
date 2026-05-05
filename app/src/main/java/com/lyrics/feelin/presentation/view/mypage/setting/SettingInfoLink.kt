package com.lyrics.feelin.presentation.view.mypage.setting

enum class SettingInfoLink(
    val title: String,
    val url: String,
    val opensInternally: Boolean,
) {
    SERVICE_USAGE(
        title = "서비스 이용 약관",
        url = "https://zircon-taste-62f.notion.site/Feelin-424aa52fb951444fa95f3966672ec670",
        opensInternally = true,
    ),
    PERSONAL_INFO(
        title = "개인정보처리방침",
        url = "https://zircon-taste-62f.notion.site/Feelin-2f586ef1b7c947d89ad8cac8a83b61d1",
        opensInternally = true,
    ),
    FAQ(
        title = "FAQ",
        url = "https://noon-spaghetti-8cf.notion.site/34b546f268d58034a86cd07ca5605468",
        opensInternally = true
    ),
    SERVICE_INQUIRY(
        title = "서비스 문의하기",
        url = "https://docs.google.com/forms/d/1ottTpPuoiDfQnZaMYwwi75WXdEInq6KHN8jY4L9Qc00/viewform",
        opensInternally = false,
    )
}
