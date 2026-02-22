package com.lyrics.feelin.presentation.view.onboarding.favoriteartist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.component.FeelinSearchInputField
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithClose
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.component.artist.ArtistBubbleComponent
import com.lyrics.feelin.presentation.view.component.artist.ArtistBubbleComponentData

private const val GRID_COL_MAX_ELEMENTS = 3

@Composable
fun OnboardingFavoriteArtistScreen(
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEnableComplete by rememberSaveable { mutableStateOf(false) }

    val searchState = rememberTextFieldState()

    // TODO(@이대근): view state 클래스로 분리 2026.02.22.
    val artists: List<FavoriteArtistData> = listOf(
        FavoriteArtistData(
            id = 1,
            name = "검정치마",
            imageUrl = "https://i.scdn.co/image/ab6761610000e5eb8609536d21beed6769d09d7f"
        ),
        FavoriteArtistData(
            id = 1,
            name = "검정치마",
            imageUrl = "https://i.scdn.co/image/ab6761610000e5eb8609536d21beed6769d09d7f"
        ),
        FavoriteArtistData(
            id = 1,
            name = "검정치마",
            imageUrl = "https://i.scdn.co/image/ab6761610000e5eb8609536d21beed6769d09d7f"
        ),
        FavoriteArtistData(
            id = 1,
            name = "검정치마",
            imageUrl = "https://i.scdn.co/image/ab6761610000e5eb8609536d21beed6769d09d7f"
        ),
    )

    FeelinTheme(darkTheme = false) {
        val feelinColors = LocalFeelinColors.current

        Box(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
                    .fillMaxSize()
                    .background(color = feelinColors.gray00)
            ) {
                FeelinTopAppBarWithClose(
                    title = "",
                    onCloseClick = onCloseClick,
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
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(28.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(GRID_COL_MAX_ELEMENTS),
                    // 스크롤 끝까지 내렸을 때 마지막 항목이 버튼에 가려지지 않게 하단 패딩 부여
                    contentPadding = PaddingValues(bottom = 100.dp, start = 20.dp, end = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(artists) {
                        ArtistBubbleComponent(
                            state = ArtistBubbleComponentData.InitialSelectArtistType(
                                name = it.name,
                                imageUrl = it.imageUrl,
                                isSelected = false,
                            ),
                            modifier = Modifier.clickable(enabled = true, onClick = {
                                // TODO(@이대근): 클릭해 아티스트 선택하도록 수정 2026.02.23.
                            })
                        )
                    }
                }
            }

            Button(
                onClick = {},
                enabled = isEnableComplete,
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

private data class FavoriteArtistData(
    val id: Int,
    val name: String,
    val imageUrl: String,
)

@Preview(name = "온보딩 선호 아티스트 화면", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun OnboardingFavoriteArtistScreenPreview() {
    OnboardingFavoriteArtistScreen(
        onCloseClick = {}
    )
}
