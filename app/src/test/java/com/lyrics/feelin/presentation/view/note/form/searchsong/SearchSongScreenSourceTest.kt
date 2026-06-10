package com.lyrics.feelin.presentation.view.note.form.searchsong

import java.io.File
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchSongScreenSourceTest {

    @Test
    fun `search song field connects clear button to text field state`() {
        val source = searchSongScreenSource()

        assertTrue(
            "곡 검색 FeelinSearchInputField는 clearText를 import해야 합니다.",
            source.contains("import androidx.compose.foundation.text.input.clearText"),
        )
        assertTrue(
            "곡 검색 FeelinSearchInputField의 x 버튼은 searchFieldState.clearText()에 연결되어야 합니다.",
            source.contains("onClearClick = { searchFieldState.clearText() }"),
        )
    }

    @Test
    fun `search song field keeps song search placeholder`() {
        val source = searchSongScreenSource()

        assertTrue(
            "곡 검색 FeelinSearchInputField는 곡 검색 placeholder를 유지해야 합니다.",
            source.contains("placeholder = \"곡 검색\""),
        )
    }

    private fun searchSongScreenSource(): String {
        return File(SEARCH_SONG_SCREEN_PATH).readText()
    }

    private companion object {
        const val SEARCH_SONG_SCREEN_PATH =
            "src/main/java/com/lyrics/feelin/presentation/view/note/form/searchsong/SearchSongScreen.kt"
    }
}
