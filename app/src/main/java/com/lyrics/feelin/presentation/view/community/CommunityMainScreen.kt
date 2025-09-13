package com.lyrics.feelin.presentation.view.community

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.lyrics.feelin.R
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithBack
import com.lyrics.feelin.core.designsystem.component.FeelinTransparentTopAppBar
import com.lyrics.feelin.core.designsystem.component.TopBarIconButton
import com.lyrics.feelin.core.designsystem.icon.NotificationIcon
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.LightGray00
import com.lyrics.feelin.presentation.designsystem.theme.LightGray03
import com.lyrics.feelin.presentation.designsystem.theme.LightGray09
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.component.note.NoteComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityMainScreen(
    artistName: String,
    onBack: () -> Unit,
    viewModel: CommunityViewModel = viewModel()
) {
    val listState = rememberLazyListState()
    val communityViewState by viewModel.viewState.collectAsState()

    val feelinColors = LocalFeelinColors.current

    val isCollapsed by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex >= 1
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadCommunityData()
    }

    Scaffold(
        modifier = Modifier
            .then(
                Modifier.windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
                    .takeIf { isCollapsed } ?: Modifier
            ),
        topBar = {
            if (isCollapsed) {
                FeelinTopAppBarWithBack(
                    title = "$artistName 레코드",
                    onBackClick = onBack,
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.systemBars.only(
                            WindowInsetsSides.Top
                        )
                    )
                )
            } else {
                FeelinTransparentTopAppBar(
                    onBackClick = onBack,
                    actions = {
                        TopBarIconButton(
                            imageVector = NotificationIcon,
                            contentDescription = "알림",
                            tint = LightGray09, // 투명 상태일 때는 다크모드 미 적용입니다.
                            onClick = {},
                        )
                    },
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.systemBars.only(
                            WindowInsetsSides.Top
                        )
                    )
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
                modifier = Modifier
                    .padding(bottom = 4.dp, end = 4.dp),
                onClick = {},
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(R.drawable.pencil),
                    modifier = Modifier.size(24.dp),
                    contentDescription = "write note",
                )
            }
        }
    ) { contentPadding ->
        when (communityViewState.status) {
            CommunityViewStatus.INITIAL,
            CommunityViewStatus.LOADING -> {
                Box(
                    modifier = Modifier
                        .padding(paddingValues = contentPadding)
                        .fillMaxSize()
                        .background(feelinColors.gray00)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(alignment = Alignment.Center)
                    )
                }
            }

            CommunityViewStatus.SUCCESS_LOAD -> {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(
                        top = 0.dp,
                        bottom = contentPadding.calculateBottomPadding()
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(feelinColors.gray00)
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(310.dp)
                        ) {
                            // TODO: 헤더 이미지
                            AsyncImage(
                                model = "https://i.namu.wiki/i/whqmv7WsYtoH3bY7IdwldbHcrZPIsOdPZKNkEmSH6Pk5HqjYzVpBGtxYrJP5cA1LJx9VRg-jb1G319Glx_rAXnAK8-1FLn4qRiDQz2tU9bLfReoHbUxpZWZXHKbEf56okna-ycyi_fPtuuxaRTnZ4w.webp",
                                contentDescription = "${communityViewState.artist.name}'s image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            0.0f to Color.Transparent,
                                            0.6f to Color.Transparent,
                                            1.0f to Color(0x80000000)
                                        )
                                    )
                            )

                            // 아티스트 정보 및 버튼 - 다크모드 미 적용
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .padding(start = 20.dp, end = 20.dp, bottom = 28.dp),
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = "$artistName 레코드",
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        color = LightGray00
                                    ),
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Button(
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonColors(
                                        containerColor = Color(0x33FFFFFF),
                                        contentColor = Color(0x33FFFFFF),
                                        disabledContainerColor = Color(0x33FFFFFF),
                                        disabledContentColor = Color(0x33FFFFFF),
                                    ),
                                    onClick = {},
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                    ) {
                                        Icon(
                                            painter = painterResource(
                                                if (communityViewState.artist.isLike)
                                                    R.drawable.heart_light_active
                                                else R.drawable.heart_light_inactive
                                            ),
                                            tint = LightGray03,
                                            contentDescription = "are you like this artist: ${communityViewState.artist.isLike}",
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "관심 아티스트",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                color = LightGray00
                                            ),
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 노트 설정 섹션
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = "노트",
                                style = MaterialTheme.typography.headlineSmall.copy(color = feelinColors.gray09),
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(R.drawable.check_icon),
                                    tint = if (communityViewState.isViewNoteOnlyLyrics)
                                        MaterialTheme.colorScheme.primary
                                    else feelinColors.gray03,
                                    contentDescription = "",
                                )
                                Text(
                                    text = "가사 포함된 노트만 보기",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = if (communityViewState.isViewNoteOnlyLyrics)
                                            MaterialTheme.colorScheme.primary
                                        else feelinColors.gray03
                                    ),
                                )
                            }
                        }
                    }

                    // 노트 목록
                    items(items = communityViewState.noteState.notes) { note ->
                        NoteComponent(noteData = note)
                    }
                }
            }

            CommunityViewStatus.ERROR -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        "오류가 발생했습니다.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Preview(name = "Light Mode")
@Composable
private fun CommunityMainScreenPreview() {
    FeelinTheme {
        CommunityMainScreen(
            artistName = "실리카겔",
            onBack = {}
        )
    }
}

@Preview(name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CommunityMainScreenDarkPreview() {
    FeelinTheme {
        CommunityMainScreen(
            artistName = "실리카겔",
            onBack = {}
        )
    }
}