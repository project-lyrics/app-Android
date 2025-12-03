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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lyrics.feelin.R
import com.lyrics.feelin.core.designsystem.icon.FeelinTextIcon
import com.lyrics.feelin.core.domain.model.OAuthProvider
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LightBackgroundPrimary
import com.lyrics.feelin.presentation.designsystem.theme.LightGray04
import com.lyrics.feelin.presentation.designsystem.theme.LightGray05

// 로그인 화면은 테마 미 적용입니다. @이대근 2025.09.15.

@Composable
fun LoginScreen(modifier: Modifier = Modifier, loginViewModel: LoginViewModel = viewModel()) {
    val loginErrorCode by loginViewModel.loginErrorCode.collectAsState(initial = null)
    val lastOAuthProvider by loginViewModel.lastOauthProvider.collectAsState()

    if (loginErrorCode != null) {
        // TODO(@이대근): 로그인 에러 다이얼로그 표시 2025.09.27.
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LightBackgroundPrimary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
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
                    modifier = Modifier
                        .width(212.dp)
                        .height(88.dp),
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "모두의 이야기로 채우는 우리의 음악 공간",
                    style = FeelinTypography.title2.copy(
                        fontSize = 16.sp,
                        color = LightGray05,
                    ),
                )
            }
        }
        Spacer(modifier = Modifier.height(73.dp))
//        AppleLoginButton()
//        Spacer(modifier = Modifier.height(12.dp))
        SocialLoginButton(
            config = SocialLoginButtonConfigs.Kakao,
            isLastLogin = (lastOAuthProvider == OAuthProvider.KAKAO),
            onClick = { loginViewModel.kakaoLogin() }
        )
        Spacer(modifier = Modifier.height(12.dp))
        SocialLoginButton(
            config = SocialLoginButtonConfigs.Google,
            isLastLogin = (lastOAuthProvider == OAuthProvider.GOOGLE),
            onClick = { loginViewModel.googleLogin() }
        )
        Spacer(modifier = Modifier.height(12.dp))
        SignUpLaterTextButton(
            onClick = { loginViewModel.continueWithoutLogin() },
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
            style = FeelinTypography.active.copy(fontSize = 14.sp, color = LightGray04),
            modifier = Modifier.clickable(enabled = false, onClick = onClick),
        )
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    FeelinTheme {
        LoginScreen()
    }
}
