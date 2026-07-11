package com.lyrics.feelin.presentation.view.component.note

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lyrics.feelin.core.designsystem.component.FeelinModalBottomSheet
import com.lyrics.feelin.core.designsystem.component.FeelinModalBottomSheetAction
import com.lyrics.feelin.core.designsystem.icon.DeleteIcon
import com.lyrics.feelin.core.designsystem.icon.ModifyIcon
import com.lyrics.feelin.core.designsystem.icon.ProhibitIcon
import com.lyrics.feelin.core.designsystem.icon.ReportIcon
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteMenuBottomSheet(
    noteData: NoteComponentData,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    currentUserId: Long? = null,
    onReportClick: (Long) -> Unit = {},
    onEditClick: (Long) -> Unit = {},
    onDeleteClick: (Long) -> Unit = {},
    onBlockClick: (Long) -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isMyNote = currentUserId != null && currentUserId == noteData.publisher.id

    FeelinModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        if (isMyNote) {
            FeelinModalBottomSheetAction(
                text = "수정하기",
                icon = ModifyIcon,
                onClick = { onEditClick(noteData.id) },
            )
            FeelinModalBottomSheetAction(
                text = "삭제하기",
                icon = DeleteIcon,
                onClick = { onDeleteClick(noteData.id) },
            )
        } else {
            FeelinModalBottomSheetAction(
                text = "신고하기",
                icon = ReportIcon,
                onClick = { onReportClick(noteData.id) },
            )
            FeelinModalBottomSheetAction(
                text = "차단하기",
                icon = ProhibitIcon,
                onClick = { onBlockClick(noteData.publisher.id) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteMenuBottomSheetOtherPreview() {
    FeelinTheme {
        NoteMenuBottomSheet(
            noteData = NoteComponentData.sample(),
            onDismissRequest = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteMenuBottomSheetMinePreview() {
    FeelinTheme {
        NoteMenuBottomSheet(
            noteData = NoteComponentData.sample(),
            currentUserId = NoteComponentData.sample().publisher.id,
            onDismissRequest = {},
        )
    }
}
