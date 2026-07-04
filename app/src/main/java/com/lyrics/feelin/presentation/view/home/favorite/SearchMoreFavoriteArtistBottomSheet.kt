package com.lyrics.feelin.presentation.view.home.favorite

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.lyrics.feelin.core.designsystem.component.FeelinSearchInputField
import com.lyrics.feelin.core.designsystem.icon.CloseIcon
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.home.component.HomeBottomSheetScaffold
import com.lyrics.feelin.presentation.view.onboarding.favoriteartist.FavoriteArtistData
import kotlinx.coroutines.flow.collectLatest

private const val GRID_COL_MAX_ELEMENTS = 3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchMoreFavoriteArtistBottomSheet(
    onArtistClick: (Long) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchMoreFavoriteArtistViewModel = hiltViewModel(),
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
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalAlignment = Alignment.Top
            ) {
                IconButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = CloseIcon,
                        contentDescription = "Close",
                        tint = feelinColors.gray08
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "새로운 관심 아티스트를 찾아보세요",
                        style = FeelinTypography.heading3,
                        color = feelinColors.gray08
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "아티스트를 클릭하여 레코드에 입장할 수 있어요",
                        style = FeelinTypography.body3,
                        color = feelinColors.gray04
                    )
                }
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

            Spacer(modifier = Modifier.height(24.dp))

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
                            style = FeelinTypography.body2,
                            color = feelinColors.gray04
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://forms.gle/nvxuLVfr1WuvFqrq8")
                                )
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = feelinColors.gray01,
                                contentColor = feelinColors.gray07
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "아티스트 요청하기",
                                style = FeelinTypography.body2
                            )
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(GRID_COL_MAX_ELEMENTS),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 40.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(artists, key = { it.id }) { artist ->
                        SearchArtistItem(
                            artist = artist,
                            onClick = {
                                onDismissRequest()
                                onArtistClick(artist.id.toLong())
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchArtistItem(
    artist: FavoriteArtistData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val feelinColors = LocalFeelinColors.current

    Column(
        modifier = modifier
            .width(108.dp)
            .height(146.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(108.dp)
                .background(
                    color = feelinColors.gray00,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = artist.imageUrl,
                contentDescription = artist.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(feelinColors.gray01)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = artist.name,
            style = FeelinTypography.body2,
            color = feelinColors.gray08,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
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
