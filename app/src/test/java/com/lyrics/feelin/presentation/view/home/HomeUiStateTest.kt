package com.lyrics.feelin.presentation.view.home

import com.lyrics.feelin.core.designsystem.component.FilterButtonData
import com.lyrics.feelin.presentation.view.home.component.FeedTab
import com.lyrics.feelin.presentation.view.home.component.FeedTabState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeUiStateTest {

    @Test
    fun defaultStateHasFeedTabActive() {
        val state = HomeUiState()

        assertEquals(FeedTab.FEED, state.selectedTab)
        assertEquals(true, state.legacyMode)
    }

    @Test
    fun tabSwitchPreservesSelectedFilter() {
        val selectedFilter = FilterButtonData(id = 1L, name = "실리카겔", imageUrl = "https://picsum.photos/200")
        val feedState = FeedTabState(
            filters = listOf(selectedFilter),
            selectedFilter = selectedFilter,
        )
        val state = HomeUiState(
            tabStates = FeedTab.entries.associateWith { FeedTabState() }
                .plus(FeedTab.FEED to feedState),
        )

        val switchedState = state.copy(selectedTab = FeedTab.ARTISTS)
            .copy(selectedTab = FeedTab.FEED)

        assertEquals(selectedFilter, switchedState.currentTabState.selectedFilter)
    }

    @Test
    fun emptyNotesListShowsEmptyFlag() {
        val state = HomeUiState()

        assertTrue(state.tabStates[FeedTab.FEED]!!.notes.isEmpty())
    }

    @Test
    fun errorStateExposesMessage() {
        val expected = "홈 피드를 불러오지 못했어요."
        val state = HomeUiState(errorMessage = expected)

        assertEquals(expected, state.errorMessage)
    }

    @Test
    fun defaultStateHasNoBottomSheet() {
        val state = HomeUiState()

        assertEquals(HomeBottomSheetType.None, state.bottomSheetType)
    }

    @Test
    fun bottomSheetStateCanBeSet() {
        val myFavoriteState = HomeUiState(bottomSheetType = HomeBottomSheetType.MyFavoriteArtists)
        val searchState = HomeUiState(bottomSheetType = HomeBottomSheetType.SearchMoreArtists)

        assertEquals(HomeBottomSheetType.MyFavoriteArtists, myFavoriteState.bottomSheetType)
        assertEquals(HomeBottomSheetType.SearchMoreArtists, searchState.bottomSheetType)
    }
}
