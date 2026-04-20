package com.lyrics.feelin.presentation.view.note.search.result

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithBack
import com.lyrics.feelin.core.domain.enum.NoteTopic
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.util.label
import com.lyrics.feelin.presentation.view.component.note.NoteComponent
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
private const val PAGINATION_PREFETCH_THRESHOLD = 3

private val noteTopicFilters = NoteTopic.entries

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
fun NoteSearchResultScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: NoteSearchResultViewModel = viewModel(),
) {
    val viewState by viewModel.viewState.collectAsState()
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

    LaunchedEffect(listState) {
        snapshotFlow {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItemIndex to totalItems
        }.collectLatest { (lastVisible, total) ->
            // 스크롤 끝에 닿고 나서 로드하면 체감 지연이 커서, 마지막 몇 개 전부터 다음 페이지를 미리 요청한다.
            if (total > 0 && lastVisible >= total - PAGINATION_PREFETCH_THRESHOLD) {
                viewModel.loadNextPage()
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
                    ExpandedSearchHeader(
                        totalNoteCount = viewState.totalNoteCount,
                        songSummary = viewState.songSummary,
                    )
                }

                item {
                    if (isFilterPinned) {
                        Spacer(modifier = Modifier.height(TOPIC_FILTER_ROW_HEIGHT.dp))
                    } else {
                        TopicFilterRow(
                            selectedNoteTopic = viewState.selectedNoteTopic,
                            onTopicSelect = viewModel::selectTopic,
                        )
                    }
                }

                if (
                    viewState.status == NoteSearchResultStatus.INITIAL ||
                    viewState.status == NoteSearchResultStatus.LOADING
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                } else if (viewState.status == NoteSearchResultStatus.ERROR) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = viewState.errorMessage ?: "오류가 발생했습니다.",
                                style = FeelinTypography.body1.copy(color = feelinColors.gray04),
                            )
                        }
                    }
                } else if (viewState.status == NoteSearchResultStatus.EMPTY) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "작성된 노트가 없어요.",
                                style = FeelinTypography.body1.copy(color = feelinColors.gray04),
                            )
                        }
                    }
                } else {
                    items(viewState.notes) { note ->
                        NoteComponent(
                            noteData = note,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                        )
                    }

                    if (viewState.isNextPageLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            }
                        }
                    }
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
                    onBackClick = onBackClick,
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
                TopicFilterRow(
                    selectedNoteTopic = viewState.selectedNoteTopic,
                    onTopicSelect = viewModel::selectTopic,
                )
            }
        }
    }
}

@Composable
private fun ExpandedSearchHeader(
    totalNoteCount: Int,
    songSummary: NoteSearchResultSongSummary,
    modifier: Modifier = Modifier
) {
    val feelinColors = LocalFeelinColors.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(28.dp))
        SearchedSongSummaryCard(songSummary = songSummary)
        Spacer(modifier = Modifier.height(42.dp))
        Text(
            text = buildAnnotatedString {
                append("노트 ")
                withStyle(style = SpanStyle(color = feelinColors.brandPrimary)) {
                    append(totalNoteCount.toString())
                }
            },
            style = FeelinTypography.heading3.copy(color = feelinColors.gray09),
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}

// MARK(@이대근): 추후 홈화면과 아티스트 화면에서도 사용해야 하니 컴포넌트화를 고려 2026.04.13.
@Composable
private fun TopicFilterRow(
    selectedNoteTopic: NoteTopic,
    onTopicSelect: (NoteTopic) -> Unit,
    modifier: Modifier = Modifier,
) {
    val feelinColors = LocalFeelinColors.current
    val density = LocalDensity.current
    val textWidths = remember { mutableStateMapOf<NoteTopic, Int>() }

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
        noteTopicFilters.forEach { topic ->
            val isSelected = topic == selectedNoteTopic
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable { onTopicSelect(topic) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = topic.label,
                    style = FeelinTypography.title3.copy(
                        color = if (isSelected) feelinColors.gray09 else feelinColors.gray04,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 20.sp,
                    ),
                    onTextLayout = { textLayoutResult ->
                        textWidths[topic] = textLayoutResult.size.width
                    },
                )
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .width(with(density) { (textWidths[topic] ?: 0).toDp() })
                            .height(2.dp)
                            .background(feelinColors.gray09)
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchedSongSummaryCard(
    songSummary: NoteSearchResultSongSummary,
    modifier: Modifier = Modifier,
) {
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
                model = songSummary.imageUrl,
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
                    text = songSummary.title,
                    style = FeelinTypography.title2.copy(color = feelinColors.gray09),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = songSummary.artistName,
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
