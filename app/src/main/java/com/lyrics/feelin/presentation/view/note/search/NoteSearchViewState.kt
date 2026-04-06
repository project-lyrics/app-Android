package com.lyrics.feelin.presentation.view.note.search

import com.lyrics.feelin.presentation.view.component.music.MusicComponentData

enum class NoteSearchScreenStatus {
    INITIAL,
    LOADING,
    SUCCESS_LOAD,
    ERROR,
}

enum class NoteSearchListStatus {
    INITIAL,
    REFRESHING,
    NEW_PAGE_LOADING,
    SUCCESS_LOAD,
    ERROR,
}

data class NoteSearchViewState(
    val screenStatus: NoteSearchScreenStatus,
    val listStatus: NoteSearchListStatus,
    val searchResults: List<MusicComponentData.SearchNoteByMusic>,
    val hasNextPage: Boolean,
    val errorMessage: String? = null,
) {
    companion object {
        fun initial(): NoteSearchViewState {
            return NoteSearchViewState(
                screenStatus = NoteSearchScreenStatus.INITIAL,
                listStatus = NoteSearchListStatus.INITIAL,
                searchResults = emptyList(),
                hasNextPage = false,
                errorMessage = null,
            )
        }

        fun loading(): NoteSearchViewState {
            return NoteSearchViewState(
                screenStatus = NoteSearchScreenStatus.LOADING,
                listStatus = NoteSearchListStatus.INITIAL,
                searchResults = emptyList(),
                hasNextPage = false,
                errorMessage = null,
            )
        }

        fun success(
            searchResults: List<MusicComponentData.SearchNoteByMusic>,
            hasNextPage: Boolean,
            listStatus: NoteSearchListStatus = NoteSearchListStatus.SUCCESS_LOAD,
        ): NoteSearchViewState {
            return NoteSearchViewState(
                screenStatus = NoteSearchScreenStatus.SUCCESS_LOAD,
                listStatus = listStatus,
                searchResults = searchResults,
                hasNextPage = hasNextPage,
                errorMessage = null,
            )
        }

        fun error(
            message: String,
            searchResults: List<MusicComponentData.SearchNoteByMusic> = emptyList(),
        ): NoteSearchViewState {
            return NoteSearchViewState(
                screenStatus = NoteSearchScreenStatus.ERROR,
                listStatus = NoteSearchListStatus.ERROR,
                searchResults = searchResults,
                hasNextPage = false,
                errorMessage = message,
            )
        }
    }
}
