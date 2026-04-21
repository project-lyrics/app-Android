package com.lyrics.feelin.presentation.view.note.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lyrics.feelin.core.designsystem.component.FeelinSearchInputField
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarNoBack
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.component.music.MusicComponent
import com.lyrics.feelin.presentation.view.component.music.MusicComponentData
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop

private const val SEARCH_DEBOUNCE_MS = 1000L
private const val PAGINATION_PREFETCH_THRESHOLD = 3

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun NoteSearchScreen(
    modifier: Modifier = Modifier,
    onMusicClick: (MusicComponentData.SearchNoteByMusic) -> Unit = {},
    viewModel: NoteSearchViewModel = viewModel(),
) {
    val viewState by viewModel.viewState.collectAsState()
    val searchState = rememberTextFieldState()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        viewModel.loadInitialNotes()
    }

    LaunchedEffect(Unit) {
        snapshotFlow { searchState.text.toString() }
            .drop(1)
            .debounce(SEARCH_DEBOUNCE_MS)
            .distinctUntilChanged()
            .collectLatest { keyword ->
                viewModel.searchNotes(keyword)
            }
    }

    NoteSearchScreenContent(
        viewState = viewState,
        searchState = searchState,
        onRefresh = viewModel::refresh,
        onLoadNextPage = viewModel::loadNextPage,
        onSearchClick = { keyboardController?.hide() },
        onClearClick = { searchState.clearText() },
        onMusicClick = onMusicClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteSearchScreenContent(
    viewState: NoteSearchViewState,
    searchState: TextFieldState,
    onRefresh: () -> Unit,
    onLoadNextPage: () -> Unit,
    onSearchClick: () -> Unit,
    onClearClick: () -> Unit,
    onMusicClick: (MusicComponentData.SearchNoteByMusic) -> Unit,
    modifier: Modifier = Modifier,
) {
    val feelinColors = LocalFeelinColors.current
    val listState = rememberLazyListState()
    val currentOnLoadNextPage = rememberUpdatedState(onLoadNextPage)

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collectLatest { lastVisibleItemIndex ->
                val totalItemsCount = listState.layoutInfo.totalItemsCount
                if (lastVisibleItemIndex == null || totalItemsCount == 0) return@collectLatest

                if (lastVisibleItemIndex >= totalItemsCount - PAGINATION_PREFETCH_THRESHOLD) {
                    currentOnLoadNextPage.value()
                }
            }
    }

    Scaffold(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
            .background(color = feelinColors.backgroundPrimary),
        topBar = {
            FeelinTopAppBarNoBack(
                title = "노트 검색",
                showDivider = false,
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .background(feelinColors.backgroundPrimary)
                .padding(contentPadding)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            FeelinSearchInputField(
                state = searchState,
                placeholder = "곡 검색",
                onSearchClick = onSearchClick,
                onClearClick = onClearClick,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            PullToRefreshBox(
                isRefreshing = viewState.listStatus == NoteSearchListStatus.REFRESHING,
                onRefresh = onRefresh,
                modifier = Modifier.weight(1f),
            ) {
                when (viewState.screenStatus) {
                    NoteSearchScreenStatus.INITIAL,
                    NoteSearchScreenStatus.LOADING -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    NoteSearchScreenStatus.ERROR -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = viewState.errorMessage ?: "오류가 발생했습니다.",
                            )
                        }
                    }

                    NoteSearchScreenStatus.SUCCESS_LOAD -> {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            items(
                                items = viewState.searchResults,
                                key = { "${it.songName}-${it.artistName}" },
                            ) { song ->
                                MusicComponent(
                                    state = song,
                                    onClick = { onMusicClick(song) }
                                )
                            }

                            if (viewState.listStatus == NoteSearchListStatus.NEW_PAGE_LOADING) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 16.dp),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NoteSearchScreenPreview() {
    FeelinTheme {
        val searchState = rememberTextFieldState()
        NoteSearchScreenContent(
            viewState = NoteSearchViewState.success(
                searchResults = previewSongs(),
                hasNextPage = true,
            ),
            searchState = searchState,
            onRefresh = {},
            onLoadNextPage = {},
            onSearchClick = {},
            onClearClick = { searchState.clearText() },
            onMusicClick = {},
        )
    }
}

@Suppress("MagicNumber")
private fun previewSongs(): List<MusicComponentData.SearchNoteByMusic> {
    return List(8) { index ->
        MusicComponentData.SearchNoteByMusic(
            imageUrl = "https://picsum.photos/seed/note-search-preview-$index/200/200",
            songName = "Realize $index",
            artistName = "실리카겔",
            noteCount = 999 - index,
        )
    }
}
