package com.lyrics.feelin.presentation.view.mypage.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.R
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithBack
import com.lyrics.feelin.core.designsystem.icon.CaretIcon
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.mypage.component.SettingMenuItem

const val CARET_ROTATE_TO_RIGHT = 270f

@Composable
fun SettingScreen(modifier: Modifier = Modifier) {
    val feelinColors = LocalFeelinColors.current

    Scaffold(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
            .background(color = feelinColors.backgroundPrimary),
        topBar = {
            FeelinTopAppBarWithBack(
                title = "설정",
                showDivider = false,
                onBackClick = { /* TODO: 내비게이션 pop */ }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            SettingMenuItem(title = "회원 정보", onClick = {})

            SettingCategoryDivider()

            SettingMenuItem(title = "서비스 이용 약관", onClick = {})
            Spacer(modifier = Modifier.height(16.dp))
            SettingMenuItem(title = "개인정보처리방침", onClick = {})
            Spacer(modifier = Modifier.height(16.dp))
            SettingMenuItem(title = "서비스 문의하기", onClick = {})

            SettingCategoryDivider()

            SettingInfoItem(title = "버전 정보", description = "최신 v1.0.0 사용 중")
            Spacer(modifier = Modifier.height(12.dp))
            SettingInfoItem(title = "로그아웃", onClick = {})
            Spacer(modifier = Modifier.height(12.dp))
            SettingInfoItem(title = "회원 탈퇴", titleColor = feelinColors.gray03, onClick = {})

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(R.drawable.feedback_banner),
                contentDescription = "feedback banner",
                modifier = Modifier.width(350.dp).height(110.dp)
            )
            Spacer(modifier = Modifier.height(22.dp))
        }
    }
}

@Composable
private fun SettingCategoryDivider() {
    val feelinColors = LocalFeelinColors.current

    Spacer(modifier = Modifier.height(24.dp))
    HorizontalDivider(
        modifier = Modifier,
        thickness = 8.dp,
        color = feelinColors.backgroundTertiary
    )
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
private fun SettingInfoItem(
    title: String,
    description: String? = null,
    titleColor: Color? = null,
    onClick: () -> Unit = {}
) {
    val feelinColors = LocalFeelinColors.current

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clickable(onClick = { onClick.invoke() }),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, style = FeelinTypography.body1.copy(color = titleColor ?: feelinColors.gray09))

        if (description != null) {
            Text(description, style = FeelinTypography.body2.copy(color = feelinColors.gray04))
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingScreenPreview() {
    FeelinTheme {
        SettingScreen()
    }
}
