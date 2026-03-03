package com.lyrics.feelin.presentation.view.onboarding.favoriteartist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lyrics.feelin.core.designsystem.component.FeelinModalDialog
import com.lyrics.feelin.core.designsystem.component.FeelinSearchInputField
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithClose
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LightBrandSecondary
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.component.artist.ArtistBubbleComponent
import com.lyrics.feelin.presentation.view.component.artist.ArtistBubbleComponentData
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

private const val GRID_COL_MAX_ELEMENTS = 3
private const val SEARCH_DEBOUNCE_MS = 1000L

@Composable
fun OnboardingFavoriteArtistScreen(
    onCloseScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingFavoriteArtistViewModel = viewModel(),
) {
    val viewState by viewModel.viewState.collectAsState()
    val searchState = rememberTextFieldState()

    val keyboardController = LocalSoftwareKeyboardController.current

    var isOpenCloseDialog by remember { mutableStateOf(false) }
    var isOpenArtistLimitDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadArtists()
    }

    @OptIn(FlowPreview::class)
    LaunchedEffect(Unit) {
        snapshotFlow { searchState.text.toString() }
            .debounce(SEARCH_DEBOUNCE_MS)
            .collectLatest { keyword ->
                viewModel.searchArtists(keyword)
            }
    }

    FeelinTheme(darkTheme = false) {
        val feelinColors = LocalFeelinColors.current

        if (isOpenCloseDialog) {
            FeelinModalDialog(
                title = "선택한 정보를 저장하지 않고\n나가시겠어요?",
                description = null,
                confirmButtonText = "나가기",
                dismissButtonText = "취소",
                onDismissButtonClick = { isOpenCloseDialog = false },
                onConfirmButtonClick = {
                    isOpenCloseDialog = false
                    onCloseScreen.invoke()
                }
            )
        }

        if (isOpenArtistLimitDialog) {
            FeelinModalDialog(
                title = "아티스트는 최대 30명까지\n선택할 수 있어요.",
                description = null,
                isDismissButtonEnable = false,
                confirmButtonText = "확인",
                onConfirmButtonClick = {
                    isOpenArtistLimitDialog = false
                }
            )
        }

        Box(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
                    .fillMaxSize()
                    .background(color = feelinColors.gray00)
            ) {
                FeelinTopAppBarWithClose(
                    title = "",
                    onCloseClick = {
                        isOpenCloseDialog = true
                    },
                    showDivider = false,
                )
                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = """
                    좋아하는 아티스트를
                    모두 선택해주세요
                    """.trimIndent(),
                    style = FeelinTypography.heading1.copy(color = feelinColors.gray08),
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Text(
                    text = "곡과 가사를 공유할 수 있는 공간이 생성돼요",
                    style = FeelinTypography.body3.copy(color = feelinColors.gray04),
                    modifier = Modifier.padding(top = 8.dp, start = 20.dp, end = 20.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                FeelinSearchInputField(
                    state = searchState,
                    placeholder = "아티스트 검색",
                    onSearchClick = { keyboardController?.hide() },
                    onClearClick = { searchState.clearText() },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(28.dp))

                when (viewState.status) {
                    OnboardingFavoriteArtistStatus.LOADING -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    OnboardingFavoriteArtistStatus.SUCCESS -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(GRID_COL_MAX_ELEMENTS),
                            // 스크롤 끝까지 내렸을 때 마지막 항목이 버튼에 가려지지 않게 하단 패딩 부여
                            contentPadding = PaddingValues(bottom = 100.dp, start = 20.dp, end = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(viewState.artists) { artist ->
                                ArtistBubbleComponent(
                                    state = ArtistBubbleComponentData.InitialSelectArtistType(
                                        name = artist.name,
                                        imageUrl = artist.imageUrl,
                                        isSelected = artist.isSelected,
                                    ),
                                    modifier = Modifier.clickable(
                                        enabled = true,
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                    ) {
                                        if (viewModel.toggleArtistSelection(artist.id)) {
                                            isOpenArtistLimitDialog = true
                                        }
                                    }
                                )
                            }
                        }
                    }

                    else -> {
                        // TODO(@이대근): 에러 다이얼로그 처리 2026.02.25.
                        Text("문제가 발생했습니다.")
                    }
                }
            }

            // 하단 그라데이션 오버레이
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(117.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                LightBrandSecondary.copy(alpha = 0.0f),
                                LightBrandSecondary.copy(alpha = 0.2f)
                            ),
                        )
                    )
            )

            Button(
                onClick = {
                    onCloseScreen.invoke()
                },
                enabled = viewState.isEnableComplete,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 24.dp, start = 20.dp, end = 20.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = feelinColors.systemActivate,
                    disabledContainerColor = feelinColors.systemDisable
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "완료",
                    style = FeelinTypography.title2,
                    color = feelinColors.gray00
                )
            }
        }
    }
}

@Preview(name = "온보딩 선호 아티스트 화면", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun OnboardingFavoriteArtistScreenPreview() {
    OnboardingFavoriteArtistScreen(
        onCloseScreen = {}
    )
}
