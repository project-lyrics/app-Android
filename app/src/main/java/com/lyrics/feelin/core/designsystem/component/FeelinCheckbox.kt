package com.lyrics.feelin.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.icon.CheckBoxIconDisabled
import com.lyrics.feelin.core.designsystem.icon.CheckBoxIconEnabled
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.designsystem.theme.Typography

/**
 * Feelin 전체동의 체크박스
 * 배경색 변경
 */
@Composable
fun FeelinCheckboxAllAgree(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    modifier: Modifier = Modifier,
) {
    val feelinColors = LocalFeelinColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (checked) {
                    feelinColors.systemPressedBrand
                } else {
                    feelinColors.gray00
                },
                shape = RoundedCornerShape(size = 8.dp)
            )
            .border(
                width = if (checked) 0.dp else 1.dp,
                color = if (checked) Color.Transparent else feelinColors.gray01,
                shape = RoundedCornerShape(size = 8.dp)
            )
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (checked) CheckBoxIconEnabled else CheckBoxIconDisabled,
            contentDescription = null,
            tint = Color.Unspecified
        )
        Spacer(
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = text,
            style = Typography.titleMedium,
            color = if (checked) feelinColors.systemActivate else feelinColors.gray04,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeelinCheckboxAllAgreePreview() {
    FeelinTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FeelinCheckboxAllAgree(
                checked = false,
                onCheckedChange = {},
                text = "전체동의"
            )
            FeelinCheckboxAllAgree(
                checked = true,
                onCheckedChange = {},
                text = "전체동의"
            )
        }
    }
}
