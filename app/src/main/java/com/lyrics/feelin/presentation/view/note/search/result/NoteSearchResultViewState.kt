package com.lyrics.feelin.presentation.view.note.search.result

import com.lyrics.feelin.core.domain.enum.NoteTopic
import com.lyrics.feelin.presentation.view.component.note.NoteComponentData

enum class NoteSearchResultStatus {
    INITIAL,
    LOADING,
    SUCCESS,
    EMPTY,
    ERROR,
}

data class NoteSearchResultSongSummary(
    val imageUrl: String,
    val title: String,
    val artistName: String,
)

data class NoteSearchResultViewState(
    val status: NoteSearchResultStatus,
    val notes: List<NoteComponentData>,
    val selectedNoteTopic: NoteTopic,
    val totalNoteCount: Int,
    val nextCursor: Int?,
    val isNextPageLoading: Boolean,
    val songSummary: NoteSearchResultSongSummary,
    val errorMessage: String? = null,
) {
    companion object {
        fun initial() = NoteSearchResultViewState(
            status = NoteSearchResultStatus.INITIAL,
            notes = emptyList(),
            selectedNoteTopic = NoteTopic.ALL,
            totalNoteCount = 0,
            nextCursor = null,
            isNextPageLoading = false,
            songSummary = NoteSearchResultSongSummary(
                imageUrl = "https://i.scdn.co/image/ab67616d0000b2730b1e2a5d990c3e198effa85b",
                title = "시퍼런 봄",
                artistName = "쏜애플",
            ),
            errorMessage = null,
        )
    }
}
