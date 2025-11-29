@file:Suppress("PackageName", "PackageNaming")

package com.lyrics.feelin.presentation.view.onboarding.gender_age

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.lyrics.feelin.R
import com.lyrics.feelin.core.designsystem.component.FeelinDropdown
import com.lyrics.feelin.core.designsystem.component.FeelinGenderButton
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithBack
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.designsystem.theme.Typography

@Composable
fun OnboardingGenderAgeScreen(modifier: Modifier = Modifier) {
    FeelinTheme(darkTheme = false) {
        val feelinColors = LocalFeelinColors.current

        // 상태 관리
        var selectedGender by remember { mutableStateOf<String?>(null) }
        var selectedYear by remember { mutableStateOf("") }

        // 버튼 활성화 조건: 성별과 출생 연도 모두 선택
        val isButtonEnabled = selectedGender != null && selectedYear.isNotEmpty()

        // 출생 연도 목록
        @Suppress("MagicNumber")
        val startYear = 1950

        @Suppress("MagicNumber")
        val endYear = 2010
        val birthYears = (startYear..endYear).map { it.toString() }.reversed()

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = feelinColors.gray00)
        ) {
            FeelinTopAppBarWithBack(
                title = "",
                onBackClick = {},
                showDivider = false,
                actions = {
                    Text(
                        text = "건너뛰기",
                        style = Typography.bodyLarge,
                        color = feelinColors.gray05
                    )
                }
            )

            Spacer(modifier = Modifier.height(28.dp))

            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "추가 정보를 입력해주세요",
                    style = Typography.headlineLarge,
                    color = feelinColors.gray08
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "서비스 이용 현황 분석을 위해서만 활용되며\n다른 곳엔 사용되지 않아요",
                    style = Typography.bodySmall,
                    color = feelinColors.gray04
                )

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "성별",
                    style = Typography.titleMedium,
                    color = feelinColors.gray05
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FeelinGenderButton(
                        text = "남성",
                        iconRes = if (selectedGender == "male") {
                            R.drawable.ic_gender_male_active
                        } else {
                            R.drawable.ic_gender_male_inactive
                        },
                        selected = selectedGender == "male",
                        onClick = { selectedGender = "male" },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    FeelinGenderButton(
                        text = "여성",
                        iconRes = if (selectedGender == "female") {
                            R.drawable.ic_gender_female_active
                        } else {
                            R.drawable.ic_gender_female_inactive
                        },
                        selected = selectedGender == "female",
                        onClick = { selectedGender = "female" },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "출생 연도",
                    style = Typography.titleMedium,
                    color = feelinColors.gray05
                )

                Spacer(modifier = Modifier.height(12.dp))

                FeelinDropdown(
                    value = if (selectedYear.isNotEmpty()) "${selectedYear}년" else "",
                    placeholder = "출생 연도를 입력해주세요",
                    items = birthYears.map { "${it}년" },
                    onValueChange = {
                        selectedYear = it.removeSuffix("년")
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { /* 다음 */ },
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
                        text = "다음",
                        style = Typography.titleMedium,
                        color = feelinColors.gray00
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingGenderAgeScreenPreview() {
    FeelinTheme {
        OnboardingGenderAgeScreen()
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun OnboardingGenderAgeScreenDarkModePreview() {
    FeelinTheme {
        OnboardingGenderAgeScreen()
    }
}

@Preview(showBackground = true, name = "Gender Selected")
@Composable
private fun OnboardingGenderAgeScreenGenderSelectedPreview() {
    FeelinTheme(darkTheme = false) {
        OnboardingGenderAgeScreenWithState(selectedGender = "female", selectedYear = "")
    }
}

@Preview(showBackground = true, name = "Year Selected")
@Composable
private fun OnboardingGenderAgeScreenYearSelectedPreview() {
    FeelinTheme(darkTheme = false) {
        OnboardingGenderAgeScreenWithState(selectedGender = "female", selectedYear = "2000")
    }
}

@Composable
private fun OnboardingGenderAgeScreenWithState(
    selectedGender: String,
    selectedYear: String,
    modifier: Modifier = Modifier
) {
    FeelinTheme(darkTheme = false) {
        val feelinColors = LocalFeelinColors.current

        // 버튼 활성화 조건: 성별과 출생 연도 모두 선택
        val isButtonEnabled = selectedGender.isNotEmpty() && selectedYear.isNotEmpty()

        // 출생 연도 목록
        @Suppress("MagicNumber")
        val startYear = 1950

        @Suppress("MagicNumber")
        val endYear = 2010
        val birthYears = (startYear..endYear).map { it.toString() }.reversed()

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = feelinColors.gray00)
        ) {
            FeelinTopAppBarWithBack(
                title = "",
                onBackClick = {},
                showDivider = false,
                actions = {
                    Text(
                        text = "건너뛰기",
                        style = Typography.bodyMedium,
                        color = feelinColors.gray05,
                        modifier = Modifier.padding(end = 20.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.height(28.dp))

            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "추가 정보를 입력해주세요",
                    style = Typography.headlineLarge,
                    color = feelinColors.gray08
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "서비스 이용 현황 분석을 위해서만 활용되며\n다른 곳엔 사용되지 않아요",
                    style = Typography.bodySmall,
                    color = feelinColors.gray04
                )

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "성별",
                    style = Typography.titleMedium,
                    color = feelinColors.gray05
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FeelinGenderButton(
                        text = "남성",
                        iconRes = if (selectedGender == "male") {
                            R.drawable.ic_gender_male_active
                        } else {
                            R.drawable.ic_gender_male_inactive
                        },
                        selected = selectedGender == "male",
                        onClick = { },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    FeelinGenderButton(
                        text = "여성",
                        iconRes = if (selectedGender == "female") {
                            R.drawable.ic_gender_female_active
                        } else {
                            R.drawable.ic_gender_female_inactive
                        },
                        selected = selectedGender == "female",
                        onClick = { },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "출생 연도",
                    style = Typography.titleMedium,
                    color = feelinColors.gray05
                )

                Spacer(modifier = Modifier.height(12.dp))

                FeelinDropdown(
                    value = if (selectedYear.isNotEmpty()) "${selectedYear}년" else "",
                    placeholder = "출생 연도를 입력해주세요",
                    items = birthYears.map { "${it}년" },
                    onValueChange = { }
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { /* 다음 */ },
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
                        text = "다음",
                        style = Typography.titleMedium,
                        color = feelinColors.gray00
                    )
                }
            }
        }
    }
}
