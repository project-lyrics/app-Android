package com.lyrics.feelin.presentation.view.mypage.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.icon.CaretIcon
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.view.mypage.setting.CARET_ROTATE_TO_RIGHT

@Composable
fun SettingMenuItem(title: String, onClick: () -> Unit) {
    val feelinColors = LocalFeelinColors.current

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clickable(onClick = { onClick.invoke() }),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, style = FeelinTypography.body1.copy(color = feelinColors.gray09))

        Icon(
            imageVector = CaretIcon,
            contentDescription = "enter $title",
            tint = feelinColors.gray05,
            modifier = Modifier
                .size(18.dp)
                .rotate(CARET_ROTATE_TO_RIGHT)
        )
    }
}