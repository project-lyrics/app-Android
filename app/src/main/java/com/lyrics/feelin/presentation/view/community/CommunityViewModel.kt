package com.lyrics.feelin.presentation.view.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CommunityViewModel : ViewModel() {

    private val _viewState = MutableStateFlow(CommunityViewState.initial())
    val viewState: StateFlow<CommunityViewState> = _viewState.asStateFlow()

    fun loadCommunityData() {
        viewModelScope.launch {
            _viewState.value = CommunityViewState.loading()

            // TODO: 실제로는 repository에서 데이터를 가져오는 로직
            @Suppress("MagicNumber")
            delay(500L)

            _viewState.value = CommunityViewState.success()
        }
    }

    fun toggleNoteOnlyLyrics() {
        _viewState.value = _viewState.value.copy(
            isViewNoteOnlyLyrics = !_viewState.value.isViewNoteOnlyLyrics
        )
    }

    fun toggleArtistLike() {
        val currentArtist = _viewState.value.artist
        _viewState.value = _viewState.value.copy(
            artist = currentArtist.copy(isLike = !currentArtist.isLike)
        )
    }
}
