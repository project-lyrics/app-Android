package com.lyrics.feelin.presentation.view.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import com.lyrics.feelin.presentation.designsystem.theme.LightGray01

@Composable
fun GoogleLoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            Modifier
                .padding(horizontal = 20.dp)
                .height(56.dp)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(8.dp)) // 공식 가이드라인은 12px
                .background(color = LightGray01)
                .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier.height(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.google_login_icon),
                contentDescription = "kakao login button icon",
                modifier =
                    Modifier.padding(
                        start = 2.5.dp,
                        top = 2.5.dp,
                        end = 2.5.dp,
                        bottom = 2.5.dp,
                    ),
            )
            Spacer(modifier = Modifier.width(8.dp))
            // TODO(@이대근): 살려줘요 아이콘이랑 텍스트가 중앙정렬이 안 맞아 2025.09.16.
            Text(
                text = "Google 계정으로 로그인",
                style =
                    MaterialTheme.typography.titleMedium.copy(
                        color = Color(0xFF1F1F1F),
                        textAlign = TextAlign.Center,
                        lineHeight = 1.2.em,
                        platformStyle =
                            PlatformTextStyle(
                                includeFontPadding = false,
                            ),
                    ),
                modifier =
                    Modifier
                        .height(24.dp)
                        .wrapContentHeight(),
            )
        }
    }
}

@Preview
@Composable
private fun GoogleLoginButtonPreview() {
    FeelinTheme {
        GoogleLoginButton(onClick = {})
    }
}
