package com.lyrics.feelin.presentation.view.note.form.searchsong

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lyrics.feelin.core.designsystem.component.FeelinSearchInputField
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithBack
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.component.music.MusicComponent
import com.lyrics.feelin.presentation.view.component.music.MusicComponentData
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchSongRoute(
    onBackClick: () -> Unit,
    onSongSelect: (MusicComponentData.NoteWriteMusicExist) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchSongViewModel = hiltViewModel(),
) {
    val uiState by viewModel.viewState.collectAsState()

    SearchSongScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onSongSelect = onSongSelect,
        modifier = modifier,
    )
}

@Composable
fun SearchSongScreen(
    uiState: SearchSongUiState,
    onBackClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSongSelect: (MusicComponentData.NoteWriteMusicExist) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalFeelinColors.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.backgroundPrimary)
            .statusBarsPadding()
    ) {
        FeelinTopAppBarWithBack(
            title = "노래 추가",
            onBackClick = onBackClick,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        val currentOnSearchQueryChange by androidx.compose.runtime.rememberUpdatedState(onSearchQueryChange)
        val searchFieldState = rememberTextFieldState(initialText = uiState.searchQuery)
        LaunchedEffect(searchFieldState) {
            snapshotFlow { searchFieldState.text }.collectLatest {
                currentOnSearchQueryChange(it.toString())
            }
        }

        FeelinSearchInputField(
            state = searchFieldState,
            placeholder = "가수, 노래제목 검색",
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.searchQuery.isNotEmpty() && uiState.searchResults.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "검색 결과가 없어요",
                    style = FeelinTypography.body1,
                    color = colors.gray04,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.searchResults) { song ->
                    MusicComponent(
                        state = song,
                        onClick = {
                            onSongSelect(
                                MusicComponentData.NoteWriteMusicExist(
                                    imageUrl = song.imageUrl,
                                    songName = song.songName,
                                    artistName = song.artistName
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchSongScreenEmptyPreview() {
    FeelinTheme {
        SearchSongScreen(
            uiState = SearchSongUiState.initial(),
            onBackClick = {},
            onSearchQueryChange = {},
            onSongSelect = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchSongScreenListPreview() {
    FeelinTheme {
        SearchSongScreen(
            uiState = SearchSongUiState.searchSample(),
            onBackClick = {},
            onSearchQueryChange = {},
            onSongSelect = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchSongScreenErrorPreview() {
    FeelinTheme {
        SearchSongScreen(
            uiState = SearchSongUiState.errorSample(),
            onBackClick = {},
            onSearchQueryChange = {},
            onSongSelect = {}
        )
    }
}
