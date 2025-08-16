package com.lyrics.feelin.presentation.view.component.note

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lyrics.feelin.R
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.LightGray01
import com.lyrics.feelin.presentation.designsystem.theme.LightGray03
import com.lyrics.feelin.presentation.designsystem.theme.LightGray04
import com.lyrics.feelin.presentation.designsystem.theme.LightGray09
import com.lyrics.feelin.presentation.view.component.profile.ProfileComponent
import com.lyrics.feelin.presentation.view.component.profile.ProfileType
import com.lyrics.feelin.util.compareNowToUser

// TODO(@이대근): 다크모드 대응 필요 2025.08.17.

@Composable
fun NoteComponent(noteData: NoteComponentData, modifier: Modifier = Modifier) {
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
                ProfileComponent(type = ProfileType.POOP_HAIR, size = 32)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = noteData.publisher.nickname,
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = noteData.createdAt.compareNowToUser(),
                    style = MaterialTheme.typography.labelMedium.copy(color = LightGray03),
                )
            }
            Image(
                painter = painterResource(R.drawable.meetball_light),
                contentDescription = "${noteData.song.name} menu",
                modifier = Modifier.size(24.dp),
            )
        }
        Text(
            noteData.content,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            style = MaterialTheme.typography.bodySmall.copy(color = LightGray09),
            modifier = Modifier.padding(vertical = 16.dp),
        )
        if (noteData.lyrics != null)
            Box(
                modifier = Modifier.width(350.dp).height(132.dp).padding(bottom = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                //                Image(noteData.lyrics.background, contentDescription = null) TODO
                Text(
                    noteData.lyrics.content,
                    style = MaterialTheme.typography.bodyLarge.copy(color = LightGray09),
                )
            }
        HorizontalDivider(color = LightGray01, thickness = 1.dp)
        Row(
            modifier = Modifier.fillMaxWidth().height(64.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = noteData.song.imageUrl,
                    modifier = Modifier.size(40.dp),
                    contentDescription = "${noteData.song.name}'s album art",
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = noteData.song.name, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = noteData.song.artist.name,
                        style = MaterialTheme.typography.labelSmall.copy(color = LightGray04),
                    )
                }
            }
            Image(
                painter = painterResource(R.drawable.play),
                contentDescription = "${noteData.song.name} play",
                modifier = Modifier.size(24.dp),
            )
        }
        HorizontalDivider(color = LightGray01, thickness = 1.dp)
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
                            if (noteData.isLiked) R.drawable.heart_light_active
                            else R.drawable.heart_light_inactive
                        ),
                    contentDescription = "note like icon",
                    modifier = Modifier.size(24.dp).padding(end = 4.dp),
                )
                Text(
                    text = noteData.likesCount.toString(),
                    style = MaterialTheme.typography.bodySmall.copy(color = LightGray03),
                )
                Spacer(modifier = Modifier.width(16.dp))
                Image(
                    painter = painterResource(R.drawable.chatcircle_light),
                    contentDescription = "note comment icon",
                    modifier = Modifier.size(24.dp).padding(end = 4.dp),
                )
                Text(
                    text = noteData.commentsCount.toString(),
                    style = MaterialTheme.typography.bodySmall.copy(color = LightGray03),
                )
            }
            Image(
                painter =
                    painterResource(
                        if (noteData.isBookmarked) R.drawable.bookmark_light_active
                        else R.drawable.bookmark_light_inactive
                    ),
                contentDescription =
                    "note is ${if(noteData.isBookmarked) "" else "not "}bookmarked",
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteComponentPreview() {
    FeelinTheme {
        Column {
            NoteComponent(noteData = NoteComponentData.sample())
            NoteComponent(noteData = NoteComponentData.sampleNoLyrics())
        }
    }
}
