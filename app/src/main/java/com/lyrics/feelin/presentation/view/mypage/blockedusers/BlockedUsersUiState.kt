package com.lyrics.feelin.presentation.view.mypage.blockedusers

data class BlockedUsersUiState(
    val blockedUsers: List<BlockedUserListItemData> = emptyList()
) {
    companion object {
        fun initial(): BlockedUsersUiState = BlockedUsersUiState()
    }
}
