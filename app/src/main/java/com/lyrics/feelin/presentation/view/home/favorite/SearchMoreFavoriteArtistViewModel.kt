package com.lyrics.feelin.presentation.view.home.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyrics.feelin.presentation.view.onboarding.favoriteartist.FavoriteArtistData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchMoreFavoriteArtistViewModel @Inject constructor() : ViewModel() {

    private val allArtists = MutableStateFlow<List<FavoriteArtistData>>(emptyList())
    private val _searchQuery = MutableStateFlow("")

    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val debouncedSearchQuery = _searchQuery.debounce(SEARCH_DEBOUNCE_MS.milliseconds)

    val artists: StateFlow<List<FavoriteArtistData>> = combine(allArtists, debouncedSearchQuery) { artists, query ->
        if (query.isBlank()) {
            artists
        } else {
            artists.filter { it.name.contains(query, ignoreCase = true) }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = emptyList()
    )

    val isEmpty: StateFlow<Boolean> = combine(artists, debouncedSearchQuery) { filteredArtists, query ->
        query.isNotBlank() && filteredArtists.isEmpty()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = false
    )

    init {
        loadArtists()
    }

    private fun loadArtists() {
        allArtists.value = sampleArtists()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    companion object {
        private const val SEARCH_DEBOUNCE_MS = 300L
        private const val STOP_TIMEOUT_MILLIS = 5_000L

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
