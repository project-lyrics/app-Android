package com.lyrics.feelin.presentation.view.home

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lyrics.feelin.core.designsystem.component.FeelinModalDialog
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.component.artist.ArtistBubbleComponentData
import com.lyrics.feelin.presentation.view.component.note.NoteComponentData
import com.lyrics.feelin.presentation.view.component.note.NoteMenuBottomSheet
import com.lyrics.feelin.presentation.view.home.component.ArtistRow
import com.lyrics.feelin.presentation.view.home.component.BannerSection
import com.lyrics.feelin.presentation.view.home.component.DummyBannerSection
import com.lyrics.feelin.presentation.view.home.component.HomeHeader
import com.lyrics.feelin.presentation.view.home.component.feedSection
import com.lyrics.feelin.presentation.view.home.favorite.MyFavoriteArtistsBottomSheet
import com.lyrics.feelin.presentation.view.home.favorite.SearchMoreFavoriteArtistBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onBannerClick: (String) -> Unit,
    onArtistClick: (Long) -> Unit,
    onNoteClick: (Long) -> Unit,
    onNoteReportClick: (Long) -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier,
    currentUserId: Long? = null,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedNoteForMenu by remember { mutableStateOf<NoteComponentData?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadHomeData()
    }

    val feelinColors = LocalFeelinColors.current

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                HomeHeader(
                    hasUnreadNotification = uiState.hasUnreadNotification,
                    onNotificationClick = onNotificationClick,
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .padding(horizontal = 20.dp)
                )
            },
            containerColor = feelinColors.backgroundPrimary,
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
                .fillMaxSize()
        ) { innerPadding ->
            PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = viewModel::refresh,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    uiState.banner?.let { banner ->
                        item {
                            BannerSection(
                                banner = banner,
                                onBannerClick = onBannerClick,
                                modifier = Modifier
                                    .padding(vertical = 24.dp, horizontal = 20.dp)
                            )
                        }
                    }

                    // 배너가 아직 날아오지 않은 초기 로딩 상태에서 레이아웃 붕괴를 막기 위한 플레이스홀더다.
                    if (uiState.banner == null && uiState.errorMessage == null) {
                        item {
                            DummyBannerSection(
                                modifier = Modifier.padding(vertical = 24.dp, horizontal = 20.dp)
                            )
                        }
                    }

                    // 풀투리프레시 등 초기 로딩이 아닌 시점의 오류는 본문 중앙에 별도 메시지를 노출한다.
                    if (uiState.errorMessage != null && !uiState.isInitialLoading) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxSize().padding(vertical = 40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "데이터를 불러오는 중 오류가 발생했습니다.")
                            }
                        }
                    } else {
                        if (uiState.artists.isNotEmpty()) {
                            item {
                                ArtistRow(
                                    artists = uiState.artists,
                                    onArtistClick = onArtistClick,
                                    onShowAllArtistsClick = viewModel::showMyFavoriteArtistsBottomSheet,
                                    onFindArtistsClick = viewModel::showSearchMoreArtistsBottomSheet,
                                )
                            }
                        }

                        if (uiState.isInitialLoading) {
                            // 관심 아티스트 행 역시 로딩 중 레이아웃 높이를 유지하기 위해 더미 아이템을 보여준다.
                            item {
                                ArtistRow(
                                    artists = listOf(
                                        ArtistBubbleComponentData.HomeFavoriteSearchType(name = "찾아보기")
                                    ),
                                    onArtistClick = {},
                                    onShowAllArtistsClick = {},
                                    onFindArtistsClick = {},
                                )
                            }
                        }

                        item {
                            Spacer(
                                modifier = Modifier
                                    .padding(top = 32.dp, bottom = 24.dp)
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .background(color = feelinColors.backgroundTertiary)
                            )
                        }

                        feedSection(
                            uiState = uiState,
                            onTabClick = viewModel::selectTab,
                            onFilterClick = viewModel::selectFilter,
                            onNoteClick = onNoteClick,
                            onNoteLikeClick = viewModel::toggleLike,
                            onNoteBookmarkClick = viewModel::toggleBookmark,
                            onNoteMenuClick = { selectedNoteForMenu = it },
                        )
                    }
                }
            }
        }

        selectedNoteForMenu?.let { selectedNote ->
            NoteMenuBottomSheet(
                noteData = selectedNote,
                currentUserId = currentUserId,
                onReportClick = { noteId ->
                    selectedNoteForMenu = null
                    onNoteReportClick(noteId)
                },
                onDismissRequest = { selectedNoteForMenu = null },
            )
        }

        if (uiState.bottomSheetType == HomeBottomSheetType.MyFavoriteArtists) {
            val favoriteArtists = uiState.artists.filterIsInstance<ArtistBubbleComponentData.HomeFavoriteArtistType>()
            MyFavoriteArtistsBottomSheet(
                artists = favoriteArtists,
                onArtistClick = { artistId ->
                    viewModel.hideBottomSheet()
                    onArtistClick(artistId)
                },
                onDismissRequest = viewModel::hideBottomSheet,
            )
        }

        if (uiState.bottomSheetType == HomeBottomSheetType.SearchMoreArtists) {
            SearchMoreFavoriteArtistBottomSheet(
                onArtistClick = { artistId ->
                    viewModel.hideBottomSheet()
                    onArtistClick(artistId)
                },
                onDismissRequest = viewModel::hideBottomSheet,
            )
        }

        // 최초 진입 시 전체 화면을 덮는 오버레이로, 중복 요청과 터치 이벤트를 차단한다.
        if (uiState.isInitialLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) { detectTapGestures { } }
                    .background(color = feelinColors.dim),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (uiState.errorMessage != null) {
            // MARK: 실제 에러와 메시지 표현 필요
            FeelinModalDialog(
                title = uiState.errorMessage!!,
                description = "에러코드 [-1]",
                confirmButtonText = "확인",
                onConfirmButtonClick = viewModel::clearError
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreviewLoading() {
    FeelinTheme {
        HomeScreen(
            viewModel = HomeViewModel(shouldFailLoading = false),
            onBannerClick = {},
            onArtistClick = {},
            onNoteClick = {},
            onNoteReportClick = {},
            onNotificationClick = {},
        )
    }
}
