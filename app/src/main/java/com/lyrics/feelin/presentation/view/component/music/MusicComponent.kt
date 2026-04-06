package com.lyrics.feelin.presentation.view.component.music

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lyrics.feelin.R
import com.lyrics.feelin.core.designsystem.icon.PlayIcon
import com.lyrics.feelin.core.designsystem.icon.PlusIcon
import com.lyrics.feelin.core.designsystem.icon.SongListIcon
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

@Composable
fun MusicComponent(state: MusicComponentData, modifier: Modifier = Modifier) {
    val feelinColors = LocalFeelinColors.current

    when (state) {
        is MusicComponentData.NoteWriteEmpty -> {
            MusicComponentLayout(
                modifier = modifier,
                showDivider = true,
                leadingContent = {
                    Image(
                        painter = painterResource(R.drawable.album_image),
                        contentDescription = "empty album art",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(shape = RoundedCornerShape(4.dp))
                    )
                },
                textContent = {
                    Text(
                        text = "곡을 추가해주세요",
                        style = FeelinTypography.body2.copy(color = feelinColors.gray04)
                    )
                },
                trailingContent = {
                    Icon(
                        imageVector = PlusIcon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = feelinColors.gray03,
                    )
                }
            )
        }
        is MusicComponentData.NoteWriteMusicExist -> {
            MusicComponentLayout(
                modifier = modifier,
                showDivider = true,
                leadingContent = {
                    AsyncImage(
                        model = state.imageUrl,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(shape = RoundedCornerShape(4.dp)),
                        contentDescription = "${state.songName}'s album art",
                    )
                },
                textContent = {
                    MusicTextContent(
                        songName = state.songName,
                        artistName = state.artistName
                    )
                },
                trailingContent = {
                    Icon(
                        imageVector = SongListIcon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = feelinColors.gray03,
                    )
                }
            )
        }
        is MusicComponentData.SearchList -> {
            MusicComponentLayout(
                modifier = modifier,
                showDivider = false,
                leadingContent = {
                    AsyncImage(
                        model = state.imageUrl,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(shape = RoundedCornerShape(4.dp)),
                        contentDescription = "${state.songName}'s album art",
                    )
                },
                textContent = {
                    MusicTextContent(
                        songName = state.songName,
                        artistName = state.artistName
                    )
                },
                trailingContent = {
                    Icon(
                        imageVector = PlayIcon,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(24.dp),
                        tint = feelinColors.gray03,
                    )
                }
            )
        }
        is MusicComponentData.SearchNoteByMusic -> {
            MusicComponentLayout(
                modifier = modifier,
                showDivider = false,
                leadingContent = {
                    AsyncImage(
                        model = state.imageUrl,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(shape = RoundedCornerShape(4.dp)),
                        contentDescription = "${state.songName}'s album art",
                    )
                },
                textContent = {
                    MusicTextContent(
                        songName = state.songName,
                        artistName = state.artistName
                    )
                },
                trailingContent = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .height(28.dp)
                            .clip(shape = RoundedCornerShape(size = 100.dp))
                            .background(color = feelinColors.brandSecondary)
                    ) {
                        Text(
                            "노트 ${state.noteCount.toFormattedNoteCount()}",
                            style = FeelinTypography.caption1.copy(color = feelinColors.brandTertiary),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            )
        }
        is MusicComponentData.NoteComponent -> {
            MusicComponentLayout(
                modifier = modifier,
                showDivider = true,
                leadingContent = {
                    AsyncImage(
                        model = state.imageUrl,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(shape = RoundedCornerShape(4.dp)),
                        contentDescription = "${state.songName}'s album art",
                    )
                },
                textContent = {
                    MusicTextContent(
                        songName = state.songName,
                        artistName = state.artistName
                    )
                },
                trailingContent = {
                    Icon(
                        imageVector = PlayIcon,
                        contentDescription = "${state.songName} play",
                        modifier = Modifier.size(24.dp),
                        tint = feelinColors.gray03,
                    )
                }
            )
        }
    }
}

/**
 * MusicComponent의 공통 레이아웃.
 * Slot 패턴을 사용하여 각 상태별로 다른 콘텐츠를 주입받습니다.
 */
@Composable
private fun MusicComponentLayout(
    leadingContent: @Composable () -> Unit,
    textContent: @Composable () -> Unit,
    trailingContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    showDivider: Boolean = true,
) {
    val feelinColors = LocalFeelinColors.current

    Column(modifier = modifier) {
        if (showDivider) {
            HorizontalDivider(color = feelinColors.gray01, thickness = 1.dp)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                leadingContent()
                Spacer(modifier = Modifier.width(10.dp))
                textContent()
            }
            trailingContent()
        }
        if (showDivider) {
            HorizontalDivider(color = feelinColors.gray01, thickness = 1.dp)
        }
    }
}

