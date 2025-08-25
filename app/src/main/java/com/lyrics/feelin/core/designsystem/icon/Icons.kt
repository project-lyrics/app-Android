package com.lyrics.feelin.core.designsystem.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
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
