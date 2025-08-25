package com.lyrics.feelin.presentation.view.community

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme

@Composable
fun CommunityMainScreen() {
    var current by remember { mutableStateOf(0) }


    // Scaffold(bottomBar = { ... }) 안에서 보통 이렇게 사용
//    FeelinBottomBar(
//        items = items,
//        selectedIndex = current,
//        onItemSelected = { current = it }
//    )

    // 콘텐츠는 listState를 쓰는 LazyColumn 등으로 구성
}

@Preview
@Composable
private fun CommunityMainScreenPreview() {
    FeelinTheme {
        CommunityMainScreen()
    }
}