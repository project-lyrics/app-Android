package com.lyrics.feelin.presentation.view.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.component.FeelinTab
import com.lyrics.feelin.core.designsystem.component.FeelinTabRow
import com.lyrics.feelin.core.designsystem.component.FilterButton
import com.lyrics.feelin.core.designsystem.component.FilterButtonData
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.component.note.NoteComponent
import com.lyrics.feelin.presentation.view.component.note.NoteComponentData
import com.lyrics.feelin.presentation.view.home.HomeUiState

/**
 * 홈 화면의 피드 섹션을 [LazyListScope]에 직접 방출한다.
 *
 * 상위 [androidx.compose.foundation.lazy.LazyColumn]의 가상화를 유지하기 위해
 * 이 컴포넌트는 독립적인 [androidx.compose.foundation.lazy.LazyColumn]이 아닌
 * [LazyListScope] 확장 함수로 구현되어 있다.
 */
fun LazyListScope.feedSection(
    uiState: HomeUiState,
    onTabClick: (FeedTab) -> Unit = {},
    onFilterClick: (FilterButtonData) -> Unit = {},
    onNoteClick: (Long) -> Unit = {},
    onNoteLikeClick: (Long) -> Unit = {},
    onNoteBookmarkClick: (Long) -> Unit = {},
    onNoteMenuClick: (NoteComponentData) -> Unit = {},
) {
    feedSectionHeader(
        selectedTab = uiState.selectedTab,
        legacyMode = uiState.legacyMode,
        onTabClick = onTabClick
    )

    if (!uiState.legacyMode) {
        feedSectionFilters(
            filters = uiState.currentTabState.filters,
            selectedFilter = uiState.currentTabState.selectedFilter,
            onFilterClick = onFilterClick
        )
    }

    feedSectionNotes(
        notes = uiState.currentTabState.notes,
        onNoteClick = onNoteClick,
        onNoteLikeClick = onNoteLikeClick,
        onNoteBookmarkClick = onNoteBookmarkClick,
        onNoteMenuClick = onNoteMenuClick,
    )
}

private fun LazyListScope.feedSectionHeader(
    selectedTab: FeedTab,
    legacyMode: Boolean,
    onTabClick: (FeedTab) -> Unit,
) {
    item(key = "feedHeader") {
        val feelinColors = LocalFeelinColors.current

        Text(
            text = "따끈따끈한 노트",
            style = FeelinTypography.heading3,
            color = feelinColors.gray09,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
        )
    }

    item(key = "feedHeaderSpacing") {
        Spacer(modifier = Modifier.height(8.dp))
    }

    if (legacyMode) return

    item(key = "feedTabRow") {
        FeelinTabRow(
            selectedTabIndex = selectedTab.ordinal,
            modifier = Modifier.fillMaxWidth()
        ) {
            FeelinTab(
                selected = selectedTab == FeedTab.FEED,
                onClick = { onTabClick(FeedTab.FEED) },
                text = { Text("피드") }
            )
            FeelinTab(
                selected = selectedTab == FeedTab.ARTISTS,
                onClick = { onTabClick(FeedTab.ARTISTS) },
                text = { Text("관심 아티스트") }
            )
        }
    }

    item(key = "feedTabSpacing") {
        Spacer(modifier = Modifier.height(20.dp))
    }
}

private fun LazyListScope.feedSectionFilters(
    filters: List<FilterButtonData>,
    selectedFilter: FilterButtonData?,
    onFilterClick: (FilterButtonData) -> Unit,
) {
    if (filters.isEmpty()) return

    item(key = "feedFilters") {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters) { filter ->
                // MARK(@이대근): 담고 있는 데이터의 형태가 달라 같은 FliterButton을 유지할지 결정 필요
                FilterButton(
                    data = filter,
                    isSelect = selectedFilter?.id == filter.id,
                    onClick = { onFilterClick(filter) }
                )
            }
        }
    }

    item(key = "feedFilterSpacing") {
        Spacer(modifier = Modifier.height(16.dp))
    }
}

private fun LazyListScope.feedSectionNotes(
    notes: List<NoteComponentData>,
    onNoteClick: (Long) -> Unit,
    onNoteLikeClick: (Long) -> Unit,
    onNoteBookmarkClick: (Long) -> Unit,
    onNoteMenuClick: (NoteComponentData) -> Unit,
) {
    if (notes.isEmpty()) {
        item(key = "feedEmptyState") {
            EmptyState(modifier = Modifier.padding(top = 40.dp, bottom = 40.dp))
        }
        return
    }

    items(
        items = notes,
        key = { note -> note.id },
        contentType = { "NoteComponent" }
    ) { note ->
        NoteComponent(
            noteData = note,
            onClick = { onNoteClick(note.id) },
            onLikeClick = { onNoteLikeClick(note.id) },
            onBookmarkClick = { onNoteBookmarkClick(note.id) },
            onMenuClick = onNoteMenuClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
