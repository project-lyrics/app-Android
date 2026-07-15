package com.lyrics.feelin.presentation.view.notification

import androidx.compose.runtime.Immutable

enum class NotificationTab(val label: String) {
    MY_NEWS("내 소식"),
    ALL("전체"),
}

@Immutable
data class NotificationItemUiModel(
    val id: Long,
    val message: String,
    val timeLabel: String,
    val imageUrl: String?,
    val isRead: Boolean,
)

@Immutable
data class NotificationUiState(
    val selectedTab: NotificationTab = NotificationTab.MY_NEWS,
    val items: List<NotificationItemUiModel> = emptyList(),
    val isDeletedNoteDialogVisible: Boolean = false,
) {
    companion object {
        fun populatedSample(): NotificationUiState {
            return NotificationUiState(
                selectedTab = NotificationTab.MY_NEWS,
                items = listOf(
                    NotificationItemUiModel(
                        id = 1L,
                        message = "새 노트에 좋아요가 달렸어요.",
                        timeLabel = "방금 전",
                        imageUrl = "https://picsum.photos/seed/feelin-notification-1/72/72",
                        isRead = false,
                    ),
                    NotificationItemUiModel(
                        id = 2L,
                        message = "친구가 당신의 노트에 댓글을 남겼어요.",
                        timeLabel = "10분 전",
                        imageUrl = "https://picsum.photos/seed/feelin-notification-2/72/72",
                        isRead = false,
                    ),
                    NotificationItemUiModel(
                        id = 3L,
                        message = "즐겨찾는 아티스트의 새 글이 올라왔어요.",
                        timeLabel = "1시간 전",
                        imageUrl = "https://picsum.photos/seed/feelin-notification-3/72/72",
                        isRead = true,
                    ),
                    NotificationItemUiModel(
                        id = 4L,
                        message = "내가 저장한 노트의 반응을 확인해보세요.",
                        timeLabel = "어제",
                        imageUrl = "https://picsum.photos/seed/feelin-notification-4/72/72",
                        isRead = true,
                    ),
                    NotificationItemUiModel(
                        id = 5L,
                        message = "노트에 새로운 공감이 추가되었어요.",
                        timeLabel = "어제",
                        imageUrl = "https://picsum.photos/seed/feelin-notification-5/72/72",
                        isRead = true,
                    ),
                    NotificationItemUiModel(
                        id = 6L,
                        message = "팔로우 중인 사용자가 새 노트를 남겼어요.",
                        timeLabel = "2일 전",
                        imageUrl = "https://picsum.photos/seed/feelin-notification-6/72/72",
                        isRead = true,
                    ),
                    NotificationItemUiModel(
                        id = 7L,
                        message = "이전 알림을 다시 확인해보세요.",
                        timeLabel = "2일 전",
                        imageUrl = "https://picsum.photos/seed/feelin-notification-7/72/72",
                        isRead = true,
                    ),
                ),
                isDeletedNoteDialogVisible = false,
            )
        }

        fun emptySample(): NotificationUiState {
            return NotificationUiState(
                selectedTab = NotificationTab.ALL,
                items = emptyList(),
                isDeletedNoteDialogVisible = false,
            )
        }

        fun reportSample(): NotificationUiState {
            return NotificationUiState(
                selectedTab = NotificationTab.ALL,
                items = listOf(
                    NotificationItemUiModel(
                        id = 101L,
                        message = "신고가 접수된 노트예요.\n반복되는 비방 표현이 포함되어 있어요.\n운영팀 확인 후 삭제되었어요.",
                        timeLabel = "3시간 전",
                        imageUrl = "https://picsum.photos/seed/feelin-report-1/72/72",
                        isRead = true,
                    ),
                    NotificationItemUiModel(
                        id = 102L,
                        message = "최근 활동 알림",
                        timeLabel = "3시간 전",
                        imageUrl = "https://picsum.photos/seed/feelin-report-2/72/72",
                        isRead = true,
                    ),
                ),
                isDeletedNoteDialogVisible = false,
            )
        }

        fun deletedNoteDialogSample(): NotificationUiState {
            return populatedSample().copy(isDeletedNoteDialogVisible = true)
        }
    }
}
