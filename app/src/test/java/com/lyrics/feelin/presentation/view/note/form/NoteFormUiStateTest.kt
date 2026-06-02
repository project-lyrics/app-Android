package com.lyrics.feelin.presentation.view.note.form

import com.lyrics.feelin.core.domain.enum.NoteTopic
import com.lyrics.feelin.presentation.view.component.music.MusicComponentData
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NoteFormUiStateTest {

    @Test
    fun `interpretation topic requires song for completion`() {
        val state = NoteFormUiState.create()
            .copy(selectedTopic = NoteTopic.INTERPRETATION, body = "본문")

        assertFalse(state.isCompleteEnabled)
    }

    @Test
    fun `free topic does not require song for completion`() {
        val state = NoteFormUiState.create()
            .copy(selectedTopic = NoteTopic.FREE, body = "본문")

        assertTrue(state.isCompleteEnabled)
    }

    @Test
    fun `edit completion requires a change from original values`() {
        val state = NoteFormUiState.editSample()

        assertFalse(state.isCompleteEnabled)
        assertTrue(state.copy(body = "수정된 본문").isCompleteEnabled)
    }

    @Test
    fun `edit mode disables song delete and song selection`() {
        val state = NoteFormUiState.editSample()

        assertFalse(state.isSongDeleteVisible)
        assertFalse(state.isSongSelectable)
    }

    @Test
    fun `create mode enables song delete only when song exists`() {
        val state = NoteFormUiState.create().copy(selectedSong = sampleSong)

        assertTrue(state.isSongDeleteVisible)
        assertTrue(state.isSongSelectable)
    }

    private companion object {
        val sampleSong = MusicComponentData.NoteWriteMusicExist(
            imageUrl = "https://picsum.photos/200",
            songName = "Realize",
            artistName = "실리카겔",
        )
    }
}
