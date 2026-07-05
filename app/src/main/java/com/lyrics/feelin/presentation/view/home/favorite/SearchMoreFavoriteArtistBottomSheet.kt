package com.lyrics.feelin.presentation.view.home.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lyrics.feelin.core.designsystem.component.FeelinGrayButton
import com.lyrics.feelin.core.designsystem.component.FeelinSearchInputField
import com.lyrics.feelin.core.designsystem.icon.CloseIcon
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.util.openExternalBrowser
import com.lyrics.feelin.presentation.view.component.artist.ArtistBubbleComponent
import com.lyrics.feelin.presentation.view.component.artist.ArtistBubbleComponentData
import com.lyrics.feelin.presentation.view.home.component.HomeBottomSheetScaffold
import kotlinx.coroutines.flow.collectLatest

private const val GRID_COL_MAX_ELEMENTS = 3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchMoreFavoriteArtistBottomSheet(
    onArtistClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchMoreFavoriteArtistViewModel = hiltViewModel(),
    onDismissRequest: () -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val feelinColors = LocalFeelinColors.current
    val context = LocalContext.current

    val searchState = rememberTextFieldState()
    val artists by viewModel.artists.collectAsStateWithLifecycle()
    val isEmpty by viewModel.isEmpty.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        snapshotFlow { searchState.text.toString() }
            .collectLatest { keyword ->
                viewModel.updateSearchQuery(keyword)
            }
    }

    HomeBottomSheetScaffold(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = CloseIcon,
                        contentDescription = "닫기",
                        tint = feelinColors.gray09
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "새로운 관심 아티스트를 찾아보세요",
                        style = FeelinTypography.heading3,
                        color = feelinColors.gray09
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "아티스트를 클릭하여 레코드에 입장할 수 있어요",
                        style = FeelinTypography.body3,
                        color = feelinColors.gray04
                    )
                }

                Spacer(modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            FeelinSearchInputField(
                state = searchState,
                placeholder = "아티스트 검색",
                onClearClick = { searchState.clearText() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (isEmpty) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "검색 결과가 없어요.",
                            style = FeelinTypography.body3,
                            color = feelinColors.gray09
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        FeelinGrayButton(
                            text = "아티스트 요청하기",
                            onClick = {
                                context.openExternalBrowser(
                                    "https://forms.gle/nvxuLVfr1WuvFqrq8"
                                )
                            },
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(GRID_COL_MAX_ELEMENTS),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 40.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    items(artists, key = { it.id }) { artist ->
                        ArtistBubbleComponent(
                            state = ArtistBubbleComponentData.FavoriteArtistFindType(
                                name = artist.name,
                                imageUrl = artist.imageUrl,
                                id = artist.id.toLong(),
                            ),
                            modifier = Modifier.clickable {
                                onDismissRequest()
                                onArtistClick(artist.id.toLong())
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun SearchMoreFavoriteArtistBottomSheetPreview() {
    FeelinTheme {
        SearchMoreFavoriteArtistBottomSheet(
            onArtistClick = {},
            onDismissRequest = {}
        )
    }
}
