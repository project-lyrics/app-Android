package com.lyrics.feelin.presentation.view.mypage.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.R
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.KAKAO_YELLOW
import com.lyrics.feelin.presentation.designsystem.theme.LightGray01
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

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
        iconBackgroundColor = LightGray01,
    )
}

/**
 * 로그인 수단의 아이콘과 이름을 함께 보여주는 계정 정보 항목입니다.
 */
@Composable
fun UserLoginInfoItem(config: LoginInfoConfig, modifier: Modifier = Modifier) {
    val feelinColors = LocalFeelinColors.current

    MyPageItemRowShell(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(shape = RoundedCornerShape(4.dp))
                    .background(color = config.iconBackgroundColor),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(config.iconRes),
                    contentDescription = config.text,
                    modifier = Modifier.width(14.dp),
                    tint = Color.Unspecified,
                )
            }
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                text = config.text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = FeelinTypography.body1.copy(color = feelinColors.gray09),
            )
        },
    )
}
