package com.lyrics.feelin.presentation.view.mypage.userinfo

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.R
import com.lyrics.feelin.core.designsystem.component.FeelinBirthYearPicker
import com.lyrics.feelin.core.designsystem.component.FeelinGenderButton
import com.lyrics.feelin.core.designsystem.component.FeelinModalDialog
import com.lyrics.feelin.core.designsystem.component.FeelinNormalButton
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithBack
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

private const val INITIAL_GENDER = "female"
private const val INITIAL_BIRTH_YEAR = "2000"

@Composable
fun EditGenderBirthYearScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val feelinColors = LocalFeelinColors.current
    var selectedGender by rememberSaveable { mutableStateOf(INITIAL_GENDER) }
    var selectedBirthYear by rememberSaveable { mutableStateOf(INITIAL_BIRTH_YEAR) }
    var showExitDialog by rememberSaveable { mutableStateOf(false) }
    val hasChanges = selectedGender != INITIAL_GENDER || selectedBirthYear != INITIAL_BIRTH_YEAR

    val handleBack = {
        if (hasChanges) {
            showExitDialog = true
        } else {
            onBackClick()
        }
    }

    BackHandler(onBack = handleBack)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
            .background(feelinColors.backgroundPrimary),
        containerColor = feelinColors.backgroundPrimary,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            FeelinTopAppBarWithBack(
                title = "회원 정보 수정",
                onBackClick = handleBack,
                centeredTitle = true,
                showDivider = false,
            )
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 20.dp)
                .windowInsetsPadding(WindowInsets.navigationBars),
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = "성별",
                style = FeelinTypography.title2,
                color = feelinColors.gray05,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
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
                    modifier = Modifier.weight(1f),
                )
                FeelinGenderButton(
                    text = "여성",
                    iconRes = if (selectedGender == "female") {
                        R.drawable.ic_gender_female_active
                    } else {
                        R.drawable.ic_gender_female_inactive
                    },
                    selected = selectedGender == "female",
                    onClick = { selectedGender = "female" },
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "출생 연도",
                style = FeelinTypography.title2,
                color = feelinColors.gray05,
            )

            Spacer(modifier = Modifier.height(12.dp))

            FeelinBirthYearPicker(
                value = "${selectedBirthYear}년",
                placeholder = "출생 연도를 입력해주세요",
                onValueChange = { selectedBirthYear = it.removeSuffix("년") },
            )

            Spacer(modifier = Modifier.weight(1f))

            FeelinNormalButton(
                text = "회원 정보 저장",
                enabled = hasChanges,
                onClick = onSaveClick,
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showExitDialog) {
        FeelinModalDialog(
            title = "저장하지 않고 나가시겠어요?",
            description = null,
            confirmButtonText = "나가기",
            onConfirmButtonClick = onBackClick,
            dismissButtonText = "취소",
            onDismissButtonClick = { showExitDialog = false },
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EditGenderBirthYearScreenPreview() {
    FeelinTheme {
        EditGenderBirthYearScreen(
            onBackClick = {},
            onSaveClick = {},
        )
    }
}
