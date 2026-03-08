package com.lyrics.feelin.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

// MARK(@이대근): 다크 모드 디자인이 완성될 때 관련 색상을 사용하도록 변경해야 합니다. 2025.11.19.

@Composable
fun FilterButton(data: FilterButtonData, isSelect: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val feelinColors = LocalFeelinColors.current

    Row(
        modifier = modifier
            .height(32.dp)
            .then(
                modifier.border(
                    width = 1.dp,
                    color = feelinColors.gray01,
                    shape = RoundedCornerShape(corner = CornerSize(8.dp))
                ).takeIf { !isSelect } ?: modifier
            )
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = if (isSelect) feelinColors.gray09 else feelinColors.gray00)
            .clickable(enabled = true, onClick = onClick::invoke)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        data.imageUrl?.let {
            AsyncImage(
                model = data.imageUrl,
                contentDescription = data.name,
                modifier = Modifier
                    .size(16.dp)
                    .clip(shape = RoundedCornerShape(10.dp)),
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            data.name,
            style = FeelinTypography.caption2.copy(
                color = if (isSelect) feelinColors.gray00 else feelinColors.gray05
            )
        )
    }
}

/**
 * 선택할 아티스트의 내용을 담는 데이터 클래스
 *
 * @property id 서버에서 받은 식별 id, 전체보기의 경우는 없을 수 있음
 * @property name 이름
 * @property imageUrl 버튼에 표시할 이미지 URL, 전체보기의 경우는 없을 수 있음
 */
data class FilterButtonData(
    val id: Long?,
    val name: String,
    val imageUrl: String?,
) {
    companion object {
        fun empty(): FilterButtonData {
            return FilterButtonData(id = -2L, name = "", imageUrl = "")
        }
    }
}

@Preview
@Composable
private fun FilterButtonNonSelectPreview() {
    FeelinTheme {
        FilterButton(
            data = FilterButtonData(
                id = 15,
                name = "쏜애플",
                imageUrl = "https://i.scdn.co/image/ab6761610000e5eb4506d70c02763753b03fd07b",
            ),
            isSelect = false,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun FilterButtonSelectPreview() {
    FeelinTheme {
        FilterButton(
            data = FilterButtonData(
                id = 15,
                name = "쏜애플",
                imageUrl = "https://i.scdn.co/image/ab6761610000e5eb4506d70c02763753b03fd07b",
            ),
            isSelect = true,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun FilterButtonViewAllNonSelectPreview() {
    FeelinTheme {
        FilterButton(
            FilterButtonData(id = null, name = "전체", imageUrl = null),
            isSelect = false,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun FilterButtonViewAllSelectPreview() {
    FeelinTheme {
        FilterButton(
            FilterButtonData(id = null, name = "전체", imageUrl = null),
            isSelect = true,
            onClick = {}
        )
    }
}
