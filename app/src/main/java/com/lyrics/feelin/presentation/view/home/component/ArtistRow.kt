package com.lyrics.feelin.presentation.view.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.component.artist.ArtistBubbleComponent
import com.lyrics.feelin.presentation.view.component.artist.ArtistBubbleComponentData

@Composable
fun ArtistRow(
    artists: List<ArtistBubbleComponentData>,
    onArtistClick: (Long) -> Unit,
    onShowAllArtistsClick: () -> Unit,
    onFindArtistsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val feelinColors = LocalFeelinColors.current

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "나의 관심 아티스트",
                style = FeelinTypography.heading3,
                color = feelinColors.gray09
            )
            Text(
                text = "전체보기",
                style = FeelinTypography.title3,
                color = feelinColors.systemActivate,
                modifier = Modifier.clickable(onClick = onShowAllArtistsClick)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(artists) { artist ->
                // 아티스트 항목은 상세 페이지로, '찾아보기'는 아티스트 검색 화면으로 분기한다.
                val onClick = when (artist) {
                    is ArtistBubbleComponentData.HomeFavoriteArtistType -> {
                        artist.id?.let { id -> { onArtistClick(id) } }
                    }
                    is ArtistBubbleComponentData.HomeFavoriteSearchType -> {
                        onFindArtistsClick
                    }
                    else -> null
                }
                ArtistBubbleComponent(
                    state = artist,
                    onClick = onClick
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ArtistRowPreview() {
    FeelinTheme {
        ArtistRow(
            artists = listOf(
                ArtistBubbleComponentData.HomeFavoriteSearchType(name = "찾아보기"),
                ArtistBubbleComponentData.HomeFavoriteArtistType(
                    name = "검정치마",
                    imageUrl = "https://example.com/image.png"
                ),
                ArtistBubbleComponentData.HomeFavoriteArtistType(
                    name = "혁오",
                    imageUrl = "https://example.com/image.png"
                )
            ),
            onArtistClick = {},
            onFindArtistsClick = {},
            onShowAllArtistsClick = {},
        )
    }
}
