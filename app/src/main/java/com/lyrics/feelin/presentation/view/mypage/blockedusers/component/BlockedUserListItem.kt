package com.lyrics.feelin.presentation.view.mypage.blockedusers.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lyrics.feelin.core.designsystem.component.FeelinActionButton
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.mypage.blockedusers.BlockedUserListItemData

@Composable
fun BlockedUserListItem(
    data: BlockedUserListItemData,
    onUnblockClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalFeelinColors.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.backgroundPrimary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = data.profileImageUrl,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colors.backgroundTertiary),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = data.nickname,
                style = FeelinTypography.title2.copy(color = colors.gray09),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            FeelinActionButton(text = "차단 해제", onClick = onUnblockClick)
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = colors.backgroundTertiary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BlockedUserListItemPreviewLight() {
    FeelinTheme {
        BlockedUserListItem(
            data = BlockedUserListItemData(
                userId = 1L,
                nickname = "차단된 사용자 닉네임",
                profileImageUrl = null
            ),
            onUnblockClick = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BlockedUserListItemPreviewDark() {
    FeelinTheme {
        BlockedUserListItem(
            data = BlockedUserListItemData(
                userId = 1L,
                nickname = "차단된 사용자 닉네임",
                profileImageUrl = null
            ),
            onUnblockClick = {}
        )
    }
}
