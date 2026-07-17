package com.lyrics.feelin.presentation.view.notification

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NotificationViewModelTest {

    private lateinit var viewModel: NotificationViewModel

    @Before
    fun setUp() {
        viewModel = NotificationViewModel()
    }

    @Test
    fun initialStateShowsMyNews() {
        val state = viewModel.uiState.value

        assertEquals(NotificationTab.MY_NEWS, state.selectedTab)
        assertTrue(state.items.isNotEmpty())
    }

    @Test
    fun selectAllShowsEmptyState() {
        viewModel.selectTab(NotificationTab.ALL)

        val state = viewModel.uiState.value

        assertEquals(NotificationTab.ALL, state.selectedTab)
        assertTrue(state.items.isEmpty())
    }

    @Test
    fun switchingBackRestoresMyNewsItems() {
        viewModel.selectTab(NotificationTab.ALL)
        viewModel.selectTab(NotificationTab.MY_NEWS)

        val state = viewModel.uiState.value

        assertEquals(NotificationTab.MY_NEWS, state.selectedTab)
        assertTrue(state.items.isNotEmpty())
    }
}
