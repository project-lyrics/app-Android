package com.lyrics.feelin.presentation.view.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.component.FeelinTab
import com.lyrics.feelin.core.designsystem.component.FeelinTabRow
import com.lyrics.feelin.core.designsystem.component.FilterButton
import com.lyrics.feelin.core.designsystem.component.FilterButtonData
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.component.note.NoteComponent
import com.lyrics.feelin.presentation.view.home.HomeUiState

@Composable
fun FeedSection(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
    onTabClick: (FeedTab) -> Unit = {},
    onFilterClick: (FilterButtonData) -> Unit = {},
    onNoteClick: (Long) -> Unit = {},
    onNoteLikeClick: (Long) -> Unit = {},
    onNoteBookmarkClick: (Long) -> Unit = {}
) {
    val feelinColors = LocalFeelinColors.current
    val currentTabState = uiState.currentTabState

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "따끈따끈한 노트",
            style = FeelinTypography.heading3,
            color = feelinColors.gray09,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        FeelinTabRow(
            selectedTabIndex = uiState.selectedTab.ordinal,
            modifier = Modifier.fillMaxWidth()
        ) {
            FeelinTab(
                selected = uiState.selectedTab == FeedTab.FEED,
                onClick = { onTabClick(FeedTab.FEED) },
                text = { Text("피드") }
            )
            FeelinTab(
                selected = uiState.selectedTab == FeedTab.ARTISTS,
                onClick = { onTabClick(FeedTab.ARTISTS) },
                text = { Text("관심 아티스트") }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (currentTabState.filters.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(currentTabState.filters) { filter ->
                    // MARK(@이대근): 담고 있는 데이터의 형태가 달라 같은 FliterButton을 유지할지 결정 필요
                    FilterButton(
                        data = filter,
                        isSelect = currentTabState.selectedFilter?.id == filter.id,
                        onClick = { onFilterClick(filter) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (currentTabState.notes.isEmpty()) {
            EmptyState(modifier = Modifier.padding(top = 40.dp, bottom = 40.dp))
        } else {
            Column(modifier = Modifier.fillMaxWidth()) {
                currentTabState.notes.forEach { note ->
                    NoteComponent(
                        noteData = note,
                        onClick = { onNoteClick(note.id) },
                        onLikeClick = { onNoteLikeClick(note.id) },
                        onBookmarkClick = { onNoteBookmarkClick(note.id) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedSectionPreview() {
    FeelinTheme {
        FeedSection(uiState = HomeUiState())
    }
}
