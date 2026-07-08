package com.lyrics.feelin.presentation.view.mypage.blockedusers

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class BlockedUsersViewModel @Inject constructor() : ViewModel() {
    private val _uiState: MutableStateFlow<BlockedUsersUiState> = MutableStateFlow(
        BlockedUsersUiState(
            blockedUsers = listOf(
                BlockedUserListItemData(userId = 1, nickname = "username01", profileImageUrl = null),
                BlockedUserListItemData(userId = 2, nickname = "username02", profileImageUrl = null),
                BlockedUserListItemData(userId = 3, nickname = "username03", profileImageUrl = null),
            )
        )
    )
    val uiState: StateFlow<BlockedUsersUiState> = _uiState.asStateFlow()

    fun unblockUser(userId: Long) {
        _uiState.value = _uiState.value.copy(
            blockedUsers = _uiState.value.blockedUsers.filter { it.userId != userId }
        )
    }
}
