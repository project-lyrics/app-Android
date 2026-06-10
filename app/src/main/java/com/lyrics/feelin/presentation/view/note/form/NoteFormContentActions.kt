package com.lyrics.feelin.presentation.view.note.form

import androidx.compose.runtime.Immutable

@Immutable
internal data class NoteFormContentActions(
    val onCategorySheetOpen: () -> Unit,
    val onSongClick: () -> Unit,
    val onSongDelete: () -> Unit,
    val onLyricsChange: (String) -> Unit,
    val onBodyChange: (String) -> Unit,
    val onShowSongSection: () -> Unit,
    val onLyricsBackgroundSheetOpen: () -> Unit,
    val onLyricsSearchSheetOpen: () -> Unit,
    val onLyricsFocusChange: (Boolean) -> Unit,
    val onShowNoSongDialog: () -> Unit,
)
