package com.lyrics.feelin.presentation.view.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

/**
 * Home 화면 전용 바텀시트 scaffold.
 *
 * [FeelinModalBottomSheet]는 메뉴형 공통 바텀시트(drag handle + 상단 radius)를 위해
 * 설계되었으므로, Home의 관심 아티스트 시트처럼 상단이 직각이고 핸들이 없는 화면은
 * 이 래퍼를 사용합니다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBottomSheetScaffold(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)
) {
    val feelinColors = LocalFeelinColors.current

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        containerColor = feelinColors.backgroundPrimary,
        dragHandle = null,
        shape = RectangleShape,
        modifier = modifier.fillMaxHeight()
    ) {
        Column(
            modifier = Modifier.statusBarsPadding(),
            horizontalAlignment = Alignment.Start,
            content = content
        )
        Spacer(modifier = Modifier.height(22.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun HomeBottomSheetScaffoldPreview() {
    val sheetState = rememberModalBottomSheetState()

    FeelinTheme {
        HomeBottomSheetScaffold(
            sheetState = sheetState,
            onDismissRequest = {}
        ) {
            Spacer(modifier = Modifier.height(200.dp))
        }
    }
}
