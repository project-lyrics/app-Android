package com.lyrics.feelin.core.designsystem.component

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.lyrics.feelin.R
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors
import com.lyrics.feelin.presentation.designsystem.theme.Typography
import java.util.Calendar

@Composable
fun FeelinBirthYearPicker(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val feelinColors = LocalFeelinColors.current
    var showDialog by remember { mutableStateOf(false) }
    val displayText = value.ifEmpty { placeholder }
    val isActivated = value.isNotEmpty()

    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    @Suppress("MagicNumber")
    val startYear = 1900
    val years = (startYear..currentYear).toList()

    var selectedYearIndex by remember {
        mutableIntStateOf(
            value.toIntOrNull()?.let { years.indexOf(it) } ?: years.indexOf(currentYear)
        )
    }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    color = if (isActivated) feelinColors.systemPressedBrand else feelinColors.gray00,
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    width = if (isActivated) 0.dp else 1.dp,
                    color = feelinColors.gray01,
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable { showDialog = true }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = displayText,
                textAlign = TextAlign.Center,
                style = Typography.titleMedium,
                color = if (isActivated) feelinColors.alertSuccess else feelinColors.gray03,
                modifier = Modifier.weight(1f)
            )
            if (!isActivated) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.caret),
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
                        style = Typography.titleLarge,
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
                                NumberPicker(context).apply {
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
                            style = Typography.bodyLarge,
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
                            style = Typography.bodyLarge,
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
