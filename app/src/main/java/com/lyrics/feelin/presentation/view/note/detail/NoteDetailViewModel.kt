package com.lyrics.feelin.presentation.view.note.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyrics.feelin.core.domain.model.ProfileType
import com.lyrics.feelin.presentation.view.component.comment.CommentComponentData
import com.lyrics.feelin.presentation.view.component.comment.CommentWriterData
import com.lyrics.feelin.presentation.view.component.note.NoteComponentData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class NoteDetailViewModel : ViewModel() {

    private val _viewState = MutableStateFlow(NoteDetailViewState.initial())
    val viewState: StateFlow<NoteDetailViewState> = _viewState.asStateFlow()

    private var loadJob: Job? = null

    private var nextCommentId = INITIAL_NEXT_COMMENT_ID

    private val currentUser = CommentWriterData(
        nickname = "필릭스",
        profileCharacterType = ProfileType.SHORT_HAIR,
    )

    init {
        loadNoteDetail()
    }

    fun loadNoteDetail() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _viewState.value = NoteDetailViewState.loading()
            delay(LOAD_DELAY_MS)
            val comments = dummyComments()
            _viewState.value = NoteDetailViewState.success(
                note = NoteComponentData.sample().copy(commentsCount = comments.size),
                comments = comments,
            )
        }
    }

    fun writeComment(content: String) {
        val trimmedContent = content.trim()
        if (trimmedContent.isEmpty()) return

        val currentState = _viewState.value
        if (currentState.status != NoteDetailStatus.SUCCESS) return

        val newComment = CommentComponentData(
            id = nextCommentId++,
            content = trimmedContent,
            createdAt = LocalDateTime.parse(input = "2024-01-15T14:35:00"),
            writer = currentUser,
            isMine = true,
        )

        val comments = currentState.comments + newComment
        // API 연동 시에는 서버가 내려주는 total commentsCount와 댓글 목록 응답을 함께 병합한다.
        _viewState.value = currentState.copy(
            note = currentState.note.copy(commentsCount = comments.size),
            comments = comments,
        )
    }

    fun selectComment(comment: CommentComponentData) {
        val currentState = _viewState.value
        if (currentState.status != NoteDetailStatus.SUCCESS) return
        _viewState.value = currentState.copy(selectedComment = comment)
    }

    fun clearSelectedComment() {
        val currentState = _viewState.value
        if (currentState.status != NoteDetailStatus.SUCCESS) return
        _viewState.value = currentState.copy(selectedComment = null)
    }

    companion object {
        private const val LOAD_DELAY_MS = 500L
        private const val INITIAL_NEXT_COMMENT_ID = 100L

        private fun dummyComments(): List<CommentComponentData> {
            return listOf(
                CommentComponentData.sample(),
                CommentComponentData(
                    id = 2L,
                    content = "저도 이 노래 들을 때마다 이 가사가 제일 먼저 떠올라요.",
                    createdAt = LocalDateTime.parse(input = "2024-01-15T14:31:00"),
                    writer = CommentWriterData(
                        nickname = "자경단2호",
                        profileCharacterType = ProfileType.BRAIDED_HAIR,
                    ),
                    isMine = true,
                ),
                CommentComponentData(
                    id = 3L,
                    content = "실리카겔 노래 중에 Realize도 좋더라고요.",
                    createdAt = LocalDateTime.parse(input = "2024-01-15T14:32:00"),
                    writer = CommentWriterData(
                        nickname = "자경단3호",
                        profileCharacterType = ProfileType.PARTED_HAIR,
                    ),
                    isMine = false,
                ),
            )
        }
    }
}
