package com.lyrics.feelin.presentation.view.home

import com.lyrics.feelin.presentation.view.home.component.FeedTabState
import org.junit.Assert.assertTrue
import org.junit.Test

class FeedTabStateTest {

    @Test
    fun defaultStateHasEmptyNotesAndFilters() {
        val state = FeedTabState()

        assertTrue(state.notes.isEmpty())
        assertTrue(state.filters.isEmpty())
    }
}
