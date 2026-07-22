package com.lyrics.feelin.core.designsystem.component

import android.view.ContextThemeWrapper
import android.widget.NumberPicker
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.lyrics.feelin.R
import com.lyrics.feelin.core.designsystem.icon.CaretIcon
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalDarkTheme
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import java.util.Calendar

@Composable
fun FeelinBirthYearPicker(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val feelinColors = LocalFeelinColors.current
    val isDarkTheme = LocalDarkTheme.current
    var showDialog by remember { mutableStateOf(false) }
    val displayText = value.ifEmpty { placeholder }
    val isActivated = value.isNotEmpty()

    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    @Suppress("MagicNumber")
    val startYear = 1900
    val years = (startYear..currentYear).toList()

    val initialYearIndex = years.indexOf(value.removeSuffix("년").toIntOrNull())
    var selectedYearIndex by remember(value) {
        mutableIntStateOf(
            initialYearIndex.takeIf { it >= 0 } ?: years.indexOf(currentYear)
        )
    }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(
                    color = if (isActivated) feelinColors.systemPressedBrand else feelinColors.gray00,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = if (isActivated) 0.dp else 1.dp,
                    color = feelinColors.gray01,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { showDialog = true }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = displayText,
                textAlign = TextAlign.Center,
                style = FeelinTypography.title2,
                color = if (isActivated) feelinColors.alertSuccess else feelinColors.gray03,
                modifier = Modifier.weight(1f)
            )
            if (!isActivated) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = CaretIcon,
                    contentDescription = null,
                    tint = feelinColors.gray02,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(
                        text = "출생 연도 선택",
                        style = FeelinTypography.title1,
                        color = feelinColors.gray09
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AndroidView(
                            factory = { context ->
                                // 네이티브 NumberPicker는 Compose 테마를 따르지 않으므로,
                                // Compose의 다크 여부(LocalDarkTheme)에 맞는 명시적 테마로 감싼다.
                                // 온보딩처럼 라이트를 강제하는 화면에서도 피커가 라이트로 그려진다.
                                val pickerTheme =
                                    if (isDarkTheme) R.style.Theme_Feelin_Dark else R.style.Theme_Feelin_Light
                                NumberPicker(ContextThemeWrapper(context, pickerTheme)).apply {
                                    minValue = 0
                                    maxValue = years.size - 1
                                    displayedValues = years.map { it.toString() }.toTypedArray()
                                    wrapSelectorWheel = false
                                    descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                                    setOnValueChangedListener { _, _, newVal ->
                                        selectedYearIndex = newVal
                                    }
                                }
                            },
                            update = { picker ->
                                picker.value = selectedYearIndex
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onValueChange(years[selectedYearIndex].toString())
                            showDialog = false
                        }
                    ) {
                        Text(
                            text = "확인",
                            style = FeelinTypography.body1,
                            color = feelinColors.systemActivate
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDialog = false }
                    ) {
                        Text(
                            text = "취소",
                            style = FeelinTypography.body1,
                            color = feelinColors.gray05
                        )
                    }
                },
                containerColor = feelinColors.gray00,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeelinBirthYearPickerPreview() {
    FeelinTheme {
        FeelinBirthYearPicker(
            value = "",
            placeholder = "출생 연도를 입력해주세요",
            onValueChange = {},
            modifier = Modifier.padding(20.dp)
        )
    }
}
