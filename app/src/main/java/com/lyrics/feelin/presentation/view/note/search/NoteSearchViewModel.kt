package com.lyrics.feelin.presentation.view.note.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyrics.feelin.presentation.view.component.music.MusicComponentData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteSearchViewModel : ViewModel() {

    private val _viewState = MutableStateFlow(NoteSearchViewState.initial())
    val viewState: StateFlow<NoteSearchViewState> = _viewState.asStateFlow()

    private var currentKeyword = ""
    private var loadingJob: Job? = null

    fun loadInitialNotes() {
        if (_viewState.value.screenStatus != NoteSearchScreenStatus.INITIAL) return
        resetAndLoad(showFullScreenLoading = true)
    }

    fun searchNotes(keyword: String) {
        currentKeyword = keyword
        resetAndLoad(showFullScreenLoading = true)
    }

    fun refresh() {
        if (_viewState.value.screenStatus == NoteSearchScreenStatus.LOADING) return
        resetAndLoad(showFullScreenLoading = false)
    }

    fun loadNextPage() {
        val currentState = _viewState.value
        if (
            currentState.screenStatus != NoteSearchScreenStatus.SUCCESS_LOAD ||
            currentState.listStatus != NoteSearchListStatus.SUCCESS_LOAD ||
            !currentState.hasNextPage
        ) {
            return
        }

        val currentResults = currentState.searchResults
        _viewState.value = currentState.copy(listStatus = NoteSearchListStatus.NEW_PAGE_LOADING)

        loadingJob = viewModelScope.launch {
            delay(LOAD_DELAY_MS)

            val filteredSongs = filteredDummySongs(currentKeyword)
            val nextPage = filteredSongs
                .drop(currentResults.size)
                .take(PAGE_SIZE)
            val updatedResults = currentResults + nextPage

            _viewState.value = NoteSearchViewState.success(
                searchResults = updatedResults,
                hasNextPage = filteredSongs.size > updatedResults.size,
            )
        }
    }

    /**
     * 노트 목록을 첫 페이지부터 다시 불러옵니다.
     *
     * - 전체 결과를 새로 그려야 하는 초기 진입/검색은 [showFullScreenLoading]을 `true`로 두고
     *   [NoteSearchScreenStatus.LOADING]으로 전환해 결과 영역 전체 인디케이터를 노출합니다.
     * - pull-to-refresh는 기존 결과를 유지한 채 [NoteSearchListStatus.REFRESHING]만 바꿔서
     *   상단 refresh indicator만 노출합니다.
     * - 새 요청이 들어오면 이전 로딩 job을 취소해 더 늦게 끝난 응답이 화면을 덮지 않도록 합니다.
     */
    private fun resetAndLoad(showFullScreenLoading: Boolean) {
        loadingJob?.cancel()

        val currentResults = if (showFullScreenLoading) emptyList() else _viewState.value.searchResults
        _viewState.value = if (showFullScreenLoading) {
            NoteSearchViewState.loading()
        } else {
            NoteSearchViewState.success(
                searchResults = currentResults,
                hasNextPage = _viewState.value.hasNextPage,
                listStatus = NoteSearchListStatus.REFRESHING,
            )
        }

        loadingJob = viewModelScope.launch {
            delay(LOAD_DELAY_MS)

            val filteredSongs = filteredDummySongs(currentKeyword)
            val firstPage = filteredSongs.take(PAGE_SIZE)

            _viewState.value = NoteSearchViewState.success(
                searchResults = firstPage,
                hasNextPage = filteredSongs.size > firstPage.size,
            )
        }
    }

    private fun filteredDummySongs(keyword: String): List<MusicComponentData.SearchNoteByMusic> {
        val normalizedKeyword = keyword.trim()
        if (normalizedKeyword.isBlank()) return dummySongs()

        return dummySongs().filter { song ->
            song.songName.contains(normalizedKeyword, ignoreCase = true) ||
                song.artistName.contains(normalizedKeyword, ignoreCase = true)
        }
    }

    companion object {
        private const val LOAD_DELAY_MS = 500L
        private const val PAGE_SIZE = 8

        @Suppress("MagicNumber")
        private fun dummySongs(): List<MusicComponentData.SearchNoteByMusic> {
            val songs = listOf(
                "Realize" to "실리카겔",
                "Tik Tak Tok" to "실리카겔",
                "NO PAIN" to "실리카겔",
                "네모의 꿈" to "자우림",
                "샴푸의 요정" to "빛과 소금",
                "TOMBOY" to "혁오",
                "긴 제목 테스트 긴 제목 테스트 긴 제목 테스트 " to "아티스트",
                "영원히 둘이서" to "잔나비",
                "뜨거운 여름밤은 가고 남은 건 볼품없지만" to "잔나비",
                "Square" to "백예린",
                "Hate You" to "백예린",
                "난춘" to "새소년",
                "긴 꿈" to "새소년",
                "청춘만화" to "이무진",
                "사건의 지평선" to "윤하",
                "위잉위잉" to "혁오",
                "나랑 아니면" to "검정치마",
                "Antifreeze" to "백예린",
                "Spit It Out" to "솔루션스",
                "Holiday" to "검정치마",
                "Vivaldi" to "페퍼톤스",
                "행운을 빌어요" to "페퍼톤스",
                "Sober" to "NELL",
                "기억을 걷는 시간" to "NELL",
            )

            return songs.mapIndexed { index, (songName, artistName) ->
                MusicComponentData.SearchNoteByMusic(
                    imageUrl = "https://picsum.photos/seed/note-search-$index/200/200",
                    songName = songName,
                    artistName = artistName,
                    noteCount = 999 - index,
                )
            }
        }
    }
}
