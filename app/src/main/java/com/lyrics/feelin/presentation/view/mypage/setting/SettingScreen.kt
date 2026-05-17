package com.lyrics.feelin.presentation.view.mypage.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.R
import com.lyrics.feelin.core.designsystem.component.FeelinModalDialog
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithBack
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.mypage.component.SettingInfoItem
import com.lyrics.feelin.presentation.view.mypage.component.SettingMenuItem

@Composable
fun SettingScreen(
    onBackClick: () -> Unit,
    onUserInfoClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onInternalWebViewClick: (String) -> Unit,
    onExternalBrowserClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    isLogoutLoading: Boolean = false,
) {
    val feelinColors = LocalFeelinColors.current
    var isLogoutDialogVisible by rememberSaveable { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        if (isLogoutDialogVisible) {
            FeelinModalDialog(
                title = "로그아웃 하시겠어요?",
                description = null,
                confirmButtonText = "로그아웃",
                onConfirmButtonClick = {
                    isLogoutDialogVisible = false
                    onLogoutClick()
                },
                isDismissButtonEnable = true,
                dismissButtonText = "취소",
                onDismissButtonClick = {
                    isLogoutDialogVisible = false
                },
            )
        }

        Scaffold(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
                .background(color = feelinColors.backgroundPrimary),
            containerColor = feelinColors.backgroundPrimary,
            topBar = {
                FeelinTopAppBarWithBack(
                    title = "설정",
                    showDivider = false,
                    onBackClick = onBackClick
                )
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier.padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                SettingMenuItem(title = "회원 정보", onClick = onUserInfoClick)

                SettingCategoryDivider()

                SettingInfoLink.entries.forEachIndexed { index, link ->
                    SettingMenuItem(
                        title = link.title,
                        onClick = {
                            if (link.opensInternally) {
                                onInternalWebViewClick(link.url)
                            } else {
                                onExternalBrowserClick(link.url)
                            }
                        },
                    )
                    if (index < SettingInfoLink.entries.lastIndex) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                SettingCategoryDivider()

                SettingInfoItem(title = "버전 정보", description = "최신 v1.0.0 사용 중")
                Spacer(modifier = Modifier.height(12.dp))
                SettingInfoItem(
                    title = "로그아웃",
                    onClick = {
                        isLogoutDialogVisible = true
                    },
                )
                Spacer(modifier = Modifier.height(12.dp))
                SettingInfoItem(title = "회원 탈퇴", titleColor = feelinColors.gray03)

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(R.drawable.feedback_banner),
                    contentDescription = "feedback banner",
                    modifier = Modifier.width(350.dp).height(110.dp)
                )
                Spacer(modifier = Modifier.height(22.dp))
            }
        }

        if (isLogoutLoading) {
            LogoutLoadingOverlay()
        }
    }
}

@Composable
private fun LogoutLoadingOverlay() {
    val feelinColors = LocalFeelinColors.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(feelinColors.dim)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {},
            ),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SettingCategoryDivider() {
    val feelinColors = LocalFeelinColors.current

    Column {
        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(
            modifier = Modifier,
            thickness = 8.dp,
            color = feelinColors.backgroundTertiary,
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingScreenPreview() {
    FeelinTheme {
        SettingScreen(
            onBackClick = {},
            onUserInfoClick = {},
            onLogoutClick = {},
            onInternalWebViewClick = {},
            onExternalBrowserClick = {},
        )
    }
}
