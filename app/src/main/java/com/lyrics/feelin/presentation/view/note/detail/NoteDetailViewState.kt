package com.lyrics.feelin.presentation.view.note.detail

import com.lyrics.feelin.presentation.view.component.comment.CommentComponentData
import com.lyrics.feelin.presentation.view.component.note.NoteComponentData

enum class NoteDetailStatus {
    INITIAL,
    LOADING,
    SUCCESS,
    ERROR,
}

data class NoteDetailViewState(
    val status: NoteDetailStatus,
    val note: NoteComponentData,
    val comments: List<CommentComponentData>,
    val selectedComment: CommentComponentData?,
    val errorMessage: String? = null,
) {
    companion object {
        fun initial(): NoteDetailViewState {
            return NoteDetailViewState(
                status = NoteDetailStatus.INITIAL,
                note = NoteComponentData.empty(),
                comments = emptyList(),
                selectedComment = null,
                errorMessage = null,
            )
        }

        fun loading(): NoteDetailViewState {
            return NoteDetailViewState(
                status = NoteDetailStatus.LOADING,
                note = NoteComponentData.empty(),
                comments = emptyList(),
                selectedComment = null,
                errorMessage = null,
            )
        }

        fun success(
            note: NoteComponentData,
            comments: List<CommentComponentData>,
            selectedComment: CommentComponentData? = null,
        ): NoteDetailViewState {
            return NoteDetailViewState(
                status = NoteDetailStatus.SUCCESS,
                note = note,
                comments = comments,
                selectedComment = selectedComment,
                errorMessage = null,
            )
        }

        fun error(message: String): NoteDetailViewState {
            return NoteDetailViewState(
                status = NoteDetailStatus.ERROR,
                note = NoteComponentData.empty(),
                comments = emptyList(),
                selectedComment = null,
                errorMessage = message,
            )
        }
    }
}
