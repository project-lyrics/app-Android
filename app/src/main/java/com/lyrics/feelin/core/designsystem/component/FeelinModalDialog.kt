package com.lyrics.feelin.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.designsystem.theme.Typography

@Composable
fun FeelinModalDialog(
    title: String,
    description: String,
    confirmButtonText: String,
    onConfirmButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDismissButtonEnable: Boolean = true,
    dismissButtonText: String = "",
    onDismissButtonClick: () -> Unit = {}
) {
    val feelinColors = LocalFeelinColors.current

    val confirmInteractionSource = remember { MutableInteractionSource() }
    val isConfirmPressed by confirmInteractionSource.collectIsPressedAsState()

    Dialog(onDismissRequest = {}) {
        Column(
            modifier = modifier.width(280.dp)
                .clip(shape = RoundedCornerShape(12.dp))
                .background(color = feelinColors.modal),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                style = Typography.titleMedium.copy(color = feelinColors.gray09),
                modifier = Modifier.padding(top = 24.dp)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(color = feelinColors.gray05),
                modifier = Modifier.padding(top = 10.dp, bottom = 12.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                isDismissButtonEnable.takeIf { it }?.let {
                    val dismissInteractionSource = remember { MutableInteractionSource() }
                    val isDismissPressed by dismissInteractionSource.collectIsPressedAsState()

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .background(if (isDismissPressed) feelinColors.gray01 else feelinColors.modal)
                            .clickable(
                                interactionSource = dismissInteractionSource,
                                indication = null,
                                onClick = onDismissButtonClick::invoke
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dismissButtonText,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = if (isSystemInDarkTheme()) feelinColors.gray06 else feelinColors.gray02
                            )
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .background(if (isConfirmPressed) feelinColors.gray01 else feelinColors.modal)
                        .clickable(
                            interactionSource = confirmInteractionSource,
                            indication = null,
                            onClick = onConfirmButtonClick::invoke
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = confirmButtonText,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FeelinModalDialogPreview() {
    FeelinTheme {
        FeelinModalDialog(
            title = "모달 제목",
            description = "모달 설명은 여기 적으면 됩니다.",
            confirmButtonText = "확인",
            onConfirmButtonClick = {},
            isDismissButtonEnable = true,
            dismissButtonText = "취소",
            onDismissButtonClick = {}
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FeelinModalDialogSingleButtonPreview() {
    FeelinTheme {
        FeelinModalDialog(
            title = "모달 제목",
            description = "모달 설명은 여기 적으면 됩니다.",
            isDismissButtonEnable = false,
            confirmButtonText = "확인",
            onConfirmButtonClick = {}
        )
    }
}
