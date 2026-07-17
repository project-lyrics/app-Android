package com.lyrics.feelin.presentation.view.notification.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lyrics.feelin.R
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.notification.NotificationItemUiModel

private const val THUMBNAIL_ALPHA_READ = 0.5f

@Composable
fun NotificationListItem(
    item: NotificationItemUiModel,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val feelinColors = LocalFeelinColors.current

    val messageColor = if (item.isRead) feelinColors.gray04 else feelinColors.gray09
    val timeColor = if (item.isRead) feelinColors.gray02 else feelinColors.gray03
    val thumbnailAlpha = if (item.isRead) THUMBNAIL_ALPHA_READ else 1f

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(item.id) }
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                // MARK(@이대근): placeholder, error 다른 것으로 할 수 있는지 확인 2026.07.15.
                placeholder = painterResource(id = R.drawable.lyrics_background_img00),
                error = painterResource(id = R.drawable.lyrics_background_img00),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(thumbnailAlpha)
                    .clip(CircleShape)
                    .background(feelinColors.backgroundTertiary)
            )

            if (!item.isRead) {
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(color = feelinColors.point, shape = CircleShape)
                        .align(Alignment.TopEnd)
                        .testTag("notificationUnreadIndicator")
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.message,
                style = FeelinTypography.body2,
                color = messageColor
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.timeLabel,
                style = FeelinTypography.caption2,
                color = timeColor
            )
        }
    }
}

@Preview(name = "Notification List Item - Light", showBackground = true)
@Preview(
    name = "Notification List Item - Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun NotificationListItemPreview() {
    FeelinTheme {
        val colors = LocalFeelinColors.current
        Column(
            modifier = Modifier
                .background(colors.backgroundPrimary)
        ) {
            NotificationListItem(
                item = NotificationItemUiModel(
                    id = 1L,
                    message = "누군가 내 노트에 좋아요를 남겼어요.",
                    timeLabel = "방금 전",
                    imageUrl = "https://picsum.photos/seed/feelin-preview-unread/72/72",
                    isRead = false
                ),
                onClick = {}
            )
            NotificationListItem(
                item = NotificationItemUiModel(
                    id = 2L,
                    message = "누군가 내 노트에 좋아요를 남겼어요.",
                    timeLabel = "3시간 전",
                    imageUrl = "https://picsum.photos/seed/feelin-preview-read/72/72",
                    isRead = true
                ),
                onClick = {}
            )
            val longMsg = "신고하신 노트가 삭제 처리되었어요.\n\n" +
                "해당 노트는 운영 정책 위반으로 인해 삭제되었으며, " +
                "더 이상 앱 내에서 노출되지 않습니다. 신고 접수해 주셔서 감사합니다."
            NotificationListItem(
                item = NotificationItemUiModel(
                    id = 3L,
                    message = longMsg,
                    timeLabel = "1일 전",
                    imageUrl = "https://picsum.photos/seed/feelin-preview-report/72/72",
                    isRead = false
                ),
                onClick = {}
            )
        }
    }
}
