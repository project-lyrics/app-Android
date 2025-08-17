package com.lyrics.feelin.presentation.view.community

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lyrics.feelin.R
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.LightGray00
import com.lyrics.feelin.presentation.designsystem.theme.LightGray01
import com.lyrics.feelin.presentation.designsystem.theme.LightGray03
import com.lyrics.feelin.presentation.designsystem.theme.LightGray09
import com.lyrics.feelin.presentation.view.component.note.NoteComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO(@이대근): 다크모드 대응 필요 2025.08.17.

@Composable
fun CommunityView(modifier: Modifier = Modifier) {
    // MARK(@이대근): 목업용 변수, 실제 뷰모델 생성 후 삭제 예정 2025.08.17
    val communityViewState = remember { mutableStateOf(CommunityViewState.initial()) }
    val dummyScope = rememberCoroutineScope()

    // MARK(@이대근): 목업용 데이터 진입점, 실제 뷰모델 생성 후 삭제 예정 2025.08.17.
    LaunchedEffect(Unit) {
        dummyScope.launch {
            delay(500L)
            communityViewState.value = CommunityViewState.success()
        }
    }

    Scaffold(
        topBar = {},
        bottomBar = {},
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
                // 기본 M3 16dp에 각각 4dp 추가
                modifier = Modifier.padding(bottom = 4.dp, end = 4.dp),
                onClick = {},
            ) {
                Image(
                    painter = painterResource(R.drawable.pencil),
                    modifier = Modifier.size(24.dp),
                    contentDescription = "write note",
                )
            }
        },
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        when (communityViewState.value.status) {
            CommunityViewStatus.INITIAL,
            CommunityViewStatus.LOADING -> {
                // TODO(@이대근): 추후 로딩을 스켈레톤 등으로 고도화해야 합니다. 2025.08.17.
                Box(modifier = Modifier.padding(paddingValues = innerPadding).fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(alignment = Alignment.Center)
                    )
                }
            }

            CommunityViewStatus.SUCCESS_LOAD -> {
                LazyColumn(modifier = Modifier.padding(paddingValues = innerPadding)) {
                    item {
                        // 상단 아티스트 뷰
                        Box(modifier = Modifier.fillMaxWidth().height(310.dp)) {
                            AsyncImage(
                                model = communityViewState.value.artist.imageUrl,
                                contentDescription =
                                    "${communityViewState.value.artist.imageUrl}'s image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                            )
                            Column(
                                modifier =
                                    Modifier.align(alignment = Alignment.BottomCenter)
                                        .fillMaxWidth()
                                        .padding(start = 20.dp, end = 20.dp, bottom = 28.dp),
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = "${communityViewState.value.artist.name} 레코드",
                                    style =
                                        MaterialTheme.typography.headlineLarge.copy(
                                            color = LightGray01
                                        ),
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                // TODO(@이대근): 버튼을 디자인시스템 컴포넌트화해야 하는 지 확인 2025.08.17.
                                Button(
                                    shape = RoundedCornerShape(8.dp), // corner radius 추가
                                    colors =
                                        ButtonColors(
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
                                            painter =
                                                painterResource(
                                                    if (communityViewState.value.artist.isLike)
                                                        R.drawable.heart_light_active
                                                    else R.drawable.heart_light_inactive
                                                ),
                                            tint = LightGray03,
                                            contentDescription =
                                                "are you like this artist: ${communityViewState.value.artist.isLike}",
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "관심 아티스트",
                                            style =
                                                MaterialTheme.typography.labelMedium.copy(
                                                    color = LightGray00
                                                ),
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        // 노트 설정 뷰
                        Row(
                            modifier =
                                Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = "노트",
                                style =
                                    MaterialTheme.typography.headlineSmall.copy(color = LightGray09),
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(R.drawable.check_icon),
                                    tint =
                                        if (communityViewState.value.isViewNoteOnlyLyrics)
                                            MaterialTheme.colorScheme.primary
                                        else LightGray03,
                                    contentDescription = "",
                                )
                                Text(
                                    text = "가사 포함된 노트만 보기",
                                    style =
                                        MaterialTheme.typography.labelMedium.copy(
                                            color =
                                                if (communityViewState.value.isViewNoteOnlyLyrics)
                                                    MaterialTheme.colorScheme.primary
                                                else LightGray03
                                        ),
                                )
                            }
                        }
                    }

                    items(items = communityViewState.value.noteState.notes) { note ->
                        NoteComponent(noteData = note)
                    }
                }
            }
            CommunityViewStatus.ERROR -> TODO()
        }
    }
}

@Preview
@Composable
private fun CommunityViewPreview() {
    FeelinTheme { CommunityView() }
}
