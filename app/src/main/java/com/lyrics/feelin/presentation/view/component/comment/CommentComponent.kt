package com.lyrics.feelin.presentation.view.component.comment

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.icon.MeatballIcon
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.component.profile.ProfileComponent
import com.lyrics.feelin.util.compareNowToUser

@Composable
fun CommentComponent(
    commentData: CommentComponentData,
    modifier: Modifier = Modifier,
    onMoreClick: (CommentComponentData) -> Unit = {},
) {
    val feelinColors = LocalFeelinColors.current
    val moreInteractionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (commentData.isMine) feelinColors.gray01 else feelinColors.backgroundPrimary,
            )
            .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ProfileComponent(type = commentData.writer.profileCharacterType, size = 32)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = commentData.writer.nickname,
                    style = FeelinTypography.title3.copy(color = feelinColors.gray09),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = commentData.createdAt.compareNowToUser(),
                    style = FeelinTypography.caption2.copy(color = feelinColors.gray03),
                )
            }
            Icon(
                imageVector = MeatballIcon,
                contentDescription = "comment menu",
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(
                        interactionSource = moreInteractionSource,
                        indication = null,
                        onClick = { onMoreClick(commentData) },
                    ),
                tint = feelinColors.gray03,
            )
        }
        Text(
            text = commentData.content,
            style = FeelinTypography.body3.copy(color = feelinColors.gray09),
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
        )
    }
}

@Preview(name = "Comment Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Preview(name = "Comment Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CommentComponentPreview() {
    FeelinTheme {
        CommentComponent(commentData = CommentComponentData.sample())
    }
}

@Preview(name = "My Comment Light", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Preview(name = "My Comment Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MyCommentComponentPreview() {
    FeelinTheme {
        CommentComponent(commentData = CommentComponentData.sample(isMine = true))
    }
}
