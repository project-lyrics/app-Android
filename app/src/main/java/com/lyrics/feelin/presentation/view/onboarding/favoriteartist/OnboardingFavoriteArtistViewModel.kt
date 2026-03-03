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

    /** 검색 필터링 전 전체 아티스트 목록 (선택 상태 포함) */
    private var allArtists: List<FavoriteArtistData> = emptyList()

    fun loadArtists() {
        viewModelScope.launch {
            _viewState.value = OnboardingFavoriteArtistViewState.loading()

            // TODO(@이대근): 실제 API 호출로 교체 2026.02.25.
            @Suppress("MagicNumber")
            delay(500L)

            val artists = sampleArtists()
            allArtists = artists
            _viewState.value = OnboardingFavoriteArtistViewState.success(artists)
        }
    }

    fun toggleArtistSelection(artistId: Int): Boolean {
        val currentArtist = allArtists.find { it.id == artistId } ?: return false

        // 선택 해제는 항상 허용, 선택 시도일 때만 최대값 체크
        if (!currentArtist.isSelected && allArtists.count { it.isSelected } >= MAX_FAVORITE_ARTISTS) {
            return true
        }

        // 전체 목록의 선택 상태 업데이트
        allArtists = allArtists.map { artist ->
            if (artist.id == artistId) {
                artist.copy(isSelected = !artist.isSelected)
            } else {
                artist
            }
        }

        // 현재 표시 목록도 동기화
        _viewState.update { state ->
            state.copy(
                artists = state.artists.map { artist ->
                    if (artist.id == artistId) {
                        artist.copy(isSelected = !artist.isSelected)
                    } else {
                        artist
                    }
                }
            )
        }
        return false
    }

    fun searchArtists(keyword: String) {
        val filtered = if (keyword.isBlank()) {
            allArtists
        } else {
            allArtists.filter { it.name.contains(keyword, ignoreCase = true) }
        }
        _viewState.update { state ->
            state.copy(artists = filtered)
        }
    }

    companion object {
        private const val MAX_FAVORITE_ARTISTS = 30

        private fun sampleArtists(): List<FavoriteArtistData> {
            val names = listOf(
                "검정치마", "잔나비", "혁오", "새소년", "카더가든", "실리카겔",
                "백예린", "쏜애플", "자우림", "페퍼톤스", "브로콜리너마저",
                "짙은", "넬", "선우정아", "기프트", "치즈", "스탠딩 에그",
                "옥상달빛", "나상현씨밴드", "루시", "SURL", "적재",
                "데이먼스 이어", "ADOY", "오존", "10CM", "우효",
                "마치", "이승윤", "신해경", "오지은", "디어클라우드",
                "소란", "김뜻돌"
            )
            return names.mapIndexed { index, name ->
                FavoriteArtistData(
                    id = index + 1,
                    name = name,
                    imageUrl = "https://i.scdn.co/image/ab6761610000e5eb8609536d21beed6769d09d7f",
                )
            }
        }
    }
}
