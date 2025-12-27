package com.lyrics.feelin.core.designsystem.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import com.lyrics.feelin.R

val HomeActiveIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.home_active)

val HomeInactiveIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.home_inactive)

val NoteSearchingActiveIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.note_searching_active)

val NoteSearchingInactiveIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.note_searching_inactive)

val MyPageActiveIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.mypage_active)

val MyPageInactiveIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.mypage_inactive)

val BackIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_back)

val NotificationIcon: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_notification)

val FeelinTextIcon: Painter
    @Composable
    get() = painterResource(id = R.drawable.feelin_text_logo)

val CheckBoxIconDisabled: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_check_box_disabled)

val CheckBoxIconEnabled: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.ic_check_box_enabled)

val SettingsIconLight: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.settings_light)

val SettingsIconDark: ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.settings_dark)

val CaretIcon: Painter
    @Composable
    get() = painterResource(id = R.drawable.caret)

val EmptyImageDarkIcon: Painter
    @Composable
    get() = painterResource(id = R.drawable.empty_image_dark)

val EmptyImageLightIcon: Painter
    @Composable
    get() = painterResource(id = R.drawable.empty_image_light)

val XCircleIcon: Painter
    @Composable
    get() = painterResource(id = R.drawable.xcircle)

val SearchIcon: Painter
    @Composable
    get() = painterResource(id = R.drawable.search)

val PlayIcon: Painter
    @Composable
    get() = painterResource(id = R.drawable.play)

val SongListIcon: Painter
    @Composable
    get() = painterResource(id = R.drawable.song_list)

val PlusIcon: Painter
    @Composable
    get() = painterResource(id = R.drawable.plus)
