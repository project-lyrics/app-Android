package com.lyrics.feelin.presentation.view.login

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.lyrics.feelin.R
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LightGray01
import com.lyrics.feelin.presentation.designsystem.theme.LightGray09

/**
 * 말풍선을 로그인 버튼 위에 놓기 위한 X 조정값입니다.
 * 프리뷰를 이용해 임의 값을 지정한 것이며, +값으로 지정하면 우측으로 이동해 -값으로 지정합니다.
 * */
private const val BUBBLE_OFFSET_X = -24

/**
 * 말풍선을 로그인 버튼 위에 놓기 위한 Y 좌표 조정값입니다.
 * 프리뷰를 이용해 임의 값을 지정한 것이며, +값으로 지정하면 하단으로 이동해 -값으로 지정합니다.
 * */
private const val BUBBLE_OFFSET_Y = -22

data class SocialLoginButtonConfig(
    @param:DrawableRes val iconRes: Int,
    val text: String,
    val backgroundColor: Color,
    val textColor: Color,
    val useImageIcon: Boolean = false,
    val iconContentDescription: String
)

@Composable
fun SocialLoginButton(
    config: SocialLoginButtonConfig,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLastLogin: Boolean = false
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopEnd
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .height(56.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(8.dp)) // 공식 가이드라인은 12px
                .background(color = config.backgroundColor)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                if (config == SocialLoginButtonConfigs.Google) {
                    Image(
                        painter = painterResource(id = config.iconRes),
                        contentDescription = config.iconContentDescription,
                        modifier = Modifier.padding(
                            start = 3.dp,
                            top = 2.5.dp,
                            end = 2.5.dp,
                            bottom = 2.5.dp,
                        ),
                    )
                } else {
                    Image(
                        painter = painterResource(id = config.iconRes),
                        contentDescription = config.iconContentDescription,
                        modifier = Modifier.padding(
                            start = 4.dp,
                            top = 4.dp,
                            end = 3.dp,
                            bottom = 4.dp
                        ),
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = config.text,
                    style = FeelinTypography.title2.copy(
                        color = config.textColor,
                        textAlign = TextAlign.Center,
                        lineHeight = 1.2.em,
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false,
                        ),
                    ),
                )
            }
        }
        if (isLastLogin) {
            Image(
                painter = painterResource(R.drawable.last_login_info),
                contentDescription = "Your last login provider",
                modifier = Modifier.offset(x = BUBBLE_OFFSET_X.dp, y = BUBBLE_OFFSET_Y.dp),
            )
        }
    }
}

object SocialLoginButtonConfigs {
    private const val KAKAO_YELLOW = 0xFFFFE400

    val Kakao = SocialLoginButtonConfig(
        iconRes = R.drawable.kakao_login_icon,
        text = "카카오로 시작하기",
        backgroundColor = Color(KAKAO_YELLOW),
        textColor = LightGray09,
        useImageIcon = true,
        iconContentDescription = "kakao login button icon"
    )

    val Google = SocialLoginButtonConfig(
        iconRes = R.drawable.google_login_icon,
        text = "Google로 시작하기",
        backgroundColor = LightGray01,
        textColor = LightGray09,
        useImageIcon = true,
        iconContentDescription = "google login button icon"
    )
}

@Preview
@Composable
private fun KakaoSocialLoginButtonPreview() {
    FeelinTheme {
        SocialLoginButton(
            config = SocialLoginButtonConfigs.Kakao,
            isLastLogin = true,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun GoogleSocialLoginButtonPreview() {
    FeelinTheme {
        SocialLoginButton(
            config = SocialLoginButtonConfigs.Google,
            isLastLogin = true,
            onClick = {}
        )
    }
}
