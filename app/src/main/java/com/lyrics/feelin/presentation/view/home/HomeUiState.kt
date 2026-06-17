package com.lyrics.feelin.presentation.view.home

import com.lyrics.feelin.presentation.view.component.artist.ArtistBubbleComponentData
import com.lyrics.feelin.presentation.view.home.component.Banner
import com.lyrics.feelin.presentation.view.home.component.FeedTab
import com.lyrics.feelin.presentation.view.home.component.FeedTabState

/**
 * 홈 화면의 UI 상태.
 *
 * 각 탭(FEED/ARTISTS)마다 독립적인 필터, 노트 목록, 페이징 상태를 유지한다.
 */
data class HomeUiState(
    val banner: Banner? = null,
    val artists: List<ArtistBubbleComponentData> = emptyList(),
    val selectedTab: FeedTab = FeedTab.FEED,
    val tabStates: Map<FeedTab, FeedTabState> = FeedTab.entries.associateWith { FeedTabState() },
    val hasUnreadNotification: Boolean = false,
    val isRefreshing: Boolean = false,
    val isInitialLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    val currentTabState: FeedTabState
        get() = tabStates.getValue(selectedTab)
}
