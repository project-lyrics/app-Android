package com.lyrics.feelin.presentation.view.onboarding

import com.lyrics.feelin.core.domain.model.ProfileType
import com.lyrics.feelin.core.domain.model.SignUpTerm

data class OnboardingState(
    val termAgreements: Map<SignUpTerm, Boolean> = SignUpTerm.entries.associateWith { false },
    val gender: String? = null,
    val birthYear: Int? = null,
    val nickname: String = "",
    val profileType: ProfileType = ProfileType.SHORT_HAIR,
) {
    val isStartEnabled: Boolean
        get() = SignUpTerm.entries
            .filter { it.required }
            .all { termAgreements[it] == true }
}

sealed interface OnboardingUiState {
    data object Idle : OnboardingUiState

    data object SigningUp : OnboardingUiState

    data object SignUpSuccess : OnboardingUiState

    data class Error(
        val title: String,
        val description: String,
        val kind: Kind,
    ) : OnboardingUiState

    sealed interface Kind {
        data object MissingRequiredTerms : Kind

        data object General : Kind
    }
}
