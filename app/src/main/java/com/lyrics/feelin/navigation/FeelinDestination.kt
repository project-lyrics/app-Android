package com.lyrics.feelin.navigation

sealed class FeelinDestination(
    val route: String,
) {
    // Navigation Graph Routes
    object OnboardingGraph : FeelinDestination(route = "onboarding_graph")
    object MainGraph : FeelinDestination(route = "main_graph")
    object HomeGraph : FeelinDestination(route = "home_graph")
    object NoteSearchGraph : FeelinDestination(route = "note_search_graph")
    object MyPageGraph : FeelinDestination(route = "my_page_graph")

    // Onboarding Flow
    object Login : FeelinDestination(route = "login")
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
}
