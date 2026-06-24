package com.lyrics.feelin.presentation.view.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

@Composable
fun BannerSection(
    banner: Banner,
    onBannerClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = banner.imageUrl,
        contentDescription = "Banner Image",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onBannerClick(banner.linkUrl) }
    )
}

/**
 * 배너 이미지가 날아오지 않은 초기/로딩 상태에서 비율과 자리를 유지하는 플레이스홀더다.
 */
@Composable
fun DummyBannerSection(modifier: Modifier = Modifier) {
    val feelinColors = LocalFeelinColors.current

    Box(
        modifier = modifier
            .height(110.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = feelinColors.backgroundTertiary)
    )
}

@Preview(showBackground = true)
@Composable
private fun BannerSectionPreview() {
    FeelinTheme {
        BannerSection(
            banner = Banner(
                imageUrl = "https://example.com/banner.png",
                linkUrl = "https://example.com",
            ),
            onBannerClick = {},
        )
    }
}
