package com.lyrics.feelin.navigation

sealed class FeelinDestination(
    val route: String,
) {
    object Home : FeelinDestination(route = "home")

    object NoteSearch : FeelinDestination(route = "note_search")

    object MyPage : FeelinDestination(route = "my_page")

    object Login : FeelinDestination(route = "login")
}
