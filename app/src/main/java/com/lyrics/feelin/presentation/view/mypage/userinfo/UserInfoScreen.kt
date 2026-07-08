package com.lyrics.feelin.presentation.view.mypage.userinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithBack
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.mypage.component.FeelinActionButton
import com.lyrics.feelin.presentation.view.mypage.component.LoginInfoConfigs
import com.lyrics.feelin.presentation.view.mypage.component.SettingInfoItem
import com.lyrics.feelin.presentation.view.mypage.component.SettingMenuItem
import com.lyrics.feelin.presentation.view.mypage.component.UserLoginInfoItem

@Composable
fun UserInfoScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val feelinColors = LocalFeelinColors.current

    val loginInfoItem by remember { mutableStateOf(LoginInfoConfigs.kakao) }
    val uid by remember { mutableStateOf("8ca0fd81-fd03-438c-8730-c6c4e7ef4aa9-8ca0fd81-fd03") }
    val gender by remember { mutableStateOf("여성") }
    val birthYear by remember { mutableStateOf("2000") }

    Scaffold(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
            .background(color = feelinColors.backgroundPrimary),
        containerColor = feelinColors.backgroundPrimary,
        topBar = {
            FeelinTopAppBarWithBack(
                title = "회원 정보",
                showDivider = false,
                onBackClick = onBackClick
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            InnerTitle(title = "로그인 정보")
            UserLoginInfoItem(config = loginInfoItem)

            UserInfoCategoryDivider()

            InnerTitle(title = "유저 ID")
            SettingInfoItem(
                title = uid,
                trailingContent = {
                    FeelinActionButton(text = "복사", onClick = { /* TODO: UID 복사기능 및 스낵바 표시 */ })
                },
            )

            UserInfoCategoryDivider()

            InnerTitle(title = "성별 및 출생 년도")
            SettingMenuItem("${gender}ㆍ${birthYear}년", onClick = {})
        }
    }
}

@Composable
private fun InnerTitle(title: String, modifier: Modifier = Modifier) {
    val feelinColors = LocalFeelinColors.current

    Column(modifier = modifier) {
        Text(
            text = title,
            style = FeelinTypography.body2,
            color = feelinColors.gray04,
            modifier = Modifier.padding(horizontal = 20.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun UserInfoCategoryDivider() {
    val feelinColors = LocalFeelinColors.current

    Column {
        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(
            modifier = Modifier,
            thickness = 1.dp,
            color = feelinColors.backgroundTertiary,
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun UserInfoScreenPreview() {
    FeelinTheme {
        UserInfoScreen(onBackClick = {})
    }
}
