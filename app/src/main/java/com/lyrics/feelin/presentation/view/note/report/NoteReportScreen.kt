package com.lyrics.feelin.presentation.view.note.report

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.component.FeelinCheckboxItem
import com.lyrics.feelin.core.designsystem.component.FeelinModalDialog
import com.lyrics.feelin.core.designsystem.component.FeelinNormalButton
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithBack
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

private val reportReasons = listOf(
    "커뮤니티 성격에 맞지 않음",
    "타 유저 혹은 아티스트 비방",
    "불쾌감을 조성하는 음란성 / 선정적인 내용",
    "상업적 광고",
    "부적절한 정보 유출",
    "정치적인 내용 / 종교 포교 시도",
    "기타"
)

enum class NoteReportDialogType {
    Success,
    Duplicate,
}

@Suppress("UnusedParameter")
@Composable
fun NoteReportScreen(
    noteId: Long,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    initialDialogType: NoteReportDialogType? = null,
) {
    var selectedReason by remember { mutableStateOf<String?>(null) }
    var otherReasonText by remember { mutableStateOf("") }
    var isAgreed by remember { mutableStateOf(false) }

    var visibleDialogType by remember { mutableStateOf(initialDialogType) }

    val isSubmitEnabled = selectedReason != null && isAgreed &&
        (selectedReason != "기타" || otherReasonText.isNotBlank())

    val feelinColors = LocalFeelinColors.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            FeelinTopAppBarWithBack(
                title = "신고하기",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(feelinColors.backgroundPrimary)
                    .padding(vertical = 12.dp)
            ) {
                FeelinCheckboxItem(
                    checked = isAgreed,
                    onCheckedChange = { isAgreed = it },
                    text = "개인정보 수집에 동의합니다.",
                    required = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                FeelinNormalButton(
                    text = "신고하기",
                    enabled = isSubmitEnabled,
                    onClick = { visibleDialogType = NoteReportDialogType.Success },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        },
        containerColor = feelinColors.backgroundPrimary,
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            reportReasons.forEach { reason ->
                FeelinRadioButtonItem(
                    selected = selectedReason == reason,
                    text = reason,
                    onClick = {
                        selectedReason = reason
                        if (reason != "기타") {
                            otherReasonText = ""
                        }
                    }
                )
            }

            if (selectedReason == "기타") {
                BasicTextField(
                    value = otherReasonText,
                    onValueChange = { otherReasonText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .heightIn(min = 64.dp, max = 144.dp)
                        .background(color = feelinColors.inputField, shape = RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    textStyle = FeelinTypography.body1.copy(color = feelinColors.gray09),
                    cursorBrush = SolidColor(feelinColors.brandPrimary),
                    decorationBox = { innerTextField ->
                        Box {
                            if (otherReasonText.isEmpty()) {
                                Text(
                                    text = "신고사유를 작성해주세요.",
                                    style = FeelinTypography.body1,
                                    color = feelinColors.gray04
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            Text(
                text = "*관리자 검토 진행 후 신고가 반려될 수 있으며, 고의적인 허위신고가 반복될 경우 서비스 이용이 제한될 수 있습니다.",
                style = FeelinTypography.caption1,
                color = feelinColors.gray05,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    visibleDialogType?.let { dialogType ->
        FeelinModalDialog(
            title = when (dialogType) {
                NoteReportDialogType.Success -> "신고가 접수되었어요."
                NoteReportDialogType.Duplicate -> "이미 신고되었어요."
            },
            description = null,
            confirmButtonText = "확인",
            onConfirmButtonClick = {
                visibleDialogType = null
                if (dialogType == NoteReportDialogType.Success) {
                    onBackClick()
                }
            },
            isDismissButtonEnable = false
        )
    }
}

@Composable
private fun FeelinRadioButtonItem(
    selected: Boolean,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val feelinColors = LocalFeelinColors.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .border(
                    width = if (selected) 6.dp else 1.5.dp,
                    color = if (selected) feelinColors.brandPrimary else feelinColors.gray02,
                    shape = CircleShape
                )
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = text,
            style = FeelinTypography.title2,
            color = feelinColors.gray09
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NoteReportScreenPreview() {
    FeelinTheme {
        NoteReportScreen(
            noteId = 1L,
            onBackClick = {}
        )
    }
}
