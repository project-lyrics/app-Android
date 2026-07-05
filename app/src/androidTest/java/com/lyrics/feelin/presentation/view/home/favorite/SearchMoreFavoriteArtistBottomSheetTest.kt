package com.lyrics.feelin.presentation.view.home.favorite

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import org.junit.Rule
import org.junit.Test

class SearchMoreFavoriteArtistBottomSheetTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun sheetDisplaysTitleSubtitleAndSearchPlaceholder() {
        composeRule.setContent {
            FeelinTheme {
                SearchMoreFavoriteArtistBottomSheet(
                    onArtistClick = {},
                    onDismissRequest = {},
                    viewModel = SearchMoreFavoriteArtistViewModel(),
                )
            }
        }

        composeRule.onNodeWithText("새로운 관심 아티스트를 찾아보세요").assertIsDisplayed()
        composeRule.onNodeWithText("아티스트를 클릭하여 레코드에 입장할 수 있어요").assertIsDisplayed()
        composeRule.onNodeWithText("아티스트 검색").assertIsDisplayed()
    }
}
