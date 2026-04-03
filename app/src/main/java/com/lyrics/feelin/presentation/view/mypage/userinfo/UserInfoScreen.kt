package com.lyrics.feelin.presentation.view.mypage.userinfo

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.R
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithBack
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.KAKAO_YELLOW
import com.lyrics.feelin.presentation.designsystem.theme.LightGray01
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.mypage.component.SettingMenuItem

@Composable
fun UserInfoScreen(modifier: Modifier = Modifier) {
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
                onBackClick = { /* TODO: 내비게이션 pop */ }
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
            SettingInfoWithBtnItem(title = uid, onClick = { /* TODO: UID 복사기능 및 스낵바 표시 */ })

            UserInfoCategoryDivider()

            InnerTitle(title = "성별 및 출생 년도")
            SettingMenuItem("${gender}ㆍ${birthYear}년", onClick = {})
        }
    }
}

@Composable
private fun InnerTitle(title: String, modifier: Modifier = Modifier) {
    val feelinColors = LocalFeelinColors.current
    Text(
        text = title,
        style = FeelinTypography.body2,
        color = feelinColors.gray04,
        modifier = modifier.padding(horizontal = 20.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun UserInfoCategoryDivider() {
    val feelinColors = LocalFeelinColors.current

    Spacer(modifier = Modifier.height(24.dp))
    HorizontalDivider(
        modifier = Modifier,
        thickness = 1.dp,
        color = feelinColors.backgroundTertiary
    )
    Spacer(modifier = Modifier.height(24.dp))
}

data class LoginInfoConfig(
    @param:DrawableRes val iconRes: Int,
    val text: String,
    val iconBackgroundColor: Color,
)

object LoginInfoConfigs {
    val kakao = LoginInfoConfig(
        iconRes = R.drawable.kakao_login_icon,
        text = "카카오 로그인",
        iconBackgroundColor = Color(KAKAO_YELLOW),
    )
    val google = LoginInfoConfig(
        iconRes = R.drawable.google_login_icon,
        text = "Google 로그인",
        iconBackgroundColor = LightGray01
    )
}

@Composable
fun UserLoginInfoItem(config: LoginInfoConfig, modifier: Modifier = Modifier) {
    val feelinColors = LocalFeelinColors.current

    Row(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(shape = RoundedCornerShape(4.dp))
                .background(color = config.iconBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(config.iconRes),
                contentDescription = config.text,
                modifier = Modifier.width(14.dp),
                tint = Color.Unspecified
            )
        }
        Spacer(modifier = Modifier.size(6.dp))
        Text(
            text = config.text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = FeelinTypography.body1.copy(color = feelinColors.gray09)
        )
    }
}

@Composable
private fun SettingInfoWithBtnItem(title: String, onClick: () -> Unit) {
    val feelinColors = LocalFeelinColors.current

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clickable(onClick = { onClick.invoke() }),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = FeelinTypography.body1.copy(color = feelinColors.gray09),
            modifier = Modifier
                .weight(1f)
                .padding(end = 18.dp)
        )

        Box(
            modifier = Modifier
                .height(28.dp)
                .clip(shape = RoundedCornerShape(4.dp))
                .background(color = feelinColors.brandSecondary)
                .clickable(enabled = true, onClick = onClick::invoke)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "복사",
                style = FeelinTypography.body2.copy(
                    color = feelinColors.brandPrimary
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun UserInfoScreenPreview() {
    FeelinTheme {
        UserInfoScreen()
    }
}