/**
 * 곡명과 아티스트명을 표시하는 공통 텍스트 콘텐츠.
 */
@Composable
private fun MusicTextContent(songName: String, artistName: String) {
    val feelinColors = LocalFeelinColors.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = songName,
            style = FeelinTypography.body2.copy(color = feelinColors.gray08)
        )
        Text(
            text = artistName,
            style = FeelinTypography.caption1.copy(color = feelinColors.gray04),
        )
    }
}

const val MAX_NOTES = 999

/** 노트 수가 [MAX_NOTES] (999)를 초과하면 "999+"로 표시하고 아닐 경우 노트 수를 표시합니다. */
private fun Int.toFormattedNoteCount(): String {
    return if (this > MAX_NOTES) "999+" else this.toString()
}

@Preview(name = "노트 작성 시 곡을 선택하지 않은 경우", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Preview(
    name = "노트 작성 시 곡을 선택하지 않은 경우 - 다크모드",
    showBackground = true,
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun MusicComponentNoteWriteEmptyPreview() {
    FeelinTheme {
        MusicComponent(
            state = MusicComponentData.NoteWriteEmpty
        )
    }
}

@Preview(name = "노트 작성 시 곡을 선택한 경우", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Preview(
    name = "노트 작성 시 곡을 선택한 경우 - 다크모드",
    showBackground = true,
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun MusicComponentNoteWriteMusicExistPreview() {
    FeelinTheme {
        MusicComponent(
            state = MusicComponentData.NoteWriteMusicExist(
                imageUrl = "https://picsum.photos/200",
                songName = "Realize",
                artistName = "실리카겔",
            )
        )
    }
}

@Preview(name = "곡에 대한 노트를 검색하는 경우", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Preview(
    name = "곡에 대한 노트를 검색하는 경우 - 다크모드",
    showBackground = true,
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun MusicComponentSearchNoteByMusicPreview() {
    FeelinTheme {
        MusicComponent(
            state = MusicComponentData.SearchNoteByMusic(
                imageUrl = "https://picsum.photos/200",
                songName = "Realize",
                artistName = "실리카겔",
                noteCount = 999,
            )
        )
    }
}

@Preview(name = "노트 작성 중 곡을 검색하는 경우", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Preview(
    name = "노트 작성 중 곡을 검색하는 경우 - 다크모드",
    showBackground = true,
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun MusicComponentSearchListPreview() {
    FeelinTheme {
        MusicComponent(
            state = MusicComponentData.SearchList(
                imageUrl = "https://picsum.photos/200",
                songName = "Realize",
                artistName = "실리카겔",
            )
        )
    }
}

@Preview(name = "단일 노트에서 곡을 보여주는 경우", showBackground = true, backgroundColor = 0xFFFFFFFF)
@Preview(
    name = "단일 노트에서 곡을 보여주는 경우 - 다크모드",
    showBackground = true,
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun MusicComponentNoteComponentPreview() {
    FeelinTheme {
        MusicComponent(
            state = MusicComponentData.NoteComponent(
                imageUrl = "https://picsum.photos/200",
                songName = "Realize",
                artistName = "실리카겔",
            )
        )
    }
}
