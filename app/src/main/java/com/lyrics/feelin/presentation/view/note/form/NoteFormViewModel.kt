package com.lyrics.feelin.presentation.view.note.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.lyrics.feelin.core.domain.enum.NoteTopic
import com.lyrics.feelin.navigation.FeelinDestination
import com.lyrics.feelin.presentation.view.component.music.MusicComponentData
import com.lyrics.feelin.presentation.view.component.note.LyricsBackground
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Suppress("TooManyFunctions")
@HiltViewModel
class NoteFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val noteId: Long? = savedStateHandle.get<Long>(FeelinDestination.NoteFormEdit.NoteIdArgument)

    private val _viewState = MutableStateFlow(
        if (noteId != null) {
            NoteFormUiState.editSample()
        } else {
            NoteFormUiState.create()
        }
    )
    val viewState: StateFlow<NoteFormUiState> = _viewState.asStateFlow()

    fun selectTopic(topic: NoteTopic) {
        update { state ->
            val isNowInterpretation = topic == NoteTopic.INTERPRETATION
            val wasInterpretation = state.selectedTopic == NoteTopic.INTERPRETATION

            // 기획상 카테고리 전환 시 곡이 있으면 유지하고, 없으면 곡 UI를 숨깁니다. (UI 피그마 댓글 참조)
            val shouldShowSongSection = when {
                isNowInterpretation -> true
                wasInterpretation && state.selectedSong != null -> true
                wasInterpretation && state.selectedSong == null -> false
                else -> state.isSongSectionVisible
            }

            state.copy(
                selectedTopic = topic,
                isSongSectionVisible = shouldShowSongSection,
                isCategorySheetVisible = false,
            )
        }
    }

    fun setSong(song: MusicComponentData.NoteWriteMusicExist) {
        update { it.copy(selectedSong = song) }
    }

    fun deleteSong() {
        update {
            if (it.isEditMode) {
                // 정책상 기존 노트 수정 시에는 첨부했던 곡을 변경하거나 삭제할 수 없습니다.
                it
            } else {
                it.copy(
                    selectedSong = null,
                    lyrics = "",
                    lyricsBackground = LyricsBackground.DEFAULT,
                    temporaryLyricsBackground = LyricsBackground.DEFAULT,
                    isSongSectionVisible = false,
                )
            }
        }
    }

    fun showSongSection() {
        update { it.copy(isSongSectionVisible = true) }
    }

    fun showNoSongDialog() {
        update { it.copy(isNoSongDialogVisible = true) }
    }

    fun hideNoSongDialog() {
        update { it.copy(isNoSongDialogVisible = false) }
    }

    fun setLyricsFocus(isFocused: Boolean) {
        update { it.copy(isLyricsFocused = isFocused) }
    }

    fun setLyrics(lyrics: String) {
        update { it.copy(lyrics = lyrics.take(NOTE_FORM_LYRICS_MAX_LENGTH)) }
    }

    fun setBody(body: String) {
        update { it.copy(body = body.take(NOTE_FORM_BODY_MAX_LENGTH)) }
    }

    fun selectTemporaryBackground(background: LyricsBackground) {
        update { it.copy(temporaryLyricsBackground = background) }
    }

    fun confirmBackground() {
        update {
            it.copy(
                lyricsBackground = it.temporaryLyricsBackground,
                isLyricsBackgroundSheetVisible = false,
            )
        }
    }

    fun openCategorySheet() {
        update { it.copy(isCategorySheetVisible = true) }
    }

    fun closeCategorySheet() {
        update { it.copy(isCategorySheetVisible = false) }
    }

    fun openLyricsBackgroundSheet() {
        update {
            it.copy(
                temporaryLyricsBackground = it.lyricsBackground,
                isLyricsBackgroundSheetVisible = true,
            )
        }
    }

    fun closeLyricsBackgroundSheet() {
        update {
            it.copy(
                temporaryLyricsBackground = it.lyricsBackground,
                isLyricsBackgroundSheetVisible = false,
            )
        }
    }

    fun openLyricsSearchSheet() {
        update { it.copy(isLyricsSearchSheetVisible = true) }
    }

    fun closeLyricsSearchSheet() {
        update { it.copy(isLyricsSearchSheetVisible = false) }
    }

    private fun update(reducer: (NoteFormUiState) -> NoteFormUiState) {
        _viewState.value = reducer(_viewState.value)
    }
}
