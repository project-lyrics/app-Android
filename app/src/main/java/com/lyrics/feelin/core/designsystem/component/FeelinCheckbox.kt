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
            contentDescription = if (checked) "선택됨" else "선택 안됨",
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

/**
 * Feelin 일반 체크박스
 * @param required 필수 라벨 표시 여부
 * @param detailText 상세보기 버튼 텍스트 (null이면 버튼 숨김)
 * @param onDetailClick 상세보기 버튼 클릭 이벤트 (detailText가 null이 아닐 때만 사용)
 */
@Composable
fun FeelinCheckboxItem(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    required: Boolean = false,
    detailText: String? = null,
    onDetailClick: (() -> Unit)? = null,
) {
    val feelinColors = LocalFeelinColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable { onCheckedChange(!checked) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (checked) CheckBoxIconEnabled else CheckBoxIconDisabled,
                contentDescription = if (checked) "선택됨" else "선택 안됨",
                tint = Color.Unspecified
            )

            if (required) {
                Spacer(modifier = Modifier.padding(start = 8.dp))
                Row(
                    modifier = Modifier
                        .background(color = feelinColors.gray00, shape = RoundedCornerShape(100.dp))
                        .border(
                            width = 1.dp,
                            color = feelinColors.gray01,
                            shape = RoundedCornerShape(size = 100.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        text = "필수",
                        style = Typography.bodyMedium,
                        color = feelinColors.gray05,
                    )
                }
            }

            Text(
                text = text,
                style = Typography.titleMedium,
                color = feelinColors.gray04,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        if (detailText != null && onDetailClick != null) {
            Text(
                text = detailText,
                style = Typography.bodyMedium,
                color = feelinColors.gray04,
                modifier = Modifier
                    .clickable { onDetailClick() }
                    .padding(start = 8.dp)
            )
        }
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

@Preview(showBackground = true)
@Composable
private fun FeelinCheckboxItemPreview() {
    FeelinTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 필수
            FeelinCheckboxItem(
                checked = false,
                onCheckedChange = {},
                text = "만 14세 이상 가입 동의",
                required = true
            )

            // 필수 + 상세보기
            FeelinCheckboxItem(
                checked = false,
                onCheckedChange = {},
                text = "서비스 이용약관 동의",
                required = true,
                detailText = "보기",
                onDetailClick = {}
            )

            // 라벨
            FeelinCheckboxItem(
                checked = true,
                onCheckedChange = {},
                text = "마케팅 수신 동의"
            )

            // 라벨 + 상세보기
            FeelinCheckboxItem(
                checked = true,
                onCheckedChange = {},
                text = "개인정보처리방침 동의",
                detailText = "보기",
                onDetailClick = {}
            )
        }
    }
}
