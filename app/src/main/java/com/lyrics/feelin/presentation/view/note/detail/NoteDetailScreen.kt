package com.lyrics.feelin.presentation.view.note.detail

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithBack
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.component.comment.CommentComponent
import com.lyrics.feelin.presentation.view.component.comment.CommentComponentData
import com.lyrics.feelin.presentation.view.component.comment.CommentInputField
import com.lyrics.feelin.presentation.view.component.note.NoteComponent
import com.lyrics.feelin.presentation.view.component.note.NoteComponentData

@Composable
fun NoteDetailScreen(
    noteId: Long,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NoteDetailViewModel = viewModel(),
) {
    val viewState by viewModel.viewState.collectAsState()
    val commentInputState = remember { TextFieldState() }

    LaunchedEffect(noteId) {
        viewModel.loadNoteDetail(noteId)
    }

    NoteDetailContent(
        viewState = viewState,
        commentInputState = commentInputState,
        onBackClick = onBackClick,
        onCommentMoreClick = viewModel::selectComment,
        onSendComment = viewModel::writeComment,
        modifier = modifier,
    )
}

@Composable
private fun NoteDetailContent(
    viewState: NoteDetailViewState,
    commentInputState: TextFieldState,
    onBackClick: () -> Unit,
    onCommentMoreClick: (CommentComponentData) -> Unit,
    onSendComment: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val feelinColors = LocalFeelinColors.current

    Scaffold(
        modifier = modifier
            .background(color = feelinColors.backgroundPrimary)
            .imePadding()
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
            .fillMaxSize(),
        topBar = {
            FeelinTopAppBarWithBack(
                title = "노트",
                onBackClick = onBackClick,
                centeredTitle = true,
                showDivider = false,
            )
        },
        bottomBar = {
            if (viewState.status == NoteDetailStatus.SUCCESS) {
                CommentInputField(
                    state = commentInputState,
                    onSendClick = onSendComment,
                    modifier = Modifier
                        .background(feelinColors.backgroundPrimary)
                        .navigationBarsPadding(),
                )
            }
        },
        containerColor = feelinColors.backgroundPrimary,
        contentWindowInsets = WindowInsets(0),
    ) { innerPadding ->
        when (viewState.status) {
            NoteDetailStatus.INITIAL,
            NoteDetailStatus.LOADING -> NoteDetailLoadingContent(innerPadding = innerPadding)
            NoteDetailStatus.SUCCESS -> NoteDetailSuccessContent(
                viewState = viewState,
                onCommentMoreClick = onCommentMoreClick,
                innerPadding = innerPadding,
            )
            NoteDetailStatus.ERROR -> NoteDetailErrorContent(
                errorMessage = viewState.errorMessage,
                innerPadding = innerPadding,
            )
        }
    }
}

@Composable
private fun NoteDetailLoadingContent(innerPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun NoteDetailSuccessContent(
    viewState: NoteDetailViewState,
    onCommentMoreClick: (CommentComponentData) -> Unit,
    innerPadding: PaddingValues,
) {
    val feelinColors = LocalFeelinColors.current
    val comments = viewState.comments
    val hasComments = comments.isNotEmpty()

    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(feelinColors.backgroundPrimary),
    ) {
        item {
            NoteComponent(noteData = viewState.note)
            if (hasComments) {
                HorizontalDivider(color = feelinColors.backgroundTertiary, thickness = 8.dp)
            }
        }

        if (hasComments) {
            item {
                CommentSectionHeader(commentCount = comments.size)
            }

            items(items = comments, key = { it.id }) { comment ->
                CommentComponent(
                    commentData = comment,
                    onMoreClick = onCommentMoreClick,
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun CommentSectionHeader(commentCount: Int, modifier: Modifier = Modifier) {
    val feelinColors = LocalFeelinColors.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = FeelinTypography.title2.toSpanStyle().copy(color = feelinColors.gray09)
                ) {
                    append("댓글")
                }
                withStyle(
                    style = FeelinTypography.body2.toSpanStyle().copy(
                        color = feelinColors.brandPrimary
                    )
                ) {
                    append(" $commentCount")
                }
            }
        )
    }
}

@Composable
private fun NoteDetailErrorContent(errorMessage: String?, innerPadding: PaddingValues) {
    val feelinColors = LocalFeelinColors.current

    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = errorMessage ?: "오류가 발생했습니다.",
            style = FeelinTypography.body1.copy(color = feelinColors.gray04),
        )
    }
}

@Preview(name = "NoteDetailScreen Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Preview(
    name = "NoteDetailScreen Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF0C0C0D,
)
@Composable
private fun NoteDetailScreenPreview() {
    FeelinTheme {
        NoteDetailContent(
            viewState = NoteDetailViewState.success(
                note = NoteComponentData.sample(),
                comments = listOf(
                    CommentComponentData.sample(id = 1L),
                    CommentComponentData.sample(id = 2L, isMine = true),
                ),
            ),
            commentInputState = remember { TextFieldState() },
            onBackClick = {},
            onCommentMoreClick = {},
            onSendComment = {},
        )
    }
}

@Preview(name = "NoteDetailNoCommentScreen Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Preview(
    name = "NoteDetailNoCommentScreen Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF0C0C0D,
)
@Composable
private fun NoteDetailNoCommentScreenPreview() {
    FeelinTheme {
        NoteDetailContent(
            viewState = NoteDetailViewState.success(
                note = NoteComponentData.sample(),
                comments = emptyList(),
            ),
            commentInputState = remember { TextFieldState() },
            onBackClick = {},
            onCommentMoreClick = {},
            onSendComment = {},
        )
    }
}
