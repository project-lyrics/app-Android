package com.lyrics.feelin.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.icon.SettingsIconLight
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeelinModalBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    showDragHandle: Boolean = true,
    content: @Composable (ColumnScope.() -> Unit)
) {
    val feelinColors = LocalFeelinColors.current

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        containerColor = feelinColors.modal,
        dragHandle = { if (showDragHandle) FeelinDragHandle() },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start,
            content = content
        )
        Spacer(modifier = Modifier.height(22.dp))
    }
}

/**
 * 기본 `DragHandle`에서 패딩을 조절한 버전의 DragHandle입니다.
 * M3의 기본 핸들은 핸들의 상하 패딩을 조절할 수 없어 별도의 컴포넌트를 만들어 사용합니다.
 * */
@Composable
private fun FeelinDragHandle(modifier: Modifier = Modifier) {
    val feelinColors = LocalFeelinColors.current

    Surface(
        modifier = modifier.padding(top = 10.dp, bottom = 22.dp),
        color = feelinColors.gray02,
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Box(Modifier.size(width = 56.dp, height = 5.dp))
    }
}

@Composable
fun FeelinModalBottomSheetAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    val feelinColors = LocalFeelinColors.current

    val pressInteractionSource = remember { MutableInteractionSource() }
    val isPressed by pressInteractionSource.collectIsPressedAsState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = if (isPressed) feelinColors.systemPressedGreyScale else feelinColors.modal)
            .clickable(
                interactionSource = pressInteractionSource,
                indication = null,
                onClick = onClick::invoke
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Spacer(modifier = Modifier.width(20.dp))
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = feelinColors.gray09,
                modifier = Modifier.padding(end = 12.dp)
            )
        }
        Text(text = text, style = FeelinTypography.body1.copy(color = feelinColors.gray09))
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FeelinModalBottomSheetActionPreview() {
    FeelinTheme {
        FeelinModalBottomSheetAction(text = "테스트", onClick = {})
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FeelinModalBottomSheetActionIconPreview() {
    FeelinTheme {
        FeelinModalBottomSheetAction(icon = SettingsIconLight, text = "테스트", onClick = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FeelinModalBottomSheetPreview() {
    val sheetState = rememberModalBottomSheetState()

    FeelinTheme {
        FeelinModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {}
        ) {
            FeelinModalBottomSheetAction(icon = SettingsIconLight, text = "공유하기", onClick = {})
            FeelinModalBottomSheetAction(text = "해석공유", onClick = {})
            FeelinModalBottomSheetAction(icon = SettingsIconLight, text = "수정하기", onClick = {})
            FeelinModalBottomSheetAction(text = "질문", onClick = {})
            FeelinModalBottomSheetAction(icon = SettingsIconLight, text = "차단하기", onClick = {})
        }
    }
}
