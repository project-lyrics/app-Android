package com.lyrics.feelin.presentation.view.onboarding.favoriteartist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingFavoriteArtistViewModel : ViewModel() {

    private val _viewState = MutableStateFlow(OnboardingFavoriteArtistViewState.initial())
    val viewState: StateFlow<OnboardingFavoriteArtistViewState> = _viewState.asStateFlow()

    fun loadArtists() {
        viewModelScope.launch {
            _viewState.value = OnboardingFavoriteArtistViewState.loading()

            // TODO(@이대근): 실제 API 호출로 교체 2026.02.25.
            @Suppress("MagicNumber")
            delay(500L)

            _viewState.value = OnboardingFavoriteArtistViewState.success(sampleArtists())
        }
    }

    fun toggleArtistSelection(artistId: Int) {
        _viewState.update { state ->
            state.copy(
                artists = state.artists.map { artist ->
                    if (artist.id == artistId) artist.copy(isSelected = !artist.isSelected)
                    else artist
                }
            )
        }
    }

    companion object {
        @Suppress("MagicNumber")
        private fun sampleArtists(): List<FavoriteArtistData> {
            return listOf(
                FavoriteArtistData(
                    id = 1,
                    name = "검정치마",
                    imageUrl = "https://i.scdn.co/image/ab6761610000e5eb8609536d21beed6769d09d7f",
                ),
                FavoriteArtistData(
                    id = 2,
                    name = "검정치마",
                    imageUrl = "https://i.scdn.co/image/ab6761610000e5eb8609536d21beed6769d09d7f",
                ),
                FavoriteArtistData(
                    id = 3,
                    name = "검정치마",
                    imageUrl = "https://i.scdn.co/image/ab6761610000e5eb8609536d21beed6769d09d7f",
                ),
                FavoriteArtistData(
                    id = 4,
                    name = "검정치마",
                    imageUrl = "https://i.scdn.co/image/ab6761610000e5eb8609536d21beed6769d09d7f",
                ),
            )
        }
    }
}
