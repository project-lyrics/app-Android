package com.lyrics.feelin.presentation.view.home.component

import com.lyrics.feelin.core.designsystem.component.FilterButtonData
import com.lyrics.feelin.presentation.view.component.note.NoteComponentData

/**
 * 피드 탭별 상태.
 *
 * 탭을 전환할 때도 선택한 필터와 노트 목록이 보존되어야 한다.
 */
data class FeedTabState(
    val filters: List<FilterButtonData> = emptyList(),
    val selectedFilter: FilterButtonData? = null,
    val notes: List<NoteComponentData> = emptyList(),
    val isLoading: Boolean = false,
    val hasMore: Boolean = false,
)
