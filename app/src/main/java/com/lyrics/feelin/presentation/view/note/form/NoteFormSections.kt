package com.lyrics.feelin.presentation.view.note.form

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lyrics.feelin.R
import com.lyrics.feelin.core.designsystem.icon.CaretIcon
import com.lyrics.feelin.core.designsystem.icon.CloseIcon
import com.lyrics.feelin.core.designsystem.icon.ImageGalleryIcon
import com.lyrics.feelin.core.designsystem.icon.SearchIcon
import com.lyrics.feelin.core.designsystem.icon.SongListIcon
import com.lyrics.feelin.core.domain.enum.NoteTopic
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LightGray00
import com.lyrics.feelin.presentation.designsystem.theme.LightGray04
import com.lyrics.feelin.presentation.designsystem.theme.LightGray09
import com.lyrics.feelin.presentation.designsystem.theme.LightSystemDisable
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.component.music.MusicComponent
import com.lyrics.feelin.presentation.view.component.music.MusicComponentData
import com.lyrics.feelin.presentation.view.component.note.LyricsBackground

@Immutable
internal data class LyricsSectionActions(
    val onLyricsChange: (String) -> Unit,
    val onLyricsBackgroundSheetOpen: () -> Unit,
    val onLyricsSearchSheetOpen: () -> Unit,
    val onLyricsFocusChange: (Boolean) -> Unit,
    val onShowNoSongDialog: () -> Unit,
)

@Immutable
internal data class BodySectionActions(
    val onBodyChange: (String) -> Unit,
    val onShowSongSection: () -> Unit,
)

@Composable
internal fun NoteFormCategorySelector(
    selectedTopic: NoteTopic?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalFeelinColors.current

    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = selectedTopic.toNoteFormLabel(),
            style = FeelinTypography.body2,
            color = colors.gray08,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = CaretIcon,
            contentDescription = "카테고리 선택",
            tint = colors.gray08,
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
internal fun NoteFormSongSection(
    selectedSong: MusicComponentData.NoteWriteMusicExist?,
    isDeleteVisible: Boolean,
    isSongSelectable: Boolean,
    onSongClick: () -> Unit,
    onSongDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isDeleteVisible) {
        NoteFormSongDeleteHeader(onSongDelete = onSongDelete)
    }

    Box(
        modifier = modifier.clickable(
            enabled = isSongSelectable,
            onClick = onSongClick,
        ),
    ) {
        MusicComponent(
            state = selectedSong ?: MusicComponentData.NoteWriteEmpty,
        )
    }
}

@Composable
private fun NoteFormSongDeleteHeader(
    onSongDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalFeelinColors.current

    HorizontalDivider(color = colors.gray01, thickness = 1.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "곡",
            style = FeelinTypography.title3.copy(lineHeight = 20.sp),
            color = colors.gray08,
            modifier = Modifier.clickable(onClick = onSongDelete),
        )
        Icon(
            imageVector = CloseIcon,
            contentDescription = "삭제",
            tint = colors.gray08,
            modifier = Modifier
                .size(16.dp)
                .clickable(onClick = onSongDelete),
        )
    }
}

