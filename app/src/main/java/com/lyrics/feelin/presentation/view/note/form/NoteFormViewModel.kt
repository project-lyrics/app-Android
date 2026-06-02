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
        update {
            it.copy(
                selectedTopic = topic,
                selectedSong = null,
                lyrics = "",
                lyricsBackground = LyricsBackground.DEFAULT,
                body = "",
                temporaryLyricsBackground = LyricsBackground.DEFAULT,
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
                it
            } else {
                it.copy(
                    selectedSong = null,
                    lyrics = "",
                    lyricsBackground = LyricsBackground.DEFAULT,
                    temporaryLyricsBackground = LyricsBackground.DEFAULT,
                )
            }
        }
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
