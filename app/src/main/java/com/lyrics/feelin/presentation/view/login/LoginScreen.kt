package com.lyrics.feelin.presentation.view.login

import android.content.Context
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.lyrics.feelin.R
import com.lyrics.feelin.core.data.datasource.sdk.util.toDomainModel
import com.lyrics.feelin.core.designsystem.component.FeelinModalDialog
import com.lyrics.feelin.core.designsystem.icon.FeelinTextIcon
import com.lyrics.feelin.core.domain.model.OAuthProvider
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LightBackgroundPrimary
import com.lyrics.feelin.presentation.designsystem.theme.LightGray04
import com.lyrics.feelin.presentation.designsystem.theme.LightGray05

private const val TAG = "LoginScreen"
private const val UNKNOWN_AUTO_LOGIN_ERROR_CODE = "-1"

// 로그인 화면은 테마 미 적용입니다. @이대근 2025.09.15.

@Composable
fun LoginScreen(
    onSignUp: () -> Unit,
    onContinueToMain: () -> Unit,
    modifier: Modifier = Modifier,
    autoLoginFailedErrorCode: String? = null,
    loginViewModel: LoginViewModel = hiltViewModel<LoginViewModel>()
) {
    val loginUiState by loginViewModel.loginUiState.collectAsState()
    val lastOAuthProvider by loginViewModel.lastOauthProvider.collectAsState()
    var isAutoLoginFailedDialogVisible by rememberSaveable(autoLoginFailedErrorCode) {
        mutableStateOf(autoLoginFailedErrorCode != null)
    }

    if (isAutoLoginFailedDialogVisible) {
        FeelinModalDialog(
            title = "장시간 서비스를 이용하지 않아\n로그인 정보가 만료되었어요.",
            description = "에러코드 [${autoLoginFailedErrorCode ?: UNKNOWN_AUTO_LOGIN_ERROR_CODE}]",
            confirmButtonText = "확인",
            onConfirmButtonClick = {
                isAutoLoginFailedDialogVisible = false
            },
            isDismissButtonEnable = false,
        )
    }

    if (loginUiState is LoginUiState.Error) {
        when (val error = (loginUiState as LoginUiState.Error).error) {
            is LoginError.BackendError -> {
                // MARK(@이대근): 추후 통합된 서버 에러 다이얼로그로 변경 2026.03.15.
                FeelinModalDialog(
                    title = error.description,
                    description = "에러코드 [${error.code}]",
                    confirmButtonText = "확인",
                    onConfirmButtonClick = {
                        loginViewModel.clearLoginUiState()
                    },
                    isDismissButtonEnable = false,
                )
            }
            is LoginError.OAuthError -> {
                FeelinModalDialog(
                    title = "로그인 시도중 오류가 발생했어요.",
                    description = "에러코드 [-1]",
                    confirmButtonText = "확인",
                    onConfirmButtonClick = {
                        loginViewModel.clearLoginUiState()
                    },
                    isDismissButtonEnable = false,
                )
            }
        }
    }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        loginViewModel.getLastOAuthProvider()
    }

    LaunchedEffect(loginUiState) {
        when (loginUiState) {
            LoginUiState.Idle -> Unit
            LoginUiState.Success -> {
                loginViewModel.clearLoginUiState()
                onContinueToMain.invoke()
            }
            LoginUiState.SignUpRequired -> {
                loginViewModel.clearLoginUiState()
                onSignUp.invoke()
            }
            is LoginUiState.Error -> Unit
        }
    }

    Column(
        modifier = modifier
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
                    imageVector = FeelinTextIcon,
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
            onClick = {
                kakaoLogin(context, loginViewModel)
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        SocialLoginButton(
            config = SocialLoginButtonConfigs.Google,
            isLastLogin = (lastOAuthProvider == OAuthProvider.GOOGLE),
            onClick = {
                googleLogin(context, loginViewModel)
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        SignUpLaterTextButton(
            onClick = onContinueToMain,
        )
    }
}

private fun loginWithKakaoTalk(context: Context, viewModel: LoginViewModel) {
    UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
        when {
            error != null -> {
                // ClientError가 아닌 경우 (서버 에러, 네트워크 에러 등)
                if (error !is ClientError) {
                    Log.e(TAG, "loginWithKakaoTalk: Server or network error", error)
                    viewModel.updateLoginError(type = LoginErrorType.OAUTH_SERVER)
                    return@loginWithKakaoTalk
                }

                // ClientError 타입별 처리
                when (error.reason) {
                    // 사용자가 명시적으로 취소한 경우, 지원하지 않는 기능, 잘못된 파라미터
                    ClientErrorCause.Cancelled, ClientErrorCause.NotSupported, ClientErrorCause.BadParameter -> {
                        Log.e(TAG, "loginWithKakaoTalk: kakao login failure with ${error.reason}", error)
                        viewModel.updateLoginError(
                            type = LoginErrorType.OAUTH_CLIENT,
                            code = error.reason.name,
                        )
                    }

                    // 그 외의 경우 (Unknown, TokenNotFound, IllegalState 등) - 카카오계정으로 폴백
                    else -> {
                        loginWithKakaoAccount(context, viewModel)
                    }
                }
            }

            token != null -> {
                // 자체 서버 로그인 시작
                viewModel.login(oAuthProvider = OAuthProvider.KAKAO, token = token.toDomainModel())
            }
        }
    }
}

private fun loginWithKakaoAccount(context: Context, viewModel: LoginViewModel) {
    UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
        when {
            error != null -> {
                // ClientError 타입별 로깅
                if (error is ClientError) {
                    Log.e(TAG, "loginWithKakaoAccount: kakao login failure with ${error.reason}", error)
                    viewModel.updateLoginError(
                        type = LoginErrorType.OAUTH_CLIENT,
                        code = error.reason.name,
                    )
                } else {
                    Log.e(TAG, "loginWithKakaoAccount: Server or network error", error)
                    viewModel.updateLoginError(type = LoginErrorType.OAUTH_SERVER)
                }
            }

            token != null -> {
                // 자체 서버 로그인 시작
                viewModel.login(oAuthProvider = OAuthProvider.KAKAO, token = token.toDomainModel())
            }
        }
    }
}

private fun kakaoLogin(context: Context, viewModel: LoginViewModel) {
    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
        loginWithKakaoTalk(context, viewModel)
    } else {
        loginWithKakaoAccount(context, viewModel)
    }
}

@Suppress("UnusedParameter") // TODO(@이대근): 구글 로그인 연동 시 어노테이션 삭제 2025.11.02.
private fun googleLogin(context: Context, viewModel: LoginViewModel) {
    // TODO(@이대근): Google Credential Manager 연동 2025.11.02.
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
            modifier = Modifier.clickable(onClick = onClick),
        )
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    FeelinTheme {
        LoginScreen(
            onSignUp = {},
            onContinueToMain = {}
        )
    }
}
