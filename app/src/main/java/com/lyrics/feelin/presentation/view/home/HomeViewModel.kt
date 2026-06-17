package com.lyrics.feelin.presentation.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyrics.feelin.core.designsystem.component.FilterButtonData
import com.lyrics.feelin.presentation.view.component.artist.ArtistBubbleComponentData
import com.lyrics.feelin.presentation.view.component.note.NoteComponentData
import com.lyrics.feelin.presentation.view.home.component.Banner
import com.lyrics.feelin.presentation.view.home.component.FeedTab
import com.lyrics.feelin.presentation.view.home.component.FeedTabState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // TODO(@이대근): 실연동시 삭제 및 테스트코드 수정 2026.06.17.
    // 테스트에서 에러 경로를 검증하기 위한 스위치이며, 프로덕션 주입 생성자와는 분리되어 있다.
    private var shouldFailLoading = false

    internal constructor(shouldFailLoading: Boolean) : this() {
        this.shouldFailLoading = shouldFailLoading
    }

    // 최초 진입 시 배너, 관심 아티스트, 그리고 모든 탭의 피드 상태를 한 번에 받아온다.
    fun loadHomeData() {
        _uiState.update { state ->
            state.copy(
                isInitialLoading = true,
                errorMessage = null,
            )
        }

        viewModelScope.launch {
            runCatching {
                // TODO: 홈 Repository 연동 후 mock 데이터 대신 서버 응답으로 교체
                delay(LOAD_DELAY_MS.milliseconds)
                if (shouldFailLoading) error(LOAD_FAILURE_MESSAGE)
                createLoadedState()
            }.onSuccess { loadedState ->
                _uiState.update { currentState ->
                    loadedState.copy(
                        selectedTab = currentState.selectedTab,
                        isInitialLoading = false,
                        isRefreshing = false,
                    )
                }
            }.onFailure {
                _uiState.update { state ->
                    state.copy(
                        isInitialLoading = false,
                        isRefreshing = false,
                        errorMessage = LOAD_FAILURE_MESSAGE,
                    )
                }
            }
        }
    }

    // 현재 탭과 선택된 필터를 유지한 채, 해당 탭의 피드만 새로고침한다.
    fun refresh() {
        val selectedTab = _uiState.value.selectedTab
        val selectedFilter = _uiState.value.currentTabState.selectedFilter

        _uiState.update { state ->
            state.copy(isRefreshing = true, errorMessage = null)
        }

        viewModelScope.launch {
            runCatching {
                // TODO: 홈 Repository 연동 후 현재 탭 피드만 새로 요청
                delay(LOAD_DELAY_MS.milliseconds)
                if (shouldFailLoading) error(LOAD_FAILURE_MESSAGE)
                createTabState(tab = selectedTab, selectedFilter = selectedFilter)
            }.onSuccess { tabState ->
                updateTabState(selectedTab, tabState) { state ->
                    state.copy(isRefreshing = false)
                }
            }.onFailure {
                _uiState.update { state ->
                    state.copy(isRefreshing = false, errorMessage = LOAD_FAILURE_MESSAGE)
                }
            }
        }
    }

    fun selectTab(tab: FeedTab) {
        _uiState.update { state -> state.copy(selectedTab = tab) }
    }

    // 필터가 바뀌면 기존 노트를 즉시 비우고, 선택한 필터의 첫 페이지를 새로 요청한다.
    fun selectFilter(filter: FilterButtonData) {
        val selectedTab = _uiState.value.selectedTab

        updateCurrentTabState { tabState ->
            tabState.copy(
                selectedFilter = filter,
                notes = emptyList(),
                isLoading = true,
            )
        }

        viewModelScope.launch {
            // TODO: 홈 Repository 연동 후 선택 필터로 피드 재요청
            delay(LOAD_DELAY_MS.milliseconds)
            updateTabState(
                tab = selectedTab,
                tabState = createTabState(tab = selectedTab, selectedFilter = filter),
            )
        }
    }

    // 서버 응답 전에 좋아요 상태와 개수를 낙관적으로 반영하여 UI가 끊기지 않게 한다.
    fun toggleLike(noteId: Long) {
        updateCurrentTabState { tabState ->
            tabState.copy(
                notes = tabState.notes.map { note ->
                    if (note.id == noteId) {
                        val likesCount = if (note.isLiked) {
                            note.likesCount - LIKE_COUNT_DELTA
                        } else {
                            note.likesCount + LIKE_COUNT_DELTA
                        }

                        note.copy(
                            isLiked = !note.isLiked,
                            likesCount = likesCount.coerceAtLeast(MIN_LIKE_COUNT),
                        )
                    } else {
                        note
                    }
                }
            )
        }
    }

    // 서버 응답 전에 북마크 상태를 낙관적으로 반영하여 UI가 끊기지 않게 한다.
    fun toggleBookmark(noteId: Long) {
        updateCurrentTabState { tabState ->
            tabState.copy(
                notes = tabState.notes.map { note ->
                    if (note.id == noteId) note.copy(isBookmarked = !note.isBookmarked) else note
                }
            )
        }
    }

    fun clearError() {
        _uiState.update { state -> state.copy(errorMessage = null) }
    }

    private fun createLoadedState(): HomeUiState {
        val currentState = _uiState.value
        val tabStates = FeedTab.entries.associateWith { tab ->
            val selectedFilter = currentState.tabStates[tab]?.selectedFilter
            createTabState(tab = tab, selectedFilter = selectedFilter)
        }

        return currentState.copy(
            banner = Banner(
                imageUrl = "https://picsum.photos/seed/home-banner/800/320",
                linkUrl = "https://example.com",
            ),
            artists = mockArtists(),
            tabStates = tabStates,
            hasUnreadNotification = true,
            errorMessage = null,
        )
    }

    private fun createTabState(tab: FeedTab, selectedFilter: FilterButtonData?): FeedTabState {
        val filters = mockFilters()
        val resolvedFilter = selectedFilter ?: filters.firstOrNull()

        return FeedTabState(
            filters = filters,
            selectedFilter = resolvedFilter,
            notes = mockNotes(tab = tab, selectedFilter = resolvedFilter),
            isLoading = false,
            hasMore = false,
        )
    }

    // 현재 선택된 탭의 상태만 변경하고, 다른 탭의 필터/노트/페이징 상태는 그대로 보존한다.
    private fun updateCurrentTabState(transform: (FeedTabState) -> FeedTabState) {
        val selectedTab = _uiState.value.selectedTab

        _uiState.update { state ->
            state.copy(
                tabStates = state.tabStates.plus(
                    selectedTab to transform(state.tabStates.getValue(selectedTab))
                )
            )
        }
    }

    private fun updateTabState(
        tab: FeedTab,
        tabState: FeedTabState,
        transform: (HomeUiState) -> HomeUiState = { it },
    ) {
        _uiState.update { state ->
            transform(
                state.copy(tabStates = state.tabStates.plus(tab to tabState))
            )
        }
    }

    companion object {
        private const val LOAD_DELAY_MS = 500L
        private const val LOAD_FAILURE_MESSAGE = "홈 데이터를 불러오지 못했어요."
        private const val LIKE_COUNT_DELTA = 1
        private const val MIN_LIKE_COUNT = 0

        private fun mockArtists(): List<ArtistBubbleComponentData> {
            return listOf(
                ArtistBubbleComponentData.HomeFavoriteSearchType(name = "찾아보기"),
                ArtistBubbleComponentData.HomeFavoriteArtistType(
                    id = 1L,
                    name = "실리카겔",
                    imageUrl = "https://picsum.photos/seed/silica-gel/200/200",
                ),
                ArtistBubbleComponentData.HomeFavoriteArtistType(
                    id = 2L,
                    name = "쏜애플",
                    imageUrl = "https://picsum.photos/seed/thornapple/200/200",
                ),
            )
        }

        private fun mockFilters(): List<FilterButtonData> {
            return listOf(
                FilterButtonData(
                    id = null,
                    name = "전체",
                    imageUrl = null
                ),
                FilterButtonData(
                    id = null,
                    name = "인기",
                    imageUrl = null
                ),
                FilterButtonData(
                    id = null,
                    name = "해석공유",
                    imageUrl = null,
                ),
            )
        }

        private fun mockNotes(tab: FeedTab, selectedFilter: FilterButtonData?): List<NoteComponentData> {
            val tabOffset = if (tab == FeedTab.FEED) FEED_ID_OFFSET else ARTIST_ID_OFFSET
            val filterOffset = selectedFilter?.id?.coerceAtLeast(MIN_FILTER_OFFSET) ?: MIN_FILTER_OFFSET

            return List(MOCK_NOTE_COUNT) { index ->
                NoteComponentData.sample().copy(
                    id = tabOffset + filterOffset + index,
                    content = "${selectedFilter?.name ?: "전체"} 홈 노트 ${index + NOTE_LABEL_OFFSET}",
                    isLiked = false,
                    isBookmarked = false,
                )
            }
        }

        private const val FEED_ID_OFFSET = 100L
        private const val ARTIST_ID_OFFSET = 200L
        private const val MIN_FILTER_OFFSET = 0L
        private const val MOCK_NOTE_COUNT = 3
        private const val NOTE_LABEL_OFFSET = 1
    }
}
