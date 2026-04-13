package com.lyrics.feelin.presentation.view.note.search.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyrics.feelin.core.domain.enum.NoteTopic
import com.lyrics.feelin.presentation.view.component.note.NoteComponentData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteSearchResultViewModel : ViewModel() {

    private val _viewState = MutableStateFlow(NoteSearchResultViewState.initial())
    val viewState: StateFlow<NoteSearchResultViewState> = _viewState.asStateFlow()
    private var loadJob: Job? = null

    init {
        loadInitial()
    }

    fun loadInitial() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _viewState.value = _viewState.value.copy(
                status = NoteSearchResultStatus.LOADING,
                notes = emptyList(),
                nextCursor = null,
                isNextPageLoading = false,
                errorMessage = null,
            )

            delay(LOAD_DELAY_MS)

            val filteredNotes = filteredDummyNotes(noteTopic = _viewState.value.selectedNoteTopic)

            if (filteredNotes.isEmpty()) {
                _viewState.value = _viewState.value.copy(
                    status = NoteSearchResultStatus.EMPTY,
                    notes = emptyList(),
                    totalNoteCount = 0,
                    nextCursor = null,
                    isNextPageLoading = false,
                )
                return@launch
            }

            val firstPageNotes = filteredNotes.take(PAGE_SIZE)

            _viewState.value = _viewState.value.copy(
                status = NoteSearchResultStatus.SUCCESS,
                notes = firstPageNotes,
                totalNoteCount = filteredNotes.size,
                nextCursor = nextCursorAfter(loadedCount = firstPageNotes.size, totalCount = filteredNotes.size),
            )
        }
    }

    fun loadNextPage() {
        val currentState = _viewState.value
        if (
            currentState.status != NoteSearchResultStatus.SUCCESS ||
            currentState.isNextPageLoading ||
            currentState.nextCursor == null
        ) {
            return
        }

        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _viewState.value = currentState.copy(isNextPageLoading = true)

            delay(LOAD_DELAY_MS)

            val filteredNotes = filteredDummyNotes(noteTopic = currentState.selectedNoteTopic)
            val nextPageNotes = filteredNotes
                .drop(currentState.nextCursor)
                .take(PAGE_SIZE)
            val updatedNotes = currentState.notes + nextPageNotes

            _viewState.value = currentState.copy(
                notes = updatedNotes,
                nextCursor = nextCursorAfter(
                    loadedCount = updatedNotes.size,
                    totalCount = filteredNotes.size,
                ),
                isNextPageLoading = false,
            )
        }
    }

    fun selectTopic(noteTopic: NoteTopic) {
        if (_viewState.value.selectedNoteTopic == noteTopic) return
        _viewState.value = _viewState.value.copy(selectedNoteTopic = noteTopic)
        loadInitial()
    }

    private fun filteredDummyNotes(noteTopic: NoteTopic): List<NoteComponentData> {
        return dummyNoteItems()
            .filter { noteTopic == NoteTopic.ALL || it.noteTopic == noteTopic }
            .map { it.note }
    }

    private fun nextCursorAfter(loadedCount: Int, totalCount: Int): Int? {
        // 서버 cursor 대신 현재는 다음에 가져올 시작 위치를 더미 정수로 사용한다.
        return loadedCount.takeIf { it < totalCount }
    }

    private data class TopicNoteItem(
        val noteTopic: NoteTopic,
        val note: NoteComponentData,
    )

    companion object {
        private const val LOAD_DELAY_MS = 500L
        private const val PAGE_SIZE = 10

        private fun dummyNoteItems(): List<TopicNoteItem> {
            return listOf(
                TopicNoteItem(noteTopic = NoteTopic.INTERPRETATION, note = NoteComponentData.sample()),
                TopicNoteItem(noteTopic = NoteTopic.FREE, note = NoteComponentData.sampleNoLyrics()),
                TopicNoteItem(noteTopic = NoteTopic.INTERPRETATION, note = NoteComponentData.sample()),
                TopicNoteItem(noteTopic = NoteTopic.FREE, note = NoteComponentData.sampleNoLyrics()),
                TopicNoteItem(noteTopic = NoteTopic.INTERPRETATION, note = NoteComponentData.sample()),
                TopicNoteItem(noteTopic = NoteTopic.FREE, note = NoteComponentData.sampleNoLyrics()),
                TopicNoteItem(noteTopic = NoteTopic.INTERPRETATION, note = NoteComponentData.sample()),
                TopicNoteItem(noteTopic = NoteTopic.FREE, note = NoteComponentData.sampleNoLyrics()),
                TopicNoteItem(noteTopic = NoteTopic.INTERPRETATION, note = NoteComponentData.sample()),
                TopicNoteItem(noteTopic = NoteTopic.FREE, note = NoteComponentData.sampleNoLyrics()),
                TopicNoteItem(noteTopic = NoteTopic.INTERPRETATION, note = NoteComponentData.sample()),
                TopicNoteItem(noteTopic = NoteTopic.FREE, note = NoteComponentData.sampleNoLyrics()),
                TopicNoteItem(noteTopic = NoteTopic.INTERPRETATION, note = NoteComponentData.sample()),
            )
        }
    }
}
