package com.lyrics.feelin.presentation.view.note.form

import androidx.compose.runtime.Immutable
import com.lyrics.feelin.core.domain.enum.NoteTopic
import com.lyrics.feelin.presentation.view.component.music.MusicComponentData
import com.lyrics.feelin.presentation.view.component.note.LyricsBackground

const val NOTE_FORM_LYRICS_MAX_LENGTH = 50
const val NOTE_FORM_BODY_MAX_LENGTH = 1000

@Immutable
data class NoteFormOriginalData(
    val selectedTopic: NoteTopic?,
    val selectedSong: MusicComponentData.NoteWriteMusicExist?,
    val lyrics: String,
    val lyricsBackground: LyricsBackground,
    val body: String,
)

@Immutable
data class NoteFormUiState(
    val selectedTopic: NoteTopic?,
    val selectedSong: MusicComponentData.NoteWriteMusicExist?,
    val lyrics: String,
    val lyricsBackground: LyricsBackground,
    val body: String,
    val temporaryLyricsBackground: LyricsBackground,
    val isCategorySheetVisible: Boolean,
    val isLyricsBackgroundSheetVisible: Boolean,
    val isLyricsSearchSheetVisible: Boolean,
    val isEditMode: Boolean,
    val originalData: NoteFormOriginalData,
) {
    val isCompleteEnabled: Boolean
        get() {
            val hasRequiredFields = selectedTopic != null &&
                body.isNotBlank() &&
                (selectedTopic != NoteTopic.INTERPRETATION || selectedSong != null)

            if (!hasRequiredFields) return false
            if (!isEditMode) return true

            return currentData != originalData
        }

    val isSongDeleteVisible: Boolean
        get() = !isEditMode && selectedSong != null

    val isSongSelectable: Boolean
        get() = !isEditMode

    private val currentData: NoteFormOriginalData
        get() = NoteFormOriginalData(
            selectedTopic = selectedTopic,
            selectedSong = selectedSong,
            lyrics = lyrics,
            lyricsBackground = lyricsBackground,
            body = body,
        )

    companion object {
        fun create(): NoteFormUiState {
            return empty(isEditMode = false)
        }

        fun editSample(): NoteFormUiState {
            val song = MusicComponentData.NoteWriteMusicExist(
                imageUrl = "https://picsum.photos/seed/note-form-edit/200/200",
                songName = "Realize",
                artistName = "실리카겔",
            )
            val originalData = NoteFormOriginalData(
                selectedTopic = NoteTopic.INTERPRETATION,
                selectedSong = song,
                lyrics = "우리가 길을 헤메이는 시퍼런 봄의",
                lyricsBackground = LyricsBackground.MINT,
                body = "이 노래를 들으면 오래 남는 장면이 떠올라요.",
            )

            return NoteFormUiState(
                selectedTopic = originalData.selectedTopic,
                selectedSong = originalData.selectedSong,
                lyrics = originalData.lyrics,
                lyricsBackground = originalData.lyricsBackground,
                body = originalData.body,
                temporaryLyricsBackground = originalData.lyricsBackground,
                isCategorySheetVisible = false,
                isLyricsBackgroundSheetVisible = false,
                isLyricsSearchSheetVisible = false,
                isEditMode = true,
                originalData = originalData,
            )
        }

        private fun empty(isEditMode: Boolean): NoteFormUiState {
            val originalData = NoteFormOriginalData(
                selectedTopic = null,
                selectedSong = null,
                lyrics = "",
                lyricsBackground = LyricsBackground.DEFAULT,
                body = "",
            )

            return NoteFormUiState(
                selectedTopic = null,
                selectedSong = null,
                lyrics = "",
                lyricsBackground = LyricsBackground.DEFAULT,
                body = "",
                temporaryLyricsBackground = LyricsBackground.DEFAULT,
                isCategorySheetVisible = false,
                isLyricsBackgroundSheetVisible = false,
                isLyricsSearchSheetVisible = false,
                isEditMode = isEditMode,
                originalData = originalData,
            )
        }
    }
}
