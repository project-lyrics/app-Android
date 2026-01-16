package com.lyrics.feelin.presentation.view.onboarding.terms

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.component.FeelinCheckboxAllAgree
import com.lyrics.feelin.core.designsystem.component.FeelinCheckboxItem
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithBack
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

@Composable
fun OnboardingTermsScreen(
    onBackClick: () -> Unit,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FeelinTheme(darkTheme = false) {
        val feelinColors = LocalFeelinColors.current

        // 체크박스 상태 관리
        var allChecked by remember { mutableStateOf(false) }
        var ageChecked by remember { mutableStateOf(false) }
        var serviceChecked by remember { mutableStateOf(false) }
        var privacyChecked by remember { mutableStateOf(false) }

        // 전체동의 체크시 모두 체크
        val updateAllChecks = { checked: Boolean ->
            allChecked = checked
            ageChecked = checked
            serviceChecked = checked
            privacyChecked = checked
        }

        // 개별 항목 체크시 전체동의 상태 업데이트
        val updateIndividualCheck = {
            allChecked = ageChecked && serviceChecked && privacyChecked
        }

        // 버튼 활성화 조건: 필수 항목(나이, 서비스, 개인정보) 모두 체크
        val isButtonEnabled = ageChecked && serviceChecked && privacyChecked

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = feelinColors.gray00)
        ) {
            FeelinTopAppBarWithBack(
                title = "",
                onBackClick = onBackClick,
                showDivider = false
            )

            Spacer(modifier = Modifier.height(28.dp))

            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Feelin 이용을 위해 약관을\n동의해주세요",
                    style = FeelinTypography.heading1
                )

                Spacer(modifier = Modifier.height(32.dp))

                FeelinCheckboxAllAgree(
                    checked = allChecked,
                    onCheckedChange = updateAllChecks,
                    text = "전체동의",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                FeelinCheckboxItem(
                    checked = ageChecked,
                    onCheckedChange = { checked ->
                        ageChecked = checked
                        updateIndividualCheck()
                    },
                    text = "만 14세 이상 가입 동의",
                    required = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                FeelinCheckboxItem(
                    checked = serviceChecked,
                    onCheckedChange = { checked ->
                        serviceChecked = checked
                        updateIndividualCheck()
                    },
                    text = "서비스 이용약관 동의",
                    required = true,
                    detailText = "보기",
                    onDetailClick = {}
                )

                Spacer(modifier = Modifier.height(12.dp))

                FeelinCheckboxItem(
                    checked = privacyChecked,
                    onCheckedChange = { checked ->
                        privacyChecked = checked
                        updateIndividualCheck()
                    },
                    text = "개인정보처리방침 동의",
                    required = true,
                    detailText = "보기",
                    onDetailClick = {}
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onStartClick,
                    enabled = isButtonEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = feelinColors.systemActivate,
                        disabledContainerColor = feelinColors.systemDisable
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "시작하기",
                        style = FeelinTypography.title2,
                        color = feelinColors.gray00
                    )
                }
            }
        }
    }
}

@Preview(
    name = "Onboarding term check screen",
    showBackground = true
)
@Preview(
    name = "Onboarding term check screen - Dark Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
private fun OnboardingTermsScreenPreview() {
    FeelinTheme {
        OnboardingTermsScreen(
            onBackClick = {},
            onStartClick = {}
        )
    }
}
