package com.lyrics.feelin.presentation.view.component.note

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lyrics.feelin.R
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LightGray09
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.component.profile.ProfileComponent
import com.lyrics.feelin.util.compareNowToUser

@Composable
fun NoteComponent(noteData: NoteComponentData, modifier: Modifier = Modifier) {
    val feelinColors = LocalFeelinColors.current

    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ProfileComponent(type = noteData.publisher.profileCharacterType, size = 32)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = noteData.publisher.nickname,
                    style = FeelinTypography.title3.copy(color = feelinColors.gray09),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = noteData.createdAt.compareNowToUser(),
                    style = FeelinTypography.caption2.copy(color = feelinColors.gray03),
                )
            }
            Image(
                painter = painterResource(R.drawable.meetball_light),
                contentDescription = "${noteData.song.name} menu",
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(feelinColors.gray03),
            )
        }
        Text(
            text = noteData.content,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            style = FeelinTypography.body3.copy(color = feelinColors.gray09),
            modifier = Modifier.padding(vertical = 16.dp),
        )
        if (noteData.lyrics != null) {
            Box(
                modifier = Modifier.fillMaxWidth().height(132.dp).padding(bottom = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                LyricsBackground(background = noteData.lyrics.background)
                Text(
                    text = noteData.lyrics.content,
                    style =
                    FeelinTypography.body1.copy(
                        color = LightGray09, // 가사 텍스트는 다크모드 미 적용입니다.
                        textAlign = TextAlign.Center,
                    ),
                )
            }
        }
        HorizontalDivider(color = feelinColors.gray01, thickness = 1.dp)
        Row(
            modifier = Modifier.fillMaxWidth().height(64.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = noteData.song.imageUrl,
                    modifier = Modifier.size(40.dp).clip(shape = RoundedCornerShape(4.dp)),
                    contentDescription = "${noteData.song.name}'s album art",
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = noteData.song.name,
                        style = FeelinTypography.body2.copy(color = feelinColors.gray08)
                    )
                    Text(
                        text = noteData.song.artist.name,
                        style = FeelinTypography.caption1.copy(color = feelinColors.gray04),
                    )
                }
            }
            Image(
                painter = painterResource(R.drawable.play),
                contentDescription = "${noteData.song.name} play",
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(feelinColors.gray03),
            )
        }
        HorizontalDivider(color = feelinColors.gray01, thickness = 1.dp)
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Image(
                    painter =
                    painterResource(
                        if (noteData.isLiked) {
                            R.drawable.heart_light_active
                        } else {
                            R.drawable.heart_light_inactive
                        }
                    ),
                    contentDescription = "note like icon",
                    modifier = Modifier.size(24.dp).padding(end = 4.dp),
                    colorFilter = ColorFilter.tint(feelinColors.gray03).takeUnless { noteData.isLiked },
                )
                Text(
                    text = noteData.likesCount.toString(),
                    style = FeelinTypography.body3.copy(color = feelinColors.gray03),
                )
                Spacer(modifier = Modifier.width(16.dp))
                Image(
                    painter = painterResource(R.drawable.chatcircle_light),
                    contentDescription = "note comment icon",
                    modifier = Modifier.size(24.dp).padding(end = 4.dp),
                    colorFilter = ColorFilter.tint(feelinColors.gray03)
                )
                Text(
                    text = noteData.commentsCount.toString(),
                    style = FeelinTypography.body3.copy(color = feelinColors.gray03),
                )
            }
            Image(
                painter =
                painterResource(
                    if (noteData.isBookmarked) {
                        R.drawable.bookmark_light_active
                    } else {
                        R.drawable.bookmark_light_inactive
                    }
                ),
                contentDescription =
                "note is ${if (noteData.isBookmarked) "" else "not "}bookmarked",
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(feelinColors.gray03).takeUnless { noteData.isLiked }
            )
        }
    }
}

@Composable
private fun LyricsBackground(background: LyricsBackground, modifier: Modifier = Modifier) {
    Image(
        painterResource(background.toImage()),
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize().clip(shape = RoundedCornerShape(size = 4.dp)),
        contentDescription = null,
    )
}

private fun LyricsBackground.toImage(): Int {
    return when (this) {
        LyricsBackground.DEFAULT -> R.drawable.lyrics_background_img00
        LyricsBackground.SKYBLUE -> R.drawable.lyrics_background_img01
        LyricsBackground.BLUE -> R.drawable.lyrics_background_img02
        LyricsBackground.LAVENDER -> R.drawable.lyrics_background_img03
        LyricsBackground.MINT -> R.drawable.lyrics_background_img04
        LyricsBackground.BLACK -> R.drawable.lyrics_background_img05
        LyricsBackground.BEIGE -> R.drawable.lyrics_background_img06
        LyricsBackground.PINKGREEN -> R.drawable.lyrics_background_img07
        LyricsBackground.RED -> R.drawable.lyrics_background_img08
        LyricsBackground.WHITE -> R.drawable.lyrics_background_img09
        LyricsBackground.RAINBOW -> R.drawable.lyrics_background_img10
    }
}

@Preview(name = "Note Component - Light", showBackground = true)
@Preview(
    name = "Note Component - Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun NoteComponentPreview() {
    FeelinTheme {
        val feelinColor = LocalFeelinColors.current
        Column(
            modifier = Modifier.background(color = feelinColor.backgroundPrimary)
        ) {
            NoteComponent(noteData = NoteComponentData.sample())
            NoteComponent(noteData = NoteComponentData.sampleNoLyrics())
        }
    }
}
