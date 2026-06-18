package com.lyrics.feelin.presentation.view.home

import com.lyrics.feelin.presentation.view.home.component.FeedTab
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        viewModel = HomeViewModel()
    }

    @After
    fun tearDown() {
        viewModel.clearError()
    }

    @Test
    fun loadHomeDataPopulatesBannerArtistsAndNotes() = runTest {
        viewModel.loadHomeData()
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertNotNull(state.banner)
        assertTrue(state.artists.isNotEmpty())
        assertTrue(state.currentTabState.filters.isNotEmpty())
        assertTrue(state.currentTabState.notes.isNotEmpty())
        assertFalse(state.isInitialLoading)
    }

    @Test
    fun defaultModeLoadKeepsFeedWholeFilter() = runTest {
        viewModel.loadHomeData()
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertFalse(state.legacyMode)
        assertEquals(FeedTab.FEED, state.selectedTab)
        assertEquals("전체", state.currentTabState.selectedFilter?.name)
    }

    @Test
    fun legacyModeInitialAndLoadForceArtistsWholeFilter() = runTest {
        val legacyViewModel = HomeViewModel.createForTest(initialLegacyMode = true)

        assertTrue(legacyViewModel.uiState.value.legacyMode)
        assertEquals(FeedTab.ARTISTS, legacyViewModel.uiState.value.selectedTab)

        legacyViewModel.loadHomeData()
        advanceUntilIdle()

        val state = legacyViewModel.uiState.value

        assertTrue(state.legacyMode)
        assertEquals(FeedTab.ARTISTS, state.selectedTab)
        assertEquals("전체", state.currentTabState.selectedFilter?.name)
        assertEquals(state.currentTabState.filters.first(), state.currentTabState.selectedFilter)
    }

    @Test
    fun selectTabKeepsExistingFilter() = runTest {
        viewModel.loadHomeData()
        advanceUntilIdle()
        val feedFilter = viewModel.uiState.value.currentTabState.filters[1]
        viewModel.selectFilter(feedFilter)
        advanceUntilIdle()

        viewModel.selectTab(FeedTab.ARTISTS)
        advanceUntilIdle()
        viewModel.selectTab(FeedTab.FEED)

        assertEquals(feedFilter, viewModel.uiState.value.currentTabState.selectedFilter)
    }

    @Test
    fun selectFilterClearsNotes() = runTest {
        viewModel.loadHomeData()
        advanceUntilIdle()
        val nextFilter = viewModel.uiState.value.currentTabState.filters[1]

        viewModel.selectFilter(nextFilter)

        assertEquals(nextFilter, viewModel.uiState.value.currentTabState.selectedFilter)
        assertTrue(viewModel.uiState.value.currentTabState.notes.isEmpty())
    }

    @Test
    fun toggleLikeUpdatesImmediately() = runTest {
        viewModel.loadHomeData()
        advanceUntilIdle()
        val note = viewModel.uiState.value.currentTabState.notes.first()

        viewModel.toggleLike(note.id)

        val updatedNote = viewModel.uiState.value.currentTabState.notes.first { it.id == note.id }
        assertTrue(updatedNote.isLiked)
        assertEquals(note.likesCount + 1, updatedNote.likesCount)
    }

    @Test
    fun toggleBookmarkUpdatesImmediately() = runTest {
        viewModel.loadHomeData()
        advanceUntilIdle()
        val note = viewModel.uiState.value.currentTabState.notes.first()

        viewModel.toggleBookmark(note.id)

        val updatedNote = viewModel.uiState.value.currentTabState.notes.first { it.id == note.id }
        assertTrue(updatedNote.isBookmarked)
    }

    @Test
    fun refreshSetsAndClearsIsRefreshing() = runTest {
        viewModel.loadHomeData()
        advanceUntilIdle()

        viewModel.refresh()

        assertTrue(viewModel.uiState.value.isRefreshing)
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isRefreshing)
    }

    @Test
    fun legacyModeRefreshKeepsArtistsWholeFilter() = runTest {
        val legacyViewModel = HomeViewModel.createForTest(initialLegacyMode = true)
        legacyViewModel.loadHomeData()
        advanceUntilIdle()

        legacyViewModel.refresh()
        advanceUntilIdle()

        val state = legacyViewModel.uiState.value

        assertEquals(FeedTab.ARTISTS, state.selectedTab)
        assertEquals("전체", state.currentTabState.selectedFilter?.name)
        assertEquals(state.currentTabState.filters.first(), state.currentTabState.selectedFilter)
        assertFalse(state.isRefreshing)
    }

    @Test
    fun legacyModeSelectTabReturnsToArtists() = runTest {
        val legacyViewModel = HomeViewModel.createForTest(initialLegacyMode = true)
        legacyViewModel.loadHomeData()
        advanceUntilIdle()

        legacyViewModel.selectTab(FeedTab.FEED)

        assertEquals(FeedTab.ARTISTS, legacyViewModel.uiState.value.selectedTab)
        assertEquals(
            legacyViewModel.uiState.value.currentTabState.filters.first(),
            legacyViewModel.uiState.value.currentTabState.selectedFilter,
        )
        assertEquals("전체", legacyViewModel.uiState.value.currentTabState.selectedFilter?.name)
    }

    @Test
    fun legacyModeSelectFilterKeepsArtistsWholeFilterImmediatelyAndAfterLoad() = runTest {
        val legacyViewModel = HomeViewModel.createForTest(initialLegacyMode = true)
        legacyViewModel.loadHomeData()
        advanceUntilIdle()
        val nextFilter = legacyViewModel.uiState.value.currentTabState.filters[1]

        legacyViewModel.selectFilter(nextFilter)

        assertEquals(FeedTab.ARTISTS, legacyViewModel.uiState.value.selectedTab)
        assertEquals("전체", legacyViewModel.uiState.value.currentTabState.selectedFilter?.name)
        assertEquals(
            legacyViewModel.uiState.value.currentTabState.filters.first(),
            legacyViewModel.uiState.value.currentTabState.selectedFilter,
        )
        assertTrue(legacyViewModel.uiState.value.currentTabState.notes.isEmpty())

        advanceUntilIdle()

        assertEquals(FeedTab.ARTISTS, legacyViewModel.uiState.value.selectedTab)
        assertEquals("전체", legacyViewModel.uiState.value.currentTabState.selectedFilter?.name)
        assertEquals(
            legacyViewModel.uiState.value.currentTabState.filters.first(),
            legacyViewModel.uiState.value.currentTabState.selectedFilter,
        )
        assertTrue(
            legacyViewModel.uiState.value.currentTabState.notes.all { note ->
                note.content.startsWith("전체")
            }
        )
        assertTrue(legacyViewModel.uiState.value.currentTabState.notes.isNotEmpty())
    }

    @Test
    fun legacyModeTransitionToCurrentModeStartsFromArtistsThenAllowsSelection() = runTest {
        val legacyViewModel = HomeViewModel.createForTest(initialLegacyMode = true)
        legacyViewModel.loadHomeData()
        advanceUntilIdle()

        legacyViewModel.setLegacyModeForTest(false)

        assertFalse(legacyViewModel.uiState.value.legacyMode)
        assertEquals(FeedTab.ARTISTS, legacyViewModel.uiState.value.selectedTab)
        assertEquals("전체", legacyViewModel.uiState.value.currentTabState.selectedFilter?.name)
        assertEquals(
            legacyViewModel.uiState.value.currentTabState.filters.first(),
            legacyViewModel.uiState.value.currentTabState.selectedFilter,
        )

        legacyViewModel.selectTab(FeedTab.FEED)

        assertEquals(FeedTab.FEED, legacyViewModel.uiState.value.selectedTab)

        val nextFilter = legacyViewModel.uiState.value.currentTabState.filters[1]
        legacyViewModel.selectFilter(nextFilter)

        assertEquals(nextFilter, legacyViewModel.uiState.value.currentTabState.selectedFilter)
    }

    @Test
    fun initialLoadShowsOverlay() = runTest {
        viewModel.loadHomeData()

        assertTrue(viewModel.uiState.value.isInitialLoading)
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.isInitialLoading)
    }

    @Test
    fun loadFailureShowsErrorMessage() = runTest {
        val failingViewModel = HomeViewModel(shouldFailLoading = true)

        failingViewModel.loadHomeData()
        advanceUntilIdle()

        assertEquals("홈 데이터를 불러오지 못했어요.", failingViewModel.uiState.value.errorMessage)
        assertFalse(failingViewModel.uiState.value.isInitialLoading)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
