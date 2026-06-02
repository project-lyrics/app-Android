package com.lyrics.feelin.presentation.view.note.form

import androidx.lifecycle.SavedStateHandle
import com.lyrics.feelin.core.domain.enum.NoteTopic
import com.lyrics.feelin.navigation.FeelinDestination
import com.lyrics.feelin.presentation.view.component.music.MusicComponentData
import com.lyrics.feelin.presentation.view.component.note.LyricsBackground
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NoteFormViewModelTest {

    @Test
    fun `select topic resets song lyrics background and body`() {
        val viewModel = createViewModel()
        viewModel.setSong(sampleSong)
        viewModel.setLyrics("가사")
        viewModel.selectTemporaryBackground(LyricsBackground.BLACK)
        viewModel.confirmBackground()
        viewModel.setBody("본문")

        viewModel.selectTopic(NoteTopic.FREE)

        val state = viewModel.viewState.value
        assertEquals(NoteTopic.FREE, state.selectedTopic)
        assertNull(state.selectedSong)
        assertEquals("", state.lyrics)
        assertEquals(LyricsBackground.DEFAULT, state.lyricsBackground)
        assertEquals("", state.body)
    }

    @Test
    fun `delete song in create mode resets song lyrics and background`() {
        val viewModel = createViewModel()
        viewModel.setSong(sampleSong)
        viewModel.setLyrics("가사")
        viewModel.selectTemporaryBackground(LyricsBackground.RED)
        viewModel.confirmBackground()

        viewModel.deleteSong()

        val state = viewModel.viewState.value
        assertNull(state.selectedSong)
        assertEquals("", state.lyrics)
        assertEquals(LyricsBackground.DEFAULT, state.lyricsBackground)
    }

    @Test
    fun `delete song in edit mode keeps original song`() {
        val viewModel = createViewModel(noteId = 1L)
        val originalSong = viewModel.viewState.value.selectedSong

        viewModel.deleteSong()

        assertEquals(originalSong, viewModel.viewState.value.selectedSong)
    }

    @Test
    fun `lyrics and body input are trimmed to max length`() {
        val viewModel = createViewModel()

        viewModel.setLyrics("가".repeat(NOTE_FORM_LYRICS_MAX_LENGTH + 1))
        viewModel.setBody("나".repeat(NOTE_FORM_BODY_MAX_LENGTH + 1))

        val state = viewModel.viewState.value
        assertEquals(NOTE_FORM_LYRICS_MAX_LENGTH, state.lyrics.length)
        assertEquals(NOTE_FORM_BODY_MAX_LENGTH, state.body.length)
    }

    @Test
    fun `background selection applies only after confirm`() {
        val viewModel = createViewModel()

        viewModel.selectTemporaryBackground(LyricsBackground.RAINBOW)
        assertEquals(LyricsBackground.DEFAULT, viewModel.viewState.value.lyricsBackground)

        viewModel.confirmBackground()
        assertEquals(LyricsBackground.RAINBOW, viewModel.viewState.value.lyricsBackground)
    }

    private fun createViewModel(noteId: Long? = null): NoteFormViewModel {
        val savedStateHandle = SavedStateHandle(
            noteId?.let { mapOf(FeelinDestination.NoteFormEdit.NoteIdArgument to it) } ?: emptyMap()
        )
        return NoteFormViewModel(savedStateHandle)
    }

    private companion object {
        val sampleSong = MusicComponentData.NoteWriteMusicExist(
            imageUrl = "https://picsum.photos/200",
            songName = "Realize",
            artistName = "실리카겔",
        )
    }
}
