package com.lyrics.feelin.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.icon.CloseIcon
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LightGray00
import com.lyrics.feelin.presentation.designsystem.theme.LocalDarkTheme
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

/**
 * 프로필 캐릭터 선택 Bottom Sheet
 *
 * @param onDismiss 닫기 버튼 클릭 시 호출
 * @param onSelectProfile 선택 버튼 클릭 시 호출 (선택된 프로필 전달)
 * @param selectedProfile 현재 선택된 프로필 캐릭터
 * @param modifier Modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCharacterBottomSheet(
    onDismiss: () -> Unit,
    onSelectProfile: (ProfileCharacter) -> Unit,
    modifier: Modifier = Modifier,
    selectedProfile: ProfileCharacter? = null
) {
    val colors = LocalFeelinColors.current
    val isDarkMode = LocalDarkTheme.current
    var internalSelectedProfile by remember { mutableStateOf(selectedProfile) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colors.modal,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = null,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                ProfileBottomSheetHeader(
                    onDismiss = onDismiss,
                    titleColor = colors.gray09,
                    iconColor = colors.gray09
                )

                ProfileCharacterList(
                    selectedProfile = internalSelectedProfile,
                    onProfileClick = { profile -> internalSelectedProfile = profile },
                    isDarkMode = isDarkMode
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            ProfileSelectButton(
                onClick = { internalSelectedProfile?.let { onSelectProfile(it) } },
                enabled = internalSelectedProfile != null,
                backgroundColor = colors.systemActivate,
                textColor = LightGray00
            )

            Spacer(modifier = Modifier.height(23.dp))
        }
    }
}

@Composable
private fun ProfileCharacterBottomSheetContent(
    onDismiss: () -> Unit,
    onSelectProfile: (ProfileCharacter) -> Unit,
    selectedProfile: ProfileCharacter?,
    modifier: Modifier = Modifier
) {
    val colors = LocalFeelinColors.current
    val isDarkMode = LocalDarkTheme.current
    var internalSelectedProfile by remember { mutableStateOf(selectedProfile) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            ProfileBottomSheetHeader(
                onDismiss = onDismiss,
                titleColor = colors.gray09,
                iconColor = colors.gray09
            )

            ProfileCharacterList(
                selectedProfile = internalSelectedProfile,
                onProfileClick = { profile -> internalSelectedProfile = profile },
                isDarkMode = isDarkMode
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        ProfileSelectButton(
            onClick = { internalSelectedProfile?.let { onSelectProfile(it) } },
            enabled = internalSelectedProfile != null,
            backgroundColor = colors.systemActivate,
            textColor = LightGray00
        )

        Spacer(modifier = Modifier.height(23.dp))
    }
}

@Composable
private fun ProfileBottomSheetHeader(
    onDismiss: () -> Unit,
    titleColor: Color,
    iconColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "프로필 캐릭터",
            style = FeelinTypography.title2,
            color = titleColor
        )

        Icon(
            imageVector = CloseIcon,
            contentDescription = "닫기",
            modifier = Modifier
                .size(24.dp)
                .clickable { onDismiss() },
            tint = iconColor
        )
    }
}

@Composable
private fun ProfileCharacterList(
    selectedProfile: ProfileCharacter?,
    onProfileClick: (ProfileCharacter) -> Unit,
    isDarkMode: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ProfileCharacter.entries.forEach { profile ->
                ProfileSelectItemLayered(
                    profile = profile,
                    isSelected = selectedProfile == profile,
                    onClick = { onProfileClick(profile) },
                    isDarkMode = isDarkMode,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * 레이어 구조의 프로필 선택 아이템.
 *
 * @param profile 프로필 캐릭터
 * @param isSelected 선택 상태
 * @param isDarkMode 다크모드 여부
 * @param onClick 클릭 콜백
 * @param modifier Modifier (보통 weight를 받음)
 */
@Composable
fun ProfileSelectItemLayered(
    profile: ProfileCharacter,
    isSelected: Boolean,
    isDarkMode: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalFeelinColors.current
    val imageRes = profile.getDrawableRes(isSelected, isDarkMode)
    val outerCircleColor = if (isSelected) {
        colors.brandPrimary
    } else {
        colors.systemDisable
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .clickable { onClick() }
            .border(width = 2.dp, color = outerCircleColor, shape = CircleShape)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "프로필 ${profile.id}",
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}


@Composable
private fun ProfileSelectButton(
    onClick: () -> Unit,
    enabled: Boolean,
    backgroundColor: Color,
    textColor: Color
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = "선택",
            style = FeelinTypography.title2
        )
    }
}

@Preview(name = "Light Mode - Profile 1 Selected", showBackground = true)
@Composable
private fun ProfileCharacterBottomSheetPreviewLight() {
    var selectedProfile by remember { mutableStateOf<ProfileCharacter?>(ProfileCharacter.PROFILE_1) }

    FeelinTheme(darkTheme = false) {
        ProfileCharacterBottomSheetContent(
            onDismiss = { },
            onSelectProfile = { selectedProfile = it },
            selectedProfile = selectedProfile
        )
    }
}

@Preview(
    name = "Dark Mode - Profile 2 Selected",
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
private fun ProfileCharacterBottomSheetPreviewDark() {
    var selectedProfile by remember { mutableStateOf<ProfileCharacter?>(ProfileCharacter.PROFILE_2) }

    FeelinTheme(darkTheme = true) {
        ProfileCharacterBottomSheetContent(
            onDismiss = { },
            onSelectProfile = { selectedProfile = it },
            selectedProfile = selectedProfile
        )
    }
}

@Preview(name = "No Selection", showBackground = true)
@Composable
private fun ProfileCharacterBottomSheetPreviewNoSelection() {
    FeelinTheme(darkTheme = false) {
        ProfileCharacterBottomSheetContent(
            onDismiss = { },
            onSelectProfile = { },
            selectedProfile = null
        )
    }
}

@Preview(name = "Light Mode - Profile 3 Selected", showBackground = true)
@Composable
private fun ProfileCharacterBottomSheetPreviewProfile3() {
    var selectedProfile by remember { mutableStateOf<ProfileCharacter?>(ProfileCharacter.PROFILE_3) }

    FeelinTheme(darkTheme = false) {
        ProfileCharacterBottomSheetContent(
            onDismiss = { },
            onSelectProfile = { selectedProfile = it },
            selectedProfile = selectedProfile
        )
    }
}

@Preview(
    name = "Narrow Screen (320dp)",
    showBackground = true,
    widthDp = 320
)
@Composable
private fun ProfileCharacterBottomSheetPreviewNarrow() {
    FeelinTheme(darkTheme = false) {
        ProfileCharacterBottomSheetContent(
            onDismiss = { },
            onSelectProfile = { },
            selectedProfile = ProfileCharacter.PROFILE_1
        )
    }
}