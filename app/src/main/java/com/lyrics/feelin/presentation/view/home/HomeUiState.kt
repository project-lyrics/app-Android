package com.lyrics.feelin.presentation.view.home

import com.lyrics.feelin.presentation.view.component.artist.ArtistBubbleComponentData
import com.lyrics.feelin.presentation.view.home.component.Banner
import com.lyrics.feelin.presentation.view.home.component.FeedTab
import com.lyrics.feelin.presentation.view.home.component.FeedTabState

/**
 * 홈 화면에서 표시할 바텀 시트 종류.
 */
enum class HomeBottomSheetType {
    None,
    MyFavoriteArtists,
    SearchMoreArtists,
}

/**
 * 홈 화면의 UI 상태.
 *
 * 각 탭(FEED/ARTISTS)마다 독립적인 필터, 노트 목록, 페이징 상태를 유지한다.
 */
data class HomeUiState(
    val banner: Banner? = null,
    val artists: List<ArtistBubbleComponentData> = emptyList(),
    val selectedTab: FeedTab = FeedTab.FEED,
    /** 피드를 기존 방식(관심 아티스트 + 전체)으로만 보여줄지 신규 방식(전체/관심 탭, 아티스트 필터)으로 보여줄지 판단하는 플래그 */
    val legacyMode: Boolean = true,
    val tabStates: Map<FeedTab, FeedTabState> = FeedTab.entries.associateWith { FeedTabState() },
    val hasUnreadNotification: Boolean = false,
    val isRefreshing: Boolean = false,
    val isInitialLoading: Boolean = false,
    val errorMessage: String? = null,
    val bottomSheetType: HomeBottomSheetType = HomeBottomSheetType.None,
) {
    val currentTabState: FeedTabState
        get() = tabStates.getValue(selectedTab)
}
