package com.lyrics.feelin.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.R
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

@Composable
fun FeelinGenderButton(
    text: String,
    @DrawableRes iconRes: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val feelinColors = LocalFeelinColors.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 204.dp)
            .background(
                color = if (selected) feelinColors.systemPressedBrand else feelinColors.gray00,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = if (selected) 0.dp else 1.dp,
                color = feelinColors.gray01,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = text,
            modifier = Modifier.size(120.dp),
            tint = androidx.compose.ui.graphics.Color.Unspecified
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = text,
            style = FeelinTypography.title2,
            color = if (selected) feelinColors.alertSuccess else feelinColors.gray03
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeelinGenderButtonPreview() {
    FeelinTheme {
        Column(modifier = Modifier.padding(20.dp)) {
            FeelinGenderButton(
                text = "남성",
                iconRes = R.drawable.ic_gender_male_inactive,
                selected = false,
                onClick = {}
            )
            Spacer(modifier = Modifier.height(16.dp))
            FeelinGenderButton(
                text = "여성",
                iconRes = R.drawable.ic_gender_female_active,
                selected = true,
                onClick = {}
            )
        }
    }
}
