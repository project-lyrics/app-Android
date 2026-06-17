package com.lyrics.feelin.navigation

import android.net.Uri

sealed class FeelinDestination(
    val route: String,
) {
    companion object {
        private const val LOGIN_ROUTE = "login"
        private const val NOTE_DETAIL_ROUTE = "note_detail"
        private const val NOTE_ID_ARGUMENT = "noteId"
        private const val ARTIST_ID_ARGUMENT = "artistId"
        private const val WEB_VIEW_URL_ARGUMENT = "url"
    }

    // Navigation Graph Routes
    object Splash : FeelinDestination(route = "splash")
    object OnboardingGraph : FeelinDestination(route = "onboarding_graph")
    object MainGraph : FeelinDestination(route = "main_graph")
    object HomeGraph : FeelinDestination(route = "home_graph")
    object NoteSearchGraph : FeelinDestination(route = "note_search_graph")
    object MyPageGraph : FeelinDestination(route = "my_page_graph")

    // Onboarding Flow
    object Login : FeelinDestination(route = LOGIN_ROUTE) {
        const val AUTO_LOGIN_FAILED_ERROR_CODE_ARGUMENT = "autoLoginFailedErrorCode"

        const val ROUTE_WITH_AUTO_LOGIN_FAILED_ERROR_CODE = "$LOGIN_ROUTE/{$AUTO_LOGIN_FAILED_ERROR_CODE_ARGUMENT}"

        fun createRoute(autoLoginFailedErrorCode: String): String {
            return "$LOGIN_ROUTE/${Uri.encode(autoLoginFailedErrorCode)}"
        }
    }
    object OnboardingTerms : FeelinDestination(route = "onboarding_terms")
    object OnboardingGenderAge : FeelinDestination(route = "onboarding_gender_age")
    object OnboardingProfile : FeelinDestination(route = "onboarding_profile")
    object OnboardingWelcome : FeelinDestination(route = "onboarding_welcome")

    // Main Flow (with Bottom Navigation)
    object Home : FeelinDestination(route = "home")
    object ArtistRecord : FeelinDestination(route = "artist_record/{$ARTIST_ID_ARGUMENT}") {
        const val ArtistIdArgument = ARTIST_ID_ARGUMENT

        fun createRoute(artistId: Long): String {
            return "artist_record/$artistId"
        }
    }
    object NoteSearch : FeelinDestination(route = "note_search")
    object NoteSearchResult : FeelinDestination(route = "note_search_result")
    object NoteFormCreate : FeelinDestination(route = "note_form/create")
    object NoteFormEdit : FeelinDestination(route = "note_form/edit/{$NOTE_ID_ARGUMENT}") {
        const val NoteIdArgument = NOTE_ID_ARGUMENT

        fun createRoute(noteId: Long): String {
            return "note_form/edit/$noteId"
        }
    }
    object NoteFormSearchSong : FeelinDestination(route = "note_form/search_song/{$ARTIST_ID_ARGUMENT}") {
        const val ArtistIdArgument = ARTIST_ID_ARGUMENT

        fun createRoute(artistId: Long): String {
            return "note_form/search_song/$artistId"
        }
    }
    object NoteDetail : FeelinDestination(route = "$NOTE_DETAIL_ROUTE/{$NOTE_ID_ARGUMENT}") {
        const val NoteIdArgument = NOTE_ID_ARGUMENT

        fun createRoute(noteId: Long): String {
            return "$NOTE_DETAIL_ROUTE/$noteId"
        }
    }
    object MyPage : FeelinDestination(route = "my_page")
    object Setting : FeelinDestination(route = "setting")
    object UserInfo : FeelinDestination(route = "user_info")

    object InternalWebView : FeelinDestination(
        route = "internal_webview?$WEB_VIEW_URL_ARGUMENT={$WEB_VIEW_URL_ARGUMENT}"
    ) {
        const val UrlArgument = WEB_VIEW_URL_ARGUMENT

        fun createRoute(url: String): String {
            return "internal_webview?$UrlArgument=${Uri.encode(url)}"
        }
    }
}
