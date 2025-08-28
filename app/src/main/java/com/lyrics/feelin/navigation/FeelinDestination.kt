package com.lyrics.feelin.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class FeelinDestination(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : FeelinDestination(
        route = "home",
        title = "홈",
        icon = Icons.Default.Home
    )

    object NoteSearch : FeelinDestination(
        route = "note_search",
        title = "노트 검색",
        icon = Icons.Default.Search
    )

    object MyPage : FeelinDestination(
        route = "my_page",
        title = "마이페이지",
        icon = Icons.Default.Person
    )
}
