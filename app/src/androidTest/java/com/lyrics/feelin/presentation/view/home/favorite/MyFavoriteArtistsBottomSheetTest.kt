package com.lyrics.feelin.presentation.view.home.favorite

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.view.component.artist.ArtistBubbleComponentData
import org.junit.Rule
import org.junit.Test

class MyFavoriteArtistsBottomSheetTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun sheetDisplaysTitleAndArtistNames() {
        val artists = listOf(
            ArtistBubbleComponentData.HomeFavoriteArtistType(
                name = "실리카겔",
                imageUrl = "",
                id = 1L,
            ),
            ArtistBubbleComponentData.HomeFavoriteArtistType(
                name = "쏜애플",
                imageUrl = "",
                id = 2L,
            ),
        )

        composeRule.setContent {
            FeelinTheme {
                MyFavoriteArtistsBottomSheet(
                    artists = artists,
                    onArtistClick = {},
                    onDismissRequest = {},
                    viewModel = MyFavoriteArtistsViewModel(),
                )
            }
        }

        composeRule.onNodeWithText("나의 관심 아티스트").assertIsDisplayed()
        composeRule.onNodeWithText("실리카겔").assertIsDisplayed()
        composeRule.onNodeWithText("쏜애플").assertIsDisplayed()
    }
}
