package com.lyrics.feelin.presentation.view.note.form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarDefaults
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithClose
import com.lyrics.feelin.core.domain.enum.NoteTopic
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.component.music.MusicComponentData
import com.lyrics.feelin.presentation.view.component.note.LyricsBackground

@Composable
fun NoteFormRoute(
    onCloseClick: () -> Unit,
    onNavigateToSearchSong: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NoteFormViewModel = hiltViewModel(),
) {
    val uiState by viewModel.viewState.collectAsState()

    NoteFormScreen(
        uiState = uiState,
        onCloseClick = onCloseClick,
        onCompleteClick = onCloseClick, // UI 구현만 수행할때는 화면을 닫는 동작만 수행합니다
        onCategorySelect = viewModel::selectTopic,
        onSongClick = onNavigateToSearchSong,
        onSongDelete = viewModel::requestDeleteSong,
        onLyricsChange = viewModel::setLyrics,
        onBodyChange = viewModel::setBody,
        onCategorySheetOpen = viewModel::openCategorySheet,
        onCategorySheetClose = viewModel::closeCategorySheet,
        onLyricsBackgroundSheetOpen = viewModel::openLyricsBackgroundSheet,
        onLyricsBackgroundSheetClose = viewModel::closeLyricsBackgroundSheet,
        onTemporaryBackgroundSelect = viewModel::selectTemporaryBackground,
        onBackgroundConfirm = viewModel::confirmBackground,
        onLyricsSearchSheetOpen = viewModel::openLyricsSearchSheet,
        onLyricsSearchSheetClose = viewModel::closeLyricsSearchSheet,
        onShowSongSection = viewModel::showSongSection,
        onLyricsFocusChange = viewModel::setLyricsFocus,
        onShowNoSongDialog = viewModel::showNoSongDialog,
        onHideNoSongDialog = viewModel::hideNoSongDialog,
        onConfirmSongDelete = viewModel::confirmDeleteSong,
        onHideSongDeleteDialog = viewModel::hideSongDeleteDialog,
        modifier = modifier,
    )
}

