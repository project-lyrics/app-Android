package com.lyrics.feelin.presentation.view.note.form

import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.lyrics.feelin.R
import com.lyrics.feelin.core.designsystem.component.FeelinModalBottomSheet
import com.lyrics.feelin.core.designsystem.component.FeelinModalBottomSheetAction
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithClose
import com.lyrics.feelin.core.designsystem.icon.CaretIcon
import com.lyrics.feelin.core.designsystem.icon.CloseIcon
import com.lyrics.feelin.core.designsystem.icon.SearchIcon
import com.lyrics.feelin.core.designsystem.icon.WritingIcon
import com.lyrics.feelin.core.domain.enum.NoteTopic
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.component.music.MusicComponent
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
        onSongDelete = viewModel::deleteSong,
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
        modifier = modifier,
    )
}

@Suppress("MagicNumber", "MaxLineLength", "LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
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
    modifier: Modifier = Modifier,
) {
    val colors = LocalFeelinColors.current
    val scrollState = rememberScrollState()

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
                    style = FeelinTypography.title3,
                    color = if (isCompleteEnabled) colors.brandPrimary else colors.systemDisable,
                    modifier = Modifier.clickable(
                        enabled = isCompleteEnabled,
                        onClick = onCompleteClick
                    )
                )
            },
            modifier = Modifier.padding(horizontal = 20.dp),
            showDivider = true
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            // Category Selector
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.gray01)
                    .clickable(onClick = onCategorySheetOpen)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (uiState.selectedTopic) {
                        NoteTopic.INTERPRETATION -> "해석공유"
                        NoteTopic.FREE -> "자유"
                        NoteTopic.QUESTION -> "질문"
                        else -> "주제를 선택해 주세요"
                    },
                    style = FeelinTypography.body2,
                    color = if (uiState.selectedTopic != null) colors.gray09 else colors.gray04
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = CaretIcon,
                    contentDescription = "카테고리 선택",
                    tint = colors.gray04,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Song Component
            if (uiState.isSongDeleteVisible) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "선택한 노래 삭제",
                        style = FeelinTypography.caption1,
                        color = colors.gray04,
                        modifier = Modifier.clickable(onClick = onSongDelete)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = CloseIcon,
                        contentDescription = "삭제",
                        tint = colors.gray04,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable(onClick = onSongDelete)
                    )
                }
            }

            Box(
                modifier = Modifier.clickable(
                    enabled = uiState.isSongSelectable,
                    onClick = onSongClick,
                )
            ) {
                MusicComponent(
                    state = uiState.selectedSong ?: MusicComponentData.NoteWriteEmpty,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .height(132.dp)
            ) {
                Image(
                    painter = painterResource(id = uiState.lyricsBackground.toDrawableRes()),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
                BasicTextField(
                    value = uiState.lyrics,
                    onValueChange = {
                        if (it.length <= NOTE_FORM_LYRICS_MAX_LENGTH) {
                            onLyricsChange(it)
                        } else {
                            onLyricsChange(it.take(NOTE_FORM_LYRICS_MAX_LENGTH))
                        }
                    },
                    textStyle = FeelinTypography.body1.copy(
                        color = if (uiState.lyricsBackground == LyricsBackground.BLACK) colors.gray00 else colors.gray09,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .padding(horizontal = 20.dp),
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (uiState.lyrics.isEmpty()) {
                                Text(
                                    text = "가사를 입력해 주세요",
                                    style = FeelinTypography.body1,
                                    color = if (uiState.lyricsBackground == LyricsBackground.BLACK) colors.gray05 else colors.gray04,
                                    textAlign = TextAlign.Center
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    LyricsActionButton(
                        text = "가사 배경",
                        icon = { WritingIcon },
                        onClick = onLyricsBackgroundSheetOpen,
                    )
                    LyricsActionButton(
                        text = "가사 검색",
                        icon = { SearchIcon },
                        onClick = onLyricsSearchSheetOpen,
                    )
                }

                Text(
                    text = "${uiState.lyrics.length}/$NOTE_FORM_LYRICS_MAX_LENGTH",
                    style = FeelinTypography.caption1,
                    color = colors.gray04
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Body Input Area
            BasicTextField(
                value = uiState.body,
                onValueChange = {
                    if (it.length <= NOTE_FORM_BODY_MAX_LENGTH) {
                        onBodyChange(it)
                    } else {
                        onBodyChange(it.take(NOTE_FORM_BODY_MAX_LENGTH))
                    }
                },
                textStyle = FeelinTypography.body1.copy(color = colors.gray09),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
                        if (uiState.body.isEmpty()) {
                            Text(
                                text = "어떤 감상을 남기고 싶나요?",
                                style = FeelinTypography.body1,
                                color = colors.gray03
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "${uiState.body.length}/$NOTE_FORM_BODY_MAX_LENGTH",
                    style = FeelinTypography.caption1,
                    color = colors.gray04
                )
            }
        }
    }

    // Category Bottom Sheet
    if (uiState.isCategorySheetVisible) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        FeelinModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onCategorySheetClose
        ) {
            FeelinModalBottomSheetAction(
                text = "해석공유",
                onClick = {
                    onCategorySelect(NoteTopic.INTERPRETATION)
                    onCategorySheetClose()
                }
            )
            FeelinModalBottomSheetAction(
                text = "자유",
                onClick = {
                    onCategorySelect(NoteTopic.FREE)
                    onCategorySheetClose()
                }
            )
            FeelinModalBottomSheetAction(
                text = "질문",
                onClick = {
                    onCategorySelect(NoteTopic.QUESTION)
                    onCategorySheetClose()
                }
            )
        }
    }

    // Lyrics Background Bottom Sheet
    if (uiState.isLyricsBackgroundSheetVisible) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        FeelinModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onLyricsBackgroundSheetClose
        ) {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "가사 배경 선택",
                    style = FeelinTypography.title1,
                    color = colors.gray09,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(LyricsBackground.entries) { background ->
                        Box(
                            modifier = Modifier
                                .size(width = 72.dp, height = 48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    width = if (uiState.temporaryLyricsBackground == background) 2.dp else 0.dp,
                                    color = if (uiState.temporaryLyricsBackground == background) colors.brandPrimary else colors.modal,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { onTemporaryBackgroundSelect(background) }
                        ) {
                            Image(
                                painter = painterResource(id = background.toDrawableRes()),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.brandPrimary)
                        .clickable {
                            onBackgroundConfirm()
                            onLyricsBackgroundSheetClose()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "완료",
                        style = FeelinTypography.title2,
                        color = colors.gray00
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Lyrics Search WebView Bottom Sheet
    if (uiState.isLyricsSearchSheetVisible) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        BackHandler(enabled = uiState.isLyricsSearchSheetVisible) {
            onLyricsSearchSheetClose()
        }

        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onLyricsSearchSheetClose,
            containerColor = colors.backgroundPrimary,
            modifier = Modifier.fillMaxHeight(0.88f)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            webViewClient = WebViewClient()
                            webChromeClient = WebChromeClient()
                            loadUrl("https://search.melon.com/search/mcom_index.htm")
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    onRelease = { webView ->
                        webView.stopLoading()
                        webView.webChromeClient = null
                        webView.removeAllViews()
                        webView.destroy()
                    }
                )
            }
        }
    }
}

@Composable
private fun LyricsActionButton(
    text: String,
    icon: @Composable () -> androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalFeelinColors.current

    Row(
        modifier = modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon(),
            contentDescription = text,
            tint = colors.gray04,
            modifier = Modifier.size(16.dp),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = FeelinTypography.caption1,
            color = colors.gray04,
        )
    }
}

private fun LyricsBackground.toDrawableRes(): Int {
    return when (this) {
        LyricsBackground.DEFAULT -> R.drawable.lyrics_background_img00
        LyricsBackground.SKYBLUE -> R.drawable.lyrics_background_img01
        LyricsBackground.BLUE -> R.drawable.lyrics_background_img02
        LyricsBackground.LAVENDER -> R.drawable.lyrics_background_img03
        LyricsBackground.MINT -> R.drawable.lyrics_background_img04
        LyricsBackground.BLACK -> R.drawable.lyrics_background_img05
        LyricsBackground.BEIGE -> R.drawable.lyrics_background_img06
        LyricsBackground.PINKGREEN -> R.drawable.lyrics_background_img07
        LyricsBackground.RED -> R.drawable.lyrics_background_img08
        LyricsBackground.WHITE -> R.drawable.lyrics_background_img09
        LyricsBackground.RAINBOW -> R.drawable.lyrics_background_img10
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteFormScreenCreatePreview() {
    FeelinTheme {
        NoteFormScreen(
            uiState = NoteFormUiState.create(),
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
            onLyricsSearchSheetClose = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteFormScreenFreeWithSongPreview() {
    FeelinTheme {
        NoteFormScreen(
            uiState = NoteFormUiState.create().copy(
                selectedTopic = NoteTopic.FREE,
                selectedSong = MusicComponentData.NoteWriteMusicExist(
                    imageUrl = "https://picsum.photos/200",
                    songName = "No Pain",
                    artistName = "실리카겔"
                )
            ),
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
            onLyricsSearchSheetClose = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteFormScreenEditPreview() {
    FeelinTheme {
        NoteFormScreen(
            uiState = NoteFormUiState.editSample(),
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
            onLyricsSearchSheetClose = {}
        )
    }
}
