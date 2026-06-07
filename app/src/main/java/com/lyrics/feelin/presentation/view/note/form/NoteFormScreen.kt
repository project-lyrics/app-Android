package com.lyrics.feelin.presentation.view.note.form

import android.annotation.SuppressLint
import android.view.MotionEvent
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.lyrics.feelin.R
import com.lyrics.feelin.core.designsystem.component.FeelinModalBottomSheet
import com.lyrics.feelin.core.designsystem.component.FeelinModalBottomSheetAction
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarDefaults
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithClose
import com.lyrics.feelin.core.designsystem.icon.CaretIcon
import com.lyrics.feelin.core.designsystem.icon.CheckBoxIconDisabled
import com.lyrics.feelin.core.designsystem.icon.CheckBoxIconEnabled
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
import kotlinx.coroutines.launch

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

@Suppress("MagicNumber", "MaxLineLength", "LongMethod", "MultipleEmitters")
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

    val scope = rememberCoroutineScope()

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
                        onClick = onCompleteClick
                    )
                )
            },
            paddingValues = PaddingValues(
                horizontal = FeelinTopAppBarDefaults.HorizontalPadding,
                vertical = 10.dp,
            ),
            modifier = Modifier,
            showDivider = false
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            // Category Selector
            Row(
                modifier = Modifier
                    .clickable(onClick = onCategorySheetOpen)
                    .padding(vertical = 16.dp),
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
                    color = colors.gray08
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = CaretIcon,
                    contentDescription = "카테고리 선택",
                    tint = colors.gray08,
                    modifier = Modifier.size(16.dp)
                )
            }

            // Song Component - delete row
            if (uiState.isSongDeleteVisible) {
                HorizontalDivider(color = colors.gray01, thickness = 1.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "곡",
                        style = FeelinTypography.title3.copy(lineHeight = 20.sp),
                        color = colors.gray08,
                        modifier = Modifier.clickable(onClick = onSongDelete)
                    )
                    Icon(
                        imageVector = CloseIcon,
                        contentDescription = "삭제",
                        tint = colors.gray08,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable(onClick = onSongDelete)
                    )
                }
            }

            // Song Component
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

            Spacer(modifier = Modifier.height(16.dp))

            // Lyrics Component
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
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
                                // TODO(@이대근): 입력 상태로 focus될때 나오지 않아야함 2026.06.04.
                                Text(
                                    text = "좋아하는 가사를 적어주세요 (선택)",
                                    style = FeelinTypography.body1,
                                    color = if (uiState.lyrics.isEmpty()) colors.gray04 else colors.gray08,
                                    textAlign = TextAlign.Center
                                )
                            }
                            innerTextField()
                        }
                    }
                )
                Text(
                    text = "${uiState.lyrics.length}/$NOTE_FORM_LYRICS_MAX_LENGTH",
                    style = FeelinTypography.caption1,
                    color = if (uiState.lyrics.isEmpty()) colors.gray04 else colors.gray08,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 16.dp, end = 20.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LyricsActionButton(
                    text = "가사 배경",
                    isEnable = uiState.lyrics.isNotEmpty(),
                    icon = { WritingIcon },
                    onClick = onLyricsBackgroundSheetOpen,
                )
                LyricsActionButton(
                    text = "가사 검색",
                    isEnable = true,
                    icon = { SearchIcon },
                    onClick = onLyricsSearchSheetOpen,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // TODO(@이대근): 특정 배경들에는 내부 텍스트 색상이 바뀌어야함 2026.06.07.
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
                textStyle = FeelinTypography.body3.copy(color = colors.gray08),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp),
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
                        if (uiState.body.isEmpty()) {
                            // TODO(@이대근): 카테고리 선택마다 플레이스홀더 문자열이 바뀌어야함 2026.06.04.
                            Text(
                                text = "생각을 남겨보세요.",
                                style = FeelinTypography.body3,
                                color = colors.gray04
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
            onDismissRequest = onLyricsBackgroundSheetClose,
            showDragHandle = false
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.88f)
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 20.dp)
                ) {
                    Text(
                        text = "가사 배경",
                        style = FeelinTypography.title2,
                        color = colors.gray09,
                    )
                    Icon(
                        imageVector = CloseIcon,
                        contentDescription = "가사 배경 선택 하단시트 닫기",
                        tint = colors.gray09,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(onClick = {
                                scope.launch {
                                    sheetState.hide()
                                }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        onLyricsBackgroundSheetClose()
                                    }
                                }
                            })
                    )
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(LyricsBackground.entries) { background ->
                        val isSelected = uiState.temporaryLyricsBackground == background
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(132.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .clickable { onTemporaryBackgroundSelect(background) }
                        ) {
                            Image(
                                painter = painterResource(id = background.toDrawableRes()),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                            )

                            Icon(
                                imageVector = if (isSelected) CheckBoxIconEnabled else CheckBoxIconDisabled,
                                contentDescription = if (isSelected) "선택됨" else "선택 안됨",
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 16.dp, end = 16.dp)
                                    .size(24.dp)
                            )

                            // TODO(@이대근): 특정 배경들에는 텍스트 색상이 바뀌어야함 2026.06.07.
                            Text(
                                text = "이야기로 음악을 느끼다\n이야기로 음악을 채우다",
                                style = FeelinTypography.body1,
                                color = if (background == LyricsBackground.BLACK) colors.gray00 else colors.gray09,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(horizontal = 20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(8.dp))
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
            containerColor = colors.modal,
            dragHandle = @Composable {}
        ) {
            Column(modifier = Modifier.fillMaxHeight(0.88f).padding(horizontal = 20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 20.dp)
                ) {
                    Text(
                        text = "가사 검색",
                        style = FeelinTypography.title2,
                        color = colors.gray09,
                    )
                    Icon(
                        imageVector = CloseIcon,
                        contentDescription = "가사 검색 하단시트 닫기",
                        tint = colors.gray09,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(onClick = {
                                scope.launch {
                                    sheetState.hide()
                                }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        onLyricsSearchSheetClose()
                                    }
                                }
                            })
                    )
                }

                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            // JS 비활성화시 멜론 웹페이지 정상 로드 불가 @이대근 2026.06.07.
                            @SuppressLint("SetJavascriptEnabled")
                            settings.javaScriptEnabled = true
                            webViewClient = WebViewClient()
                            webChromeClient = WebChromeClient()
                            setOnTouchListener { view, event ->
                                when (event.action) {
                                    MotionEvent.ACTION_DOWN -> view.parent.requestDisallowInterceptTouchEvent(true)
                                    MotionEvent.ACTION_UP,
                                    MotionEvent.ACTION_CANCEL -> view.parent.requestDisallowInterceptTouchEvent(false)
                                }
                                false
                            }
                            loadUrl("https://search.melon.com/search/mcom_index.htm")
                        }
                    },
                    modifier = Modifier.fillMaxWidth().weight(1f),
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
    isEnable: Boolean,
    icon: @Composable () -> ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalFeelinColors.current
    val actionBtnColor = if (isEnable) colors.gray05 else colors.systemDisable

    Row(
        modifier = modifier
            .border(width = 1.dp, color = colors.gray01, shape = RoundedCornerShape(4.dp))
            .clickable(enabled = isEnable, onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon(),
            contentDescription = text,
            tint = actionBtnColor,
            modifier = Modifier.size(16.dp),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = FeelinTypography.body2,
            color = actionBtnColor,
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
