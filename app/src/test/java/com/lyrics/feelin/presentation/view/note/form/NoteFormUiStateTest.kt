package com.lyrics.feelin.presentation.view.note.form

import com.lyrics.feelin.core.domain.enum.NoteTopic
import com.lyrics.feelin.presentation.view.component.music.MusicComponentData
import org.junit.Assert.assertEquals
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
        val state = NoteFormUiState.create().copy(
            selectedTopic = NoteTopic.FREE,
            selectedSong = sampleSong,
            isSongSectionVisible = true,
        )

        assertTrue(state.isSongDeleteVisible)
        assertTrue(state.isSongSelectable)
    }

    @Test
    fun `song section is hidden by default`() {
        assertFalse(NoteFormUiState.create().isSongSectionVisible)
    }

    @Test
    fun `edit sample shows song section`() {
        assertTrue(NoteFormUiState.editSample().isSongSectionVisible)
    }

    @Test
    fun `bottom song button is visible for free topic`() {
        val state = NoteFormUiState.create().copy(selectedTopic = NoteTopic.FREE)

        assertTrue(state.isBottomSongButtonVisible)
        assertTrue(state.isBottomSongButtonEnabled)
    }

    @Test
    fun `bottom song button is hidden for interpretation`() {
        val state = NoteFormUiState.create().copy(selectedTopic = NoteTopic.INTERPRETATION)

        assertFalse(state.isBottomSongButtonVisible)
        assertFalse(state.isBottomSongButtonEnabled)
    }

    @Test
    fun `bottom song button is hidden when no topic`() {
        val state = NoteFormUiState.create()

        assertFalse(state.isBottomSongButtonVisible)
        assertFalse(state.isBottomSongButtonEnabled)
    }

    @Test
    fun `bottom song button is disabled when song exists`() {
        val state = NoteFormUiState.create().copy(
            selectedTopic = NoteTopic.FREE,
            selectedSong = sampleSong,
            isSongSectionVisible = true,
        )

        assertTrue(state.isBottomSongButtonVisible)
        assertFalse(state.isBottomSongButtonEnabled)
    }

    @Test
    fun `song delete is hidden when section is not visible`() {
        val state = NoteFormUiState.create().copy(
            selectedTopic = NoteTopic.FREE,
            selectedSong = sampleSong,
        )

        assertFalse(state.isSongDeleteVisible)
    }

    @Test
    fun `song delete is hidden for interpretation`() {
        val state = NoteFormUiState.create().copy(
            selectedTopic = NoteTopic.INTERPRETATION,
            selectedSong = sampleSong,
            isSongSectionVisible = true,
        )

        assertFalse(state.isSongDeleteVisible)
    }

    @Test
    fun `body placeholder changes by topic`() {
        assertEquals(
            "해석을 공유해주세요.",
            NoteFormUiState.create().copy(selectedTopic = NoteTopic.INTERPRETATION).bodyPlaceholder,
        )
        assertEquals(
            "이야기를 남겨보세요.",
            NoteFormUiState.create().copy(selectedTopic = NoteTopic.FREE).bodyPlaceholder,
        )
        assertEquals(
            "질문을 남겨보세요.",
            NoteFormUiState.create().copy(selectedTopic = NoteTopic.QUESTION).bodyPlaceholder,
        )
        assertEquals(
            "생각을 남겨보세요.",
            NoteFormUiState.create().bodyPlaceholder,
        )
    }

    private companion object {
        val sampleSong = MusicComponentData.NoteWriteMusicExist(
            imageUrl = "https://picsum.photos/200",
            songName = "Realize",
            artistName = "실리카겔",
        )
    }
}
