package com.lyrics.feelin.presentation.view.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lyrics.feelin.core.designsystem.component.FeelinTab
import com.lyrics.feelin.core.designsystem.component.FeelinTabRow
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarNoBack
import com.lyrics.feelin.core.designsystem.component.FilterButton
import com.lyrics.feelin.core.designsystem.component.TopBarIconButton
import com.lyrics.feelin.core.designsystem.icon.CaretIcon
import com.lyrics.feelin.core.designsystem.icon.EmptyImageDarkIcon
import com.lyrics.feelin.core.designsystem.icon.EmptyImageLightIcon
import com.lyrics.feelin.core.designsystem.icon.NotificationIconLight
import com.lyrics.feelin.core.designsystem.icon.SettingsIconLight
import com.lyrics.feelin.core.domain.model.ProfileType
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.component.note.NoteComponent
import com.lyrics.feelin.presentation.view.component.note.NoteComponentData
import com.lyrics.feelin.presentation.view.component.note.NoteMenuBottomSheet
import com.lyrics.feelin.presentation.view.component.profile.ProfileComponent

private const val NICKNAME_CARET_ROTATION_DEGREES = 270f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageScreen(
    onSettingClick: () -> Unit,
    modifier: Modifier = Modifier,
    onNoteClick: (Long) -> Unit = {},
    onNoteReportClick: (Long) -> Unit = {},
    viewModel: MyPageViewModel = hiltViewModel(),
) {
    val myPageState by viewModel.myPageScreenState.collectAsState()
    val currentUserId by viewModel.currentUserId.collectAsState()
    var selectedNoteForMenu by remember { mutableStateOf<NoteComponentData?>(null) }

    val feelinColors = LocalFeelinColors.current

    var tabIndex by rememberSaveable { mutableIntStateOf(0) }

    var selectButtonIndex by rememberSaveable { mutableIntStateOf(0) }

    fun onTabChange(index: Int) {
        tabIndex = index
    }

    LaunchedEffect(Unit) {
        viewModel.loadMyPageData()
    }

    Scaffold(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
            .background(color = feelinColors.backgroundPrimary),
        topBar = {
            FeelinTopAppBarNoBack(
                title = "마이페이지",
                actions = {
                    TopBarIconButton(
                        imageVector = SettingsIconLight,
                        contentDescription = "설정",
                        tint = feelinColors.gray09,
                        onClick = onSettingClick
                    )
                    TopBarIconButton(
                        imageVector = NotificationIconLight,
                        contentDescription = "알림",
                        tint = feelinColors.gray09,
                        onClick = {}
                    )
                }
            )
        }
    ) { contentPadding ->
        when (myPageState.status) {
            MyPageScreenStatus.INITIAL,
            MyPageScreenStatus.LOADING -> {
                Box(
                    modifier = Modifier
                        .padding(paddingValues = contentPadding)
                        .fillMaxSize()
                        .background(feelinColors.backgroundPrimary)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(alignment = Alignment.Center)
                    )
                }
            }

            MyPageScreenStatus.SUCCESS_LOAD -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = feelinColors.backgroundPrimary)
                        .padding(paddingValues = contentPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    ProfileComponent(
                        type = myPageState.user?.profileCharacterType ?: ProfileType.SHORT_HAIR,
                        size = 100,
                        modifier = Modifier.padding(top = 24.dp, bottom = 20.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = myPageState.user?.nickname ?: "로그인",
                            style = FeelinTypography.heading3,
                            textAlign = TextAlign.Center
                        )
                        Icon(
                            imageVector = CaretIcon,
                            contentDescription = "change nickname",
                            tint = feelinColors.gray09,
                            modifier = Modifier.rotate(NICKNAME_CARET_ROTATION_DEGREES)
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    FeelinTabRow(selectedTabIndex = tabIndex) {
                        FeelinTab(
                            selected = (tabIndex == 0),
                            onClick = { onTabChange(0) },
                            text = {
                                Text("작성글", style = FeelinTypography.title2)
                            }
                        )
                        FeelinTab(
                            selected = (tabIndex == 1),
                            onClick = { onTabChange(1) },
                            text = {
                                Text("북마크", style = FeelinTypography.title2)
                            }
                        )
                    }

                    // MARK: 로그아웃 상태
                    myPageState.user ?: run {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "로그인 후 이용하실 수 있어요",
                                style = FeelinTypography.body3.copy(color = feelinColors.gray09),
                                textAlign = TextAlign.Center
                            )

                            Box(
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .size(width = 124.dp, height = 40.dp)
                                    .clip(shape = RoundedCornerShape(8.dp))
                                    .clickable(enabled = true, onClick = { /**/ })
                                    .background(color = feelinColors.gray01),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "로그인하러 가기",
                                    style = FeelinTypography.body2.copy(color = feelinColors.gray09)
                                )
                            }
                        }
                    }

                    when (myPageState.tabStatus) {
                        MyPageTabScreenStatus.INITIAL,
                        MyPageTabScreenStatus.LOADING -> {
                            Box(
                                modifier = Modifier
                                    .padding(paddingValues = contentPadding)
                                    .fillMaxSize()
                                    .background(feelinColors.backgroundPrimary)
                                    .weight(1f)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(alignment = Alignment.Center)
                                )
                            }
                        }
                        MyPageTabScreenStatus.SUCCESS_LOAD -> {
                            // MARK: 로그인 + 노트 없음 상태
                            myPageState.user?.takeIf { myPageState.notes.isNullOrEmpty() }?.let {
                                // MARK(@이대근): iOS 앱에 구현된 아이콘과 텍스트 색상을 사용했습니다. 2025.11.20.
                                val emptyImageIcon = if (isSystemInDarkTheme()) {
                                    EmptyImageDarkIcon
                                } else {
                                    EmptyImageLightIcon
                                }

                                val noNoteText = if (tabIndex == 0) "작성한" else "북마크한"

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        imageVector = emptyImageIcon,
                                        contentDescription = "No notes",
                                        modifier = Modifier.size(width = 78.dp, height = 48.dp)
                                    )
                                    Text(
                                        text = "$noNoteText 노트가 없어요",
                                        style = FeelinTypography.body3.copy(color = feelinColors.gray04),
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }

                            // MARK: 로그인 + 노트 있음 상태
                            if (myPageState.user != null && myPageState.notes?.isNotEmpty() == true) {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                ) {
                                    item {
                                        LazyRow(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 16.dp),
                                            contentPadding = PaddingValues(horizontal = 20.dp),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            itemsIndexed(items = myPageState.filterArtists) { index, item ->
                                                FilterButton(
                                                    data = item,
                                                    isSelect = (selectButtonIndex == index),
                                                    onClick = { selectButtonIndex = index }
                                                )
                                            }
                                        }
                                    }

                                    items(items = myPageState.notes!!) {
                                        NoteComponent(
                                            noteData = it,
                                            onClick = { note -> onNoteClick(note.id) },
                                            onMenuClick = { note -> selectedNoteForMenu = note },
                                        )
                                    }
                                }
                            }
                        }
                        MyPageTabScreenStatus.ERROR -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f)
                            ) {
                                // TODO(@이대근): 에러 다이얼로그 표시 2025.11.21.
                                Text(
                                    "오류가 발생했습니다.",
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }

            MyPageScreenStatus.ERROR -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(feelinColors.backgroundPrimary)
                ) {
                    // TODO(@이대근): 에러 다이얼로그 표시 2025.11.19.
                    Text(
                        "오류가 발생했습니다.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        selectedNoteForMenu?.let { selectedNote ->
            NoteMenuBottomSheet(
                noteData = selectedNote,
                currentUserId = currentUserId,
                onReportClick = { noteId ->
                    selectedNoteForMenu = null
                    onNoteReportClick(noteId)
                },
                onDismissRequest = { selectedNoteForMenu = null },
            )
        }
    }
}

@Preview
@Composable
private fun MyPageScreenPreview() {
    FeelinTheme {
        MyPageScreen(onSettingClick = {})
    }
}
