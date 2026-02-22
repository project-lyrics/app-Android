package com.lyrics.feelin.presentation.view.component.artist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lyrics.feelin.core.designsystem.icon.FindSquareIcon
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LightGray08
import com.lyrics.feelin.presentation.designsystem.theme.LightSystemActivate
import com.lyrics.feelin.presentation.designsystem.theme.LightSystemDisable
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

@Composable
fun ArtistBubbleComponent(state: ArtistBubbleComponentData, modifier: Modifier = Modifier) {
    val feelinColors = LocalFeelinColors.current

    val imgSize = when (state) {
        is ArtistBubbleComponentData.HomeFavoriteSearchType,
        is ArtistBubbleComponentData.HomeFavoriteArtistType -> 64.dp
        else -> 96.dp
    }

    val borderColor = when (state) {
        is ArtistBubbleComponentData.InitialSelectArtistType if state.isSelected -> {
            LightSystemActivate
        }
        is ArtistBubbleComponentData.InitialSelectArtistType -> {
            LightSystemDisable
        }
        else -> {
            Color.Transparent
        }
    }

    val textStyle = when (state) {
        is ArtistBubbleComponentData.InitialSelectArtistType,
        is ArtistBubbleComponentData.FavoriteArtistFindType -> FeelinTypography.body2
        is ArtistBubbleComponentData.HomeFavoriteArtistType,
        is ArtistBubbleComponentData.HomeFavoriteSearchType -> FeelinTypography.caption2
    }.copy(
        color = if (state is ArtistBubbleComponentData.InitialSelectArtistType) LightGray08 else feelinColors.gray08
    )

    ArtistProfileLayout(
        modifier = modifier,
        mainContent = {
            if (state is ArtistBubbleComponentData.HomeFavoriteSearchType) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(shape = CircleShape)
                        .background(feelinColors.backgroundTertiary)
                ) {
                    Icon(
                        imageVector = FindSquareIcon,
                        contentDescription = "find favorite artist",
                        tint = feelinColors.brandPrimary,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(28.dp)
                    )
                }
            } else {
                val imageUrl = when (state) {
                    is ArtistBubbleComponentData.InitialSelectArtistType -> state.imageUrl
                    is ArtistBubbleComponentData.FavoriteArtistFindType -> state.imageUrl
                    is ArtistBubbleComponentData.HomeFavoriteArtistType -> state.imageUrl
                    else -> ""
                }

                ArtistImageLayout(
                    imageUrl = imageUrl,
                    imgSize = imgSize,
                    borderColor = borderColor,
                    hasBorder = state is ArtistBubbleComponentData.InitialSelectArtistType
                )
            }
        },
        trailingContent = {
            ArtistTextLayout(
                text = state.name,
                textStyle = textStyle
            )
        }
    )
}

@Composable
private fun ArtistProfileLayout(
    mainContent: @Composable () -> Unit,
    trailingContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        mainContent()
        Spacer(modifier = Modifier.height(8.dp))
        trailingContent()
    }
}

@Composable
private fun ArtistImageLayout(
    imageUrl: String,
    imgSize: Dp,
    borderColor: Color,
    hasBorder: Boolean,
    modifier: Modifier = Modifier
) {
    val conditionalModifier = if (hasBorder) {
        Modifier
            .border(width = 2.dp, color = borderColor, shape = CircleShape)
            .padding(6.dp)
    } else {
        Modifier
    }

    AsyncImage(
        model = imageUrl,
        contentDescription = "artist image",
        modifier = modifier
            .then(conditionalModifier)
            .size(imgSize)
            .clip(shape = CircleShape)
    )
}

@Composable
private fun ArtistTextLayout(
    text: String,
    textStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    Text(text, style = textStyle, modifier = modifier)
}

@Preview(name = "홈 화면에서 표시하는 관심 아티스트 버블", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun HomeFavoriteArtistTypePreview() {
    val data = ArtistBubbleComponentData.HomeFavoriteArtistType(
        name = "검정치마",
        imageUrl = "https://i.scdn.co/image/ab6761610000e5eb8609536d21beed6769d09d7f",
    )
    FeelinTheme {
        ArtistBubbleComponent(
            state = data,
            modifier = Modifier
        )
    }
}

@Preview(name = "홈 화면에서 관심 아티스트 찾아보기 버블", showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun HomeFavoriteSearchTypePreview() {
    val data = ArtistBubbleComponentData.HomeFavoriteSearchType(
        name = "찾아보기",
    )
    FeelinTheme {
        ArtistBubbleComponent(
            state = data,
            modifier = Modifier
        )
    }
}

@Preview(name = "관심 아티스트 찾아보기, 전체보기 화면에서 표시", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun FavoriteArtistTypePreview() {
    val data = ArtistBubbleComponentData.FavoriteArtistFindType(
        name = "검정치마",
        imageUrl = "https://i.scdn.co/image/ab6761610000e5eb8609536d21beed6769d09d7f",
    )
    FeelinTheme {
        ArtistBubbleComponent(
            state = data,
            modifier = Modifier
        )
    }
}

@Preview(name = "첫 관심 아티스트 선택 버블(선택 X)", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun InitialSelectArtistTypeNotSelectedPreview() {
    val data = ArtistBubbleComponentData.InitialSelectArtistType(
        name = "검정치마",
        imageUrl = "https://i.scdn.co/image/ab6761610000e5eb8609536d21beed6769d09d7f",
        isSelected = false
    )
    FeelinTheme {
        ArtistBubbleComponent(
            state = data,
            modifier = Modifier
        )
    }
}

@Preview(name = "첫 관심 아티스트 선택 버블(선택 O)", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun InitialSelectArtistTypeSelectedPreview() {
    val data = ArtistBubbleComponentData.InitialSelectArtistType(
        name = "검정치마",
        imageUrl = "https://i.scdn.co/image/ab6761610000e5eb8609536d21beed6769d09d7f",
        isSelected = true
    )
    FeelinTheme {
        ArtistBubbleComponent(
            state = data,
            modifier = Modifier
        )
    }
}
