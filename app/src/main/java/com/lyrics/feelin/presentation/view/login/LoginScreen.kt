package com.lyrics.feelin.presentation.view.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lyrics.feelin.R
import com.lyrics.feelin.core.designsystem.icon.FeelinTextIcon
import com.lyrics.feelin.presentation.designsystem.theme.CaptionActiveTextStyle
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.LightBackgroundPrimary
import com.lyrics.feelin.presentation.designsystem.theme.LightGray04
import com.lyrics.feelin.presentation.designsystem.theme.LightGray05

// 로그인 화면은 테마 미 적용입니다. @이대근 2025.09.15.

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(color = LightBackgroundPrimary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
            Modifier
                .height(172.dp)
                .fillMaxWidth(),
        ) {
            Image(
                painter = painterResource(id = R.drawable.login_wave),
                contentDescription = "login background image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize(),
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    painter = FeelinTextIcon,
                    contentDescription = "Feelin",
                    modifier =
                    Modifier
                        .width(212.dp)
                        .height(88.dp),
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "모두의 이야기로 채우는 우리의 음악 공간",
                    style =
                    MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp,
                        color = LightGray05,
                    ),
                )
            }
        }
        Spacer(modifier = Modifier.height(73.dp))
//        AppleLoginButton()
        Spacer(modifier = Modifier.height(12.dp))
        KakaoLoginButton(
            isLastLogin = false,
            onClick = {}
        )
        Spacer(modifier = Modifier.height(12.dp))
        GoogleLoginButton(
            isLastLogin = true,
            onClick = {},
        )
        Spacer(modifier = Modifier.height(12.dp))
        SignUpLaterTextButton(
            onClick = {},
        )
    }
}

@Composable
private fun SignUpLaterTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
        Modifier
            .padding(horizontal = 20.dp)
            .height(56.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "회원가입은 나중에! 둘러볼게요",
            style = CaptionActiveTextStyle.copy(fontSize = 14.sp, color = LightGray04),
            modifier = Modifier.clickable(enabled = false, onClick = onClick),
        )
    }
}

// MARK(@이대근): 애플로그인은 사용하지 않을 가능성이 커, 버튼 구현만 하고 사용하지 않습니다. 2025.09.15.
// private const val APPLE_BLACK = 0xFF000000
// @Composable
// private fun AppleLoginButton(
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier,
//    isLastLogin: Boolean = false,
// ) {
//    Box(contentAlignment = Alignment.TopCenter) {
//        Box(
//            modifier =
//            Modifier
//                .padding(horizontal = 20.dp)
//                .height(56.dp)
//                .fillMaxWidth()
//                .clip(shape = RoundedCornerShape(8.dp)) // 공식 가이드라인은 15px
//                .background(color = APPLE_BLACK)
//                .clickable(onClick = onClick),
//            contentAlignment = Alignment.Center,
//        ) {
//            Row(
//                modifier = Modifier.height(24.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center,
//            ) {
//                Image(
//                    painter = painterResource(id = new resource needed),
//                    contentDescription = "kakao login button icon",
//                    modifier =
//                    Modifier.padding(
//                        start = 2.5.dp,
//                        top = 2.5.dp,
//                        end = 2.5.dp,
//                        bottom = 2.5.dp,
//                    ),
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(
//                    text = "Apple로 시작하기",
//                    style =
//                    MaterialTheme.typography.titleMedium.copy(
//                        color = LightGray00,
//                        textAlign = TextAlign.Center,
//                        lineHeight = 1.2.em,
//                        platformStyle =
//                        PlatformTextStyle(
//                            includeFontPadding = false,
//                        ),
//                    ),
//                    modifier =
//                    Modifier
//                        .height(24.dp)
//                        .wrapContentHeight(),
//                )
//            }
//        }
//        if (isLastLogin) {
//            Image(
//                painter = painterResource(R.drawable.last_login_info),
//                contentDescription = "Your last log in is google",
//                modifier = Modifier.offset(x = 97.dp, y = (-22).dp),
//            )
//        }
//    }
// }

@Preview
@Composable
private fun LoginScreenPreview() {
    FeelinTheme {
        LoginScreen()
    }
}
