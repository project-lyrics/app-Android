package com.lyrics.feelin.presentation.view.home.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.lyrics.feelin.core.designsystem.icon.CloseIcon
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.component.artist.ArtistBubbleComponentData
import com.lyrics.feelin.presentation.view.home.component.HomeBottomSheetScaffold

@Suppress("UnusedParameter") // MARK(@이대근): ViewModel 사용 혹은 삭제와 함께 어노테이션 삭제 2026.07.05.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFavoriteArtistsBottomSheet(
    artists: List<ArtistBubbleComponentData.HomeFavoriteArtistType>,
    onArtistClick: (Long) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyFavoriteArtistsViewModel = hiltViewModel(),
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val feelinColors = LocalFeelinColors.current

    HomeBottomSheetScaffold(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                IconButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = CloseIcon,
                        contentDescription = "닫기",
                        tint = feelinColors.gray09
                    )
                }
                Text(
                    text = "나의 관심 아티스트",
                    style = FeelinTypography.heading3,
                    color = feelinColors.gray08,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(GRID_COLUMNS),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(artists) { artist ->
                    MyFavoriteArtistItem(
                        artist = artist,
                        onClick = {
                            onDismissRequest()
                            artist.id?.let { onArtistClick(it) }
                        }
                    )
                }
            }
        }
    }
}

// MARK(@이대근): 얘 이거 왜 ArtistBubbleComponent 안쓰고 따로 만들었지?
@Composable
private fun MyFavoriteArtistItem(
    artist: ArtistBubbleComponentData.HomeFavoriteArtistType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val feelinColors = LocalFeelinColors.current

    Column(
        modifier = modifier
            .size(width = 108.dp, height = 146.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(108.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = artist.imageUrl,
                contentDescription = artist.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = artist.name,
            style = FeelinTypography.caption2,
            color = feelinColors.gray08,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MyFavoriteArtistsBottomSheetPreview() {
    val artists = listOf(
        ArtistBubbleComponentData.HomeFavoriteArtistType("검정치마", "", id = 1L),
        ArtistBubbleComponentData.HomeFavoriteArtistType("혁오", "", id = 2L),
        ArtistBubbleComponentData.HomeFavoriteArtistType("잔나비", "", id = 3L),
        ArtistBubbleComponentData.HomeFavoriteArtistType("새소년", "", id = 4L),
        ArtistBubbleComponentData.HomeFavoriteArtistType("카더가든", "", id = 5L),
    )
    FeelinTheme {
        MyFavoriteArtistsBottomSheet(
            artists = artists,
            onArtistClick = {},
            onDismissRequest = {},
        )
    }
}

private const val GRID_COLUMNS = 3
