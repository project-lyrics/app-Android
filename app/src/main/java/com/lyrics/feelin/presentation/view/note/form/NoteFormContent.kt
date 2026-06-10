package com.lyrics.feelin.presentation.view.note.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun NoteFormContent(
    uiState: NoteFormUiState,
    actions: NoteFormContentActions,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val lyricsActions = rememberLyricsSectionActions(actions)
    val bodyActions = rememberBodySectionActions(actions)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
    ) {
        NoteFormCategorySelector(
            selectedTopic = uiState.selectedTopic,
            onClick = actions.onCategorySheetOpen,
        )

        if (uiState.isSongSectionVisible) {
            NoteFormSongSection(
                selectedSong = uiState.selectedSong,
                isDeleteVisible = uiState.isSongDeleteVisible,
                isSongSelectable = uiState.isSongSelectable,
                onSongClick = actions.onSongClick,
                onSongDelete = actions.onSongDelete,
            )

            Spacer(modifier = Modifier.height(16.dp))

            NoteFormLyricsSection(
                lyrics = uiState.lyrics,
                lyricsBackground = uiState.lyricsBackground,
                isLyricsFocused = uiState.isLyricsFocused,
                selectedSong = uiState.selectedSong,
                actions = lyricsActions,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        NoteFormBodySection(
            body = uiState.body,
            bodyPlaceholder = uiState.bodyPlaceholder,
            isBottomSongButtonVisible = uiState.isBottomSongButtonVisible,
            isBottomSongButtonEnabled = uiState.isBottomSongButtonEnabled,
            actions = bodyActions,
        )
    }
}

@Composable
private fun rememberLyricsSectionActions(actions: NoteFormContentActions): LyricsSectionActions {
    return remember(actions) {
        LyricsSectionActions(
            onLyricsChange = actions.onLyricsChange,
            onLyricsBackgroundSheetOpen = actions.onLyricsBackgroundSheetOpen,
            onLyricsSearchSheetOpen = actions.onLyricsSearchSheetOpen,
            onLyricsFocusChange = actions.onLyricsFocusChange,
            onShowNoSongDialog = actions.onShowNoSongDialog,
        )
    }
}

@Composable
private fun rememberBodySectionActions(actions: NoteFormContentActions): BodySectionActions {
    return remember(actions) {
        BodySectionActions(
            onBodyChange = actions.onBodyChange,
            onShowSongSection = actions.onShowSongSection,
        )
    }
}
