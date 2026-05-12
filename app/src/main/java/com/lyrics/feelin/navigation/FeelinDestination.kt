package com.lyrics.feelin.navigation

import android.net.Uri

sealed class FeelinDestination(
    val route: String,
) {
    companion object {
        private const val LOGIN_ROUTE = "login"
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
        const val DialogReasonArgument = "dialogReason"
        const val AutoLoginFailedDialogReason = "auto_login_failed"

        val routeWithDialogReason = "$LOGIN_ROUTE/{$DialogReasonArgument}"

        fun createRoute(dialogReason: String): String {
            return "$LOGIN_ROUTE/${Uri.encode(dialogReason)}"
        }
    }
    object OnboardingTerms : FeelinDestination(route = "onboarding_terms")
    object OnboardingGenderAge : FeelinDestination(route = "onboarding_gender_age")
    object OnboardingProfile : FeelinDestination(route = "onboarding_profile")
    object OnboardingWelcome : FeelinDestination(route = "onboarding_welcome")

    // Main Flow (with Bottom Navigation)
    object Home : FeelinDestination(route = "home")
    object NoteSearch : FeelinDestination(route = "note_search")
    object NoteSearchResult : FeelinDestination(route = "note_search_result")
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
