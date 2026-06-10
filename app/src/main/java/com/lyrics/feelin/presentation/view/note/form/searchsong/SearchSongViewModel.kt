package com.lyrics.feelin.presentation.view.note.form.searchsong

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.lyrics.feelin.navigation.FeelinDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class SearchSongViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    @Suppress("UnusedPrivateProperty")
    private val artistId: Long? = savedStateHandle.get<Long>(FeelinDestination.NoteFormSearchSong.ArtistIdArgument)

    private val _viewState = MutableStateFlow(SearchSongUiState.initial())
    val viewState: StateFlow<SearchSongUiState> = _viewState.asStateFlow()

    fun updateSearchQuery(query: String) {
        // REST API 연동 전까지는 고정 검색어 기반 stub 응답을 사용합니다.
        if (query == "실리카겔") {
            _viewState.value = SearchSongUiState.searchSample()
        } else if (query == "없는노래") {
            _viewState.value = SearchSongUiState.errorSample()
        } else {
            _viewState.value = SearchSongUiState(
                searchQuery = query,
                searchResults = emptyList()
            )
        }
    }
}
