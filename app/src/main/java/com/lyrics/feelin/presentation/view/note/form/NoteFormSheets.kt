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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.lyrics.feelin.core.designsystem.component.FeelinModalBottomSheet
import com.lyrics.feelin.core.designsystem.component.FeelinModalBottomSheetAction
import com.lyrics.feelin.core.designsystem.component.FeelinModalDialog
import com.lyrics.feelin.core.designsystem.icon.CheckBoxIconDisabled
import com.lyrics.feelin.core.designsystem.icon.CheckBoxIconEnabled
import com.lyrics.feelin.core.designsystem.icon.CloseIcon
import com.lyrics.feelin.core.domain.enum.NoteTopic
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.component.note.LyricsBackground
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NoteFormCategorySheet(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    onCategorySelect: (NoteTopic) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (!isVisible) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    FeelinModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
    ) {
        listOf(NoteTopic.INTERPRETATION, NoteTopic.FREE, NoteTopic.QUESTION).forEach { topic ->
            FeelinModalBottomSheetAction(
                text = topic.toNoteFormLabel(),
                onClick = {
                    onCategorySelect(topic)
                    onDismissRequest()
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NoteFormLyricsBackgroundSheet(
    isVisible: Boolean,
    selectedBackground: LyricsBackground,
    onDismissRequest: () -> Unit,
    onTemporaryBackgroundSelect: (LyricsBackground) -> Unit,
    onBackgroundConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (!isVisible) return

    val colors = LocalFeelinColors.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    FeelinModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        showDragHandle = false,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(NOTE_FORM_SHEET_HEIGHT_RATIO)
                .padding(horizontal = 20.dp),
        ) {
            NoteFormSheetHeader(
                title = "가사 배경",
                contentDescription = "가사 배경 선택 하단시트 닫기",
                onCloseClick = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onDismissRequest()
                        }
                    }
                },
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(LyricsBackground.entries) { background ->
                    LyricsBackgroundItem(
                        background = background,
                        isSelected = selectedBackground == background,
                        onClick = { onTemporaryBackgroundSelect(background) },
                    )
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
                        onDismissRequest()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "완료",
                    style = FeelinTypography.title2,
                    color = colors.gray00,
                )
            }
        }
    }
}

@Composable
private fun LyricsBackgroundItem(
    background: LyricsBackground,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalFeelinColors.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(132.dp)
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick),
    ) {
        Image(
            painter = painterResource(id = background.noteFormDrawableRes()),
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
                .size(24.dp),
        )

        Text(
            text = "이야기로 음악을 느끼다\n이야기로 음악을 채우다",
            style = FeelinTypography.body1,
            color = if (background.isNoteFormDark()) colors.gray00 else colors.gray09,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 20.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NoteFormLyricsSearchSheet(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (!isVisible) return

    val colors = LocalFeelinColors.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    BackHandler(enabled = isVisible) {
        onDismissRequest()
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        containerColor = colors.modal,
        dragHandle = @Composable {},
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(NOTE_FORM_SHEET_HEIGHT_RATIO)
                .padding(horizontal = 20.dp),
        ) {
            NoteFormSheetHeader(
                title = "가사 검색",
                contentDescription = "가사 검색 하단시트 닫기",
                onCloseClick = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onDismissRequest()
                        }
                    }
                },
            )

            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        // JS 비활성화시 멜론 웹페이지 정상 로드 불가 @이대근 2026.06.07.
                        @SuppressLint("SetJavascriptEnabled")
                        settings.javaScriptEnabled = true
                        webViewClient = WebViewClient()
                        webChromeClient = WebChromeClient()
                        setOnTouchListener { view, event ->
                            // ModalBottomSheet의 드래그 제스처가 WebView 터치와 경쟁해
                            // 이벤트를 가로채지 않도록 합니다.
                            when (event.action) {
                                MotionEvent.ACTION_DOWN -> view.parent.requestDisallowInterceptTouchEvent(true)
                                MotionEvent.ACTION_UP,
                                MotionEvent.ACTION_CANCEL -> view.parent.requestDisallowInterceptTouchEvent(false)
                            }
                            false
                        }
                        loadUrl(NOTE_FORM_LYRICS_SEARCH_URL)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onRelease = { webView ->
                    webView.stopLoading()
                    webView.webChromeClient = null
                    webView.removeAllViews()
                    webView.destroy()
                },
            )
        }
    }
}

@Composable
private fun NoteFormSheetHeader(
    title: String,
    contentDescription: String,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalFeelinColors.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 20.dp),
    ) {
        Text(
            text = title,
            style = FeelinTypography.title2,
            color = colors.gray09,
        )
        Icon(
            imageVector = CloseIcon,
            contentDescription = contentDescription,
            tint = colors.gray09,
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onCloseClick),
        )
    }
}

@Composable
internal fun NoteFormNoSongDialog(
    isVisible: Boolean,
    onConfirmClick: () -> Unit,
) {
    if (!isVisible) return

    FeelinModalDialog(
        title = "곡을 추가한 후,\n가사를 작성하실 수 있어요.",
        confirmButtonText = "확인",
        onConfirmButtonClick = onConfirmClick,
        description = null,
        isDismissButtonEnable = false,
    )
}

private const val NOTE_FORM_LYRICS_SEARCH_URL = "https://search.melon.com/search/mcom_index.htm"
private const val NOTE_FORM_SHEET_HEIGHT_RATIO = 0.88f
