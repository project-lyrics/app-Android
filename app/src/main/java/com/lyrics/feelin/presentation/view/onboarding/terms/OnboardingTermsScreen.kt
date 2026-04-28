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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.component.FeelinCheckboxAllAgree
import com.lyrics.feelin.core.designsystem.component.FeelinCheckboxItem
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithBack
import com.lyrics.feelin.core.domain.model.SignUpTerm
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

@Composable
fun OnboardingTermsScreen(
    onBackClick: () -> Unit,
    onStartClick: () -> Unit,
    termAgreements: Map<SignUpTerm, Boolean>,
    onAllCheckedChange: (Boolean) -> Unit,
    onTermCheckedChange: (SignUpTerm, Boolean) -> Unit,
    onDetailClick: (SignUpTerm) -> Unit,
    modifier: Modifier = Modifier,
) {
    FeelinTheme(darkTheme = false) {
        val feelinColors = LocalFeelinColors.current
        val allChecked = SignUpTerm.entries
            .filter { it.required }
            .all { termAgreements[it] == true }

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = feelinColors.gray00),
        ) {
            FeelinTopAppBarWithBack(
                title = "",
                onBackClick = onBackClick,
                showDivider = false,
            )

            Spacer(modifier = Modifier.height(28.dp))

            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
            ) {
                Text(
                    text = "Feelin 이용을 위해 약관을\n동의해주세요",
                    style = FeelinTypography.heading1,
                )

                Spacer(modifier = Modifier.height(32.dp))

                FeelinCheckboxAllAgree(
                    checked = allChecked,
                    onCheckedChange = onAllCheckedChange,
                    text = "전체동의",
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(24.dp))

                SignUpTerm.entries.forEachIndexed { index, signUpTerm ->
                    FeelinCheckboxItem(
                        checked = termAgreements[signUpTerm] == true,
                        onCheckedChange = { checked -> onTermCheckedChange(signUpTerm, checked) },
                        text = signUpTerm.title,
                        required = signUpTerm.required,
                        detailText = if (signUpTerm.agreement.isNotBlank()) "보기" else null,
                        onDetailClick = if (signUpTerm.agreement.isNotBlank()) {
                            { onDetailClick(signUpTerm) }
                        } else {
                            null
                        },
                    )

                    if (index < SignUpTerm.entries.lastIndex) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onStartClick,
                    enabled = allChecked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = feelinColors.systemActivate,
                        disabledContainerColor = feelinColors.systemDisable,
                    ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(
                        text = "시작하기",
                        style = FeelinTypography.title2,
                        color = feelinColors.gray00,
                    )
                }
            }
        }
    }
}

@Preview(
    name = "Onboarding term check screen",
    showBackground = true,
)
@Preview(
    name = "Onboarding term check screen - Dark Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
)
@Composable
private fun OnboardingTermsScreenPreview() {
    FeelinTheme {
        OnboardingTermsScreen(
            onBackClick = {},
            onStartClick = {},
            termAgreements = SignUpTerm.entries.associateWith { false },
            onAllCheckedChange = {},
            onTermCheckedChange = { _, _ -> },
            onDetailClick = {},
        )
    }
}
