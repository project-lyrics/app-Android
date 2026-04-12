package com.lyrics.feelin.presentation.view.note.search.result

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithBack
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.component.note.NoteComponent
import com.lyrics.feelin.presentation.view.component.note.NoteComponentData
import kotlinx.coroutines.flow.collectLatest

private enum class HeaderState {
    Expanded,
    CollapsedBarOnly,
    BarHidden,
}

private const val HEADER_EXPAND_OFFSET_THRESHOLD = 50
private const val COLLAPSED_TOP_BAR_HEIGHT = 56
private const val TOPIC_FILTER_ROW_HEIGHT = 50
private const val FILTER_ROW_INDEX = 1
private const val DUMMY_SELECTED_FILTER_INDEX = 1

private val dummyNotes = List(size = 10) { index ->
    if (index % 2 == 0) {
        NoteComponentData.sample()
    } else {
        NoteComponentData.sampleNoLyrics()
    }
}

// TODO(@이대근): enum화 2026.04.13.
private val dummyTopicFilters = listOf("전체노트", "해석공유", "자유", "질문")

/**
 * 검색 결과 화면의 헤더 노출 상태를 계산한다.
 *
 * 스크롤 방향과 최상단 offset을 함께 봐야 위로 복귀 중에는 bar만 보이고,
 * 최상단에 닿으면 expanded로 전환되는 요구사항을 안정적으로 표현할 수 있다.
 */
@Composable
private fun rememberHeaderState(listState: LazyListState): HeaderState {
    var headerState by remember { mutableStateOf(HeaderState.Expanded) }
    var isScrollingUp by remember { mutableStateOf(true) }
    var previousIndex by remember { mutableIntStateOf(listState.firstVisibleItemIndex) }
    var previousScrollOffset by remember { mutableIntStateOf(listState.firstVisibleItemScrollOffset) }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }.collectLatest { (currentIndex, currentOffset) ->
            if (currentIndex > previousIndex) {
                isScrollingUp = false
            } else if (currentIndex < previousIndex) {
                isScrollingUp = true
            } else if (currentOffset > previousScrollOffset) {
                isScrollingUp = false
            } else if (currentOffset < previousScrollOffset) {
                isScrollingUp = true
            }

            headerState = if (currentIndex == 0 && currentOffset < HEADER_EXPAND_OFFSET_THRESHOLD) {
                HeaderState.Expanded
            } else if (isScrollingUp) {
                HeaderState.CollapsedBarOnly
            } else {
                HeaderState.BarHidden
            }

            previousIndex = currentIndex
            previousScrollOffset = currentOffset
        }
    }

    return headerState
}

@Composable
fun NoteSearchResultScreen(modifier: Modifier = Modifier) {
    val feelinColors = LocalFeelinColors.current
    val density = LocalDensity.current
    val listState = rememberLazyListState()
    val headerState = rememberHeaderState(listState)
    val pinnedFilterTopPadding = if (headerState == HeaderState.CollapsedBarOnly) {
        COLLAPSED_TOP_BAR_HEIGHT.dp
    } else {
        0.dp
    }
    val pinnedFilterOffsetPx = with(density) { pinnedFilterTopPadding.roundToPx() }
    val isFilterPinned by remember(listState, headerState, pinnedFilterOffsetPx) {
        derivedStateOf {
            val filterItem = listState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == FILTER_ROW_INDEX }

            when {
                filterItem == null -> listState.firstVisibleItemIndex > FILTER_ROW_INDEX
                else -> filterItem.offset <= pinnedFilterOffsetPx
            }
        }
    }

    Scaffold(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
            .background(color = feelinColors.backgroundPrimary),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(top = 56.dp, bottom = 24.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .background(feelinColors.backgroundPrimary),
            ) {
                item {
                    ExpandedSearchHeader()
                }

                item {
                    if (isFilterPinned) {
                        Spacer(modifier = Modifier.height(TOPIC_FILTER_ROW_HEIGHT.dp))
                    } else {
                        TopicFilterRow()
                    }
                }

                items(dummyNotes) { note ->
                    NoteComponent(
                        noteData = note,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    )
                }
            }

            AnimatedVisibility(
                visible = headerState != HeaderState.BarHidden,
                enter = slideInVertically(initialOffsetY = { -it }),
                exit = slideOutVertically(targetOffsetY = { -it }),
                modifier = Modifier.align(Alignment.TopCenter),
            ) {
                FeelinTopAppBarWithBack(
                    title = "노트 검색",
                    showDivider = false,
                    onBackClick = {},
                )
            }

            AnimatedVisibility(
                visible = headerState != HeaderState.Expanded && isFilterPinned,
                enter = slideInVertically(initialOffsetY = { -it / 2 }),
                exit = slideOutVertically(targetOffsetY = { -it / 2 }),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = pinnedFilterTopPadding),
            ) {
                TopicFilterRow()
            }
        }
    }
}

@Composable
private fun ExpandedSearchHeader(modifier: Modifier = Modifier) {
    val feelinColors = LocalFeelinColors.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(28.dp))
        SearchedSongSummaryCard()
        Spacer(modifier = Modifier.height(42.dp))
        Text(
            text = buildAnnotatedString {
                append("노트 ")
                withStyle(style = SpanStyle(color = feelinColors.brandPrimary)) {
                    append("13")
                }
            },
            style = FeelinTypography.heading3.copy(color = feelinColors.gray09),
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun TopicFilterRow(modifier: Modifier = Modifier) {
    val feelinColors = LocalFeelinColors.current
    val density = LocalDensity.current
    val selectedIndex = DUMMY_SELECTED_FILTER_INDEX
    val textWidths = remember { mutableStateMapOf<Int, Int>() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(TOPIC_FILTER_ROW_HEIGHT.dp)
            .background(feelinColors.backgroundPrimary)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = feelinColors.gray01,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        dummyTopicFilters.forEachIndexed { index, label ->
            val isSelected = index == selectedIndex
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    style = FeelinTypography.title3.copy(
                        color = if (isSelected) feelinColors.gray09 else feelinColors.gray04,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 20.sp,
                    ),
                    onTextLayout = { textLayoutResult ->
                        textWidths[index] = textLayoutResult.size.width
                    },
                )
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .width(with(density) { (textWidths[index] ?: 0).toDp() })
                            .height(2.dp)
                            .background(feelinColors.gray09)
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchedSongSummaryCard(modifier: Modifier = Modifier) {
    val feelinColors = LocalFeelinColors.current

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = feelinColors.gray00,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = "https://i.scdn.co/image/ab67616d0000b2730b1e2a5d990c3e198effa85b",
                contentDescription = null,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .aspectRatio(1f),
            )
            Column(
                modifier = Modifier.padding(start = 14.dp),
            ) {
                Text(
                    text = "시퍼런 봄",
                    style = FeelinTypography.title2.copy(color = feelinColors.gray09),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "쏜애플",
                    style = FeelinTypography.body2.copy(color = feelinColors.gray05),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview(name = "NoteSearchResultScreen - Light", showBackground = true)
@Preview(
    name = "NoteSearchResultScreen - Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun NoteSearchResultScreenPreview() {
    FeelinTheme {
        NoteSearchResultScreen()
    }
}
