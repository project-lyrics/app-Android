package com.lyrics.feelin.presentation.view.note.form.searchsong

import androidx.compose.runtime.Immutable
import com.lyrics.feelin.presentation.view.component.music.MusicComponentData

@Immutable
data class SearchSongUiState(
    val searchQuery: String,
    val searchResults: List<MusicComponentData.SearchList>,
) {
    companion object {
        fun initial(): SearchSongUiState = SearchSongUiState(
            searchQuery = "",
            searchResults = emptyList(),
        )

        fun searchSample(): SearchSongUiState = SearchSongUiState(
            searchQuery = "실리카겔",
            searchResults = listOf(
                MusicComponentData.SearchList(
                    imageUrl = "https://picsum.photos/seed/song1/200/200",
                    songName = "Realize",
                    artistName = "실리카겔"
                ),
                MusicComponentData.SearchList(
                    imageUrl = "https://picsum.photos/seed/song2/200/200",
                    songName = "No Pain",
                    artistName = "실리카겔"
                ),
            )
        )

        fun errorSample(): SearchSongUiState = SearchSongUiState(
            searchQuery = "없는노래",
            searchResults = emptyList(),
        )
    }
}
