package com.lyrics.feelin.presentation.view.note.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.component.FeelinSearchInputField
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithBack
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.component.music.MusicComponent
import com.lyrics.feelin.presentation.view.component.music.MusicComponentData

@Composable
fun NoteSearchScreen(modifier: Modifier = Modifier) {
    val searchState = rememberTextFieldState()

    val feelinColors = LocalFeelinColors.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val dummySongData by remember {
        mutableStateOf(
            @Suppress("MagicNumber") // TODO(@이대근): 더미데이터 삭제시 삭제 2026.04.06.
            List(12) { index ->
                MusicComponentData.SearchNoteByMusic(
                    imageUrl = "https://picsum.photos/200",
                    songName = "Realize",
                    artistName = "실리카겔",
                    noteCount = 999 - index,
                )
            }
        )
    }

    Scaffold(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
            .background(color = feelinColors.backgroundPrimary),
        topBar = {
            FeelinTopAppBarWithBack(
                title = "노트 검색",
                showDivider = false,
                onBackClick = { /* TODO: 이전화면 라우팅*/ }
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .background(feelinColors.backgroundPrimary)
                .padding(contentPadding)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            FeelinSearchInputField(
                state = searchState,
                placeholder = "곡 검색",
                onSearchClick = { keyboardController?.hide() },
                onClearClick = { searchState.clearText() },
                modifier = Modifier.padding(bottom = 16.dp)
            )
            // TODO: 상단 스크롤로 새로고침
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(
                    items = dummySongData,
                    key = { it.hashCode() }
                ) { song ->
                    MusicComponent(song)
                }
                // TODO: 마지막 요소 도달하면 페이지네이션
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NoteSearchScreenPreview() {
    FeelinTheme {
        NoteSearchScreen()
    }
}