@Suppress("MultipleEmitters")
@Composable
internal fun NoteFormLyricsSection(
    lyrics: String,
    lyricsBackground: LyricsBackground,
    isLyricsFocused: Boolean,
    selectedSong: MusicComponentData.NoteWriteMusicExist?,
    actions: LyricsSectionActions,
    modifier: Modifier = Modifier,
) {
    val colors = LocalFeelinColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val currentOnLyricsFocusChange by rememberUpdatedState(actions.onLyricsFocusChange)

    LaunchedEffect(isFocused) {
        currentOnLyricsFocusChange(isFocused)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .height(132.dp),
    ) {
        Image(
            painter = painterResource(id = lyricsBackground.noteFormDrawableRes()),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        BasicTextField(
            value = lyrics,
            onValueChange = actions.onLyricsChange,
            interactionSource = interactionSource,
            textStyle = FeelinTypography.body1.copy(
                color = if (lyricsBackground.isNoteFormDark()) LightGray00 else LightGray09,
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 20.dp),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    if (lyrics.isEmpty() && !isLyricsFocused) {
                        Text(
                            text = "좋아하는 가사를 적어주세요 (선택)",
                            style = FeelinTypography.body1,
                            color = if (selectedSong == null) LightSystemDisable else LightGray04,
                            textAlign = TextAlign.Center,
                        )
                    }
                    innerTextField()
                }
            },
        )
        Text(
            text = "${lyrics.length}/$NOTE_FORM_LYRICS_MAX_LENGTH",
            style = FeelinTypography.caption1,
            color = if (lyrics.isEmpty()) colors.gray04 else colors.gray08,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 20.dp),
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.End),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LyricsActionButton(
            text = "가사 배경",
            isEnable = lyrics.isNotEmpty(),
            icon = { ImageGalleryIcon },
            onClick = {
                if (selectedSong != null) {
                    actions.onLyricsBackgroundSheetOpen()
                } else {
                    actions.onShowNoSongDialog()
                }
            },
        )
        LyricsActionButton(
            text = "가사 검색",
            isEnable = true,
            icon = { SearchIcon },
            onClick = {
                if (selectedSong != null) {
                    actions.onLyricsSearchSheetOpen()
                } else {
                    actions.onShowNoSongDialog()
                }
            },
        )
    }
}

@Suppress("MultipleEmitters")
@Composable
internal fun NoteFormBodySection(
    body: String,
    bodyPlaceholder: String,
    isBottomSongButtonVisible: Boolean,
    isBottomSongButtonEnabled: Boolean,
    actions: BodySectionActions,
    modifier: Modifier = Modifier,
) {
    val colors = LocalFeelinColors.current

    BasicTextField(
        value = body,
        onValueChange = actions.onBodyChange,
        textStyle = FeelinTypography.body3.copy(color = colors.gray08),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp),
        decorationBox = { innerTextField ->
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
                if (body.isEmpty()) {
                    Text(
                        text = bodyPlaceholder,
                        style = FeelinTypography.body3,
                        color = colors.gray04,
                    )
                }
                innerTextField()
            }
        },
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        if (isBottomSongButtonVisible) {
            NoteFormBottomSongButton(
                isEnabled = isBottomSongButtonEnabled,
                onClick = actions.onShowSongSection,
            )
        } else {
            Spacer(modifier = Modifier.width(1.dp))
        }
        Text(
            text = "${body.length}/$NOTE_FORM_BODY_MAX_LENGTH",
            style = FeelinTypography.caption1,
            color = colors.gray04,
        )
    }
}

@Composable
private fun NoteFormBottomSongButton(
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalFeelinColors.current
    val contentColor = if (isEnabled) colors.gray08 else colors.systemDisable

    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(size = 8.dp),
                color = colors.gray01,
            )
            .clickable(enabled = isEnabled, onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = SongListIcon,
            contentDescription = "곡 추가",
            tint = contentColor,
            modifier = Modifier.size(16.dp),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "곡",
            style = FeelinTypography.body2,
            color = contentColor,
        )
    }
}

@Composable
private fun LyricsActionButton(
    text: String,
    isEnable: Boolean,
    icon: @Composable () -> ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalFeelinColors.current
    val actionButtonColor = if (isEnable) colors.gray05 else colors.systemDisable

    Row(
        modifier = modifier
            .border(width = 1.dp, color = colors.gray01, shape = RoundedCornerShape(4.dp))
            .clickable(enabled = isEnable, onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon(),
            contentDescription = text,
            tint = actionButtonColor,
            modifier = Modifier.size(16.dp),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = FeelinTypography.body2,
            color = actionButtonColor,
        )
    }
}

internal fun NoteTopic?.toNoteFormLabel(): String {
    return when (this) {
        NoteTopic.INTERPRETATION -> "해석공유"
        NoteTopic.FREE -> "자유"
        NoteTopic.QUESTION -> "질문"
        else -> "주제를 선택해 주세요"
    }
}

internal fun LyricsBackground.noteFormDrawableRes(): Int {
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

internal fun LyricsBackground.isNoteFormDark(): Boolean = this == LyricsBackground.BLACK || this == LyricsBackground.RED