@Composable
fun NoteFormScreen(
    uiState: NoteFormUiState,
    onCloseClick: () -> Unit,
    onCompleteClick: () -> Unit,
    onCategorySelect: (NoteTopic) -> Unit,
    onSongClick: () -> Unit,
    onSongDelete: () -> Unit,
    onLyricsChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onCategorySheetOpen: () -> Unit,
    onCategorySheetClose: () -> Unit,
    onLyricsBackgroundSheetOpen: () -> Unit,
    onLyricsBackgroundSheetClose: () -> Unit,
    onTemporaryBackgroundSelect: (LyricsBackground) -> Unit,
    onBackgroundConfirm: () -> Unit,
    onLyricsSearchSheetOpen: () -> Unit,
    onLyricsSearchSheetClose: () -> Unit,
    onShowSongSection: () -> Unit,
    onLyricsFocusChange: (Boolean) -> Unit,
    onShowNoSongDialog: () -> Unit,
    onHideNoSongDialog: () -> Unit,
    onConfirmSongDelete: () -> Unit,
    onHideSongDeleteDialog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalFeelinColors.current
    val isCompleteEnabled = uiState.isCompleteEnabled

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.backgroundPrimary)
            .statusBarsPadding()
    ) {
        FeelinTopAppBarWithClose(
            title = if (uiState.isEditMode) "노트 수정" else "노트 작성",
            onCloseClick = onCloseClick,
            actions = {
                Text(
                    text = "완료",
                    style = FeelinTypography.body1,
                    color = if (isCompleteEnabled) colors.systemActivate else colors.systemDisable,
                    modifier = Modifier.clickable(
                        enabled = isCompleteEnabled,
                        onClick = onCompleteClick,
                    ),
                )
            },
            paddingValues = PaddingValues(
                horizontal = FeelinTopAppBarDefaults.HorizontalPadding,
                vertical = 10.dp,
            ),
            modifier = Modifier,
            showDivider = false,
        )

        NoteFormContent(
            uiState = uiState,
            actions = NoteFormContentActions(
                onCategorySheetOpen = onCategorySheetOpen,
                onSongClick = onSongClick,
                onSongDelete = onSongDelete,
                onLyricsChange = onLyricsChange,
                onBodyChange = onBodyChange,
                onShowSongSection = onShowSongSection,
                onLyricsBackgroundSheetOpen = onLyricsBackgroundSheetOpen,
                onLyricsSearchSheetOpen = onLyricsSearchSheetOpen,
                onLyricsFocusChange = onLyricsFocusChange,
                onShowNoSongDialog = onShowNoSongDialog,
            ),
            modifier = Modifier.weight(1f),
        )
    }

    NoteFormCategorySheet(
        isVisible = uiState.isCategorySheetVisible,
        onDismissRequest = onCategorySheetClose,
        onCategorySelect = onCategorySelect,
    )

    NoteFormLyricsBackgroundSheet(
        isVisible = uiState.isLyricsBackgroundSheetVisible,
        selectedBackground = uiState.temporaryLyricsBackground,
        onDismissRequest = onLyricsBackgroundSheetClose,
        onTemporaryBackgroundSelect = onTemporaryBackgroundSelect,
        onBackgroundConfirm = onBackgroundConfirm,
    )

    NoteFormLyricsSearchSheet(
        isVisible = uiState.isLyricsSearchSheetVisible,
        onDismissRequest = onLyricsSearchSheetClose,
    )

    NoteFormNoSongDialog(
        isVisible = uiState.isNoSongDialogVisible,
        onConfirmClick = onHideNoSongDialog,
    )

    NoteFormSongDeleteDialog(
        isVisible = uiState.isSongDeleteDialogVisible,
        onConfirmClick = onConfirmSongDelete,
        onDismissClick = onHideSongDeleteDialog,
    )
}

@Preview(showBackground = true)
@Composable
private fun NoteFormScreenCreatePreview() {
    NoteFormPreviewContent(uiState = NoteFormUiState.create())
}

@Preview(showBackground = true)
@Composable
private fun NoteFormScreenFreeWithSongPreview() {
    NoteFormPreviewContent(
        uiState = NoteFormUiState.create().copy(
            selectedTopic = NoteTopic.FREE,
            selectedSong = MusicComponentData.NoteWriteMusicExist(
                imageUrl = "https://picsum.photos/200",
                songName = "No Pain",
                artistName = "실리카겔",
            ),
            isSongSectionVisible = true,
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun NoteFormScreenFreeWithoutSongPreview() {
    NoteFormPreviewContent(
        uiState = NoteFormUiState.create().copy(
            selectedTopic = NoteTopic.FREE,
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun NoteFormScreenEditPreview() {
    NoteFormPreviewContent(uiState = NoteFormUiState.editSample())
}

@Composable
private fun NoteFormPreviewContent(uiState: NoteFormUiState) {
    FeelinTheme {
        NoteFormScreen(
            uiState = uiState,
            onCloseClick = {},
            onCompleteClick = {},
            onCategorySelect = {},
            onSongClick = {},
            onSongDelete = {},
            onLyricsChange = {},
            onBodyChange = {},
            onCategorySheetOpen = {},
            onCategorySheetClose = {},
            onLyricsBackgroundSheetOpen = {},
            onLyricsBackgroundSheetClose = {},
            onTemporaryBackgroundSelect = {},
            onBackgroundConfirm = {},
            onLyricsSearchSheetOpen = {},
            onLyricsSearchSheetClose = {},
            onShowSongSection = {},
            onLyricsFocusChange = {},
            onShowNoSongDialog = {},
            onHideNoSongDialog = {},
            onConfirmSongDelete = {},
            onHideSongDeleteDialog = {},
        )
    }
}
