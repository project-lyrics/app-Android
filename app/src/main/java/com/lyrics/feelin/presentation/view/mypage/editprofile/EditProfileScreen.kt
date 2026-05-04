package com.lyrics.feelin.presentation.view.mypage.editprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lyrics.feelin.core.designsystem.component.FeelinModalDialog
import com.lyrics.feelin.core.designsystem.component.FeelinNicknameInputField
import com.lyrics.feelin.core.designsystem.component.FeelinTopAppBarWithBack
import com.lyrics.feelin.core.designsystem.component.NicknameValidationResult
import com.lyrics.feelin.core.designsystem.component.ProfileCharacter
import com.lyrics.feelin.core.designsystem.component.ProfileCharacterBottomSheet
import com.lyrics.feelin.core.designsystem.component.validateNickname
import com.lyrics.feelin.core.designsystem.icon.WritingIcon
import com.lyrics.feelin.core.domain.model.ProfileType
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTheme
import com.lyrics.feelin.presentation.designsystem.theme.FeelinTypography
import com.lyrics.feelin.presentation.designsystem.theme.LocalFeelinColors

@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    onCompleteClick: (nickname: String, profileType: ProfileType) -> Unit,
    modifier: Modifier = Modifier,
    initialNickname: String = "",
    initialProfileType: ProfileType = ProfileType.SHORT_HAIR,
    isLoading: Boolean = false,
    errorTitle: String? = null,
    errorDescription: String? = null,
    onErrorConfirmClick: () -> Unit = {},
) {
    val feelinColors = LocalFeelinColors.current
    val nicknameState = remember(initialNickname) {
        TextFieldState(initialText = initialNickname)
    }
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedProfile by remember(initialProfileType) {
        mutableStateOf(initialProfileType.toProfileCharacter())
    }

    val currentNickname = nicknameState.text.toString()
    val isNicknameValid = currentNickname.isNotEmpty() &&
        validateNickname(nicknameState.text) == NicknameValidationResult.Valid &&
        !isLoading
    val hasChanges = currentNickname != initialNickname ||
        selectedProfile.profileType != initialProfileType

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = feelinColors.gray00)
    ) {
        FeelinTopAppBarWithBack(
            title = "프로필 수정",
            onBackClick = onBackClick,
            showDivider = false,
            actions = {}
        )

        Spacer(modifier = Modifier.height(28.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(52.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                ProfileImageSelectorWithEdit(
                    selectedProfile = selectedProfile,
                    onEditClick = { showBottomSheet = true }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "*닉네임은 한/영/숫자 상관없이 10자 이내\n(공백, 특수문자, 이모티콘 사용 불가)",
                style = FeelinTypography.body3,
                color = feelinColors.gray04
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                FeelinNicknameInputField(
                    state = nicknameState,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "닉네임",
                    onClearClick = { nicknameState.edit { replace(0, length, "") } }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            CompleteButton(
                text = "프로필 저장",
                enabled = isNicknameValid && hasChanges,
                onClick = {
                    onCompleteClick(
                        currentNickname,
                        selectedProfile.profileType,
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showBottomSheet) {
        ProfileCharacterBottomSheet(
            onDismiss = { showBottomSheet = false },
            onSelectProfile = { profile ->
                selectedProfile = profile
                showBottomSheet = false
            },
            selectedProfile = selectedProfile
        )
    }

    if (errorTitle != null && errorDescription != null) {
        FeelinModalDialog(
            title = errorTitle,
            description = errorDescription,
            confirmButtonText = "확인",
            onConfirmButtonClick = onErrorConfirmClick,
            isDismissButtonEnable = false,
        )
    }
}

@Suppress("MagicNumber")
private val ProfileImageOverlapOffset = (-36).dp

private fun ProfileType.toProfileCharacter(): ProfileCharacter {
    return ProfileCharacter.entries.firstOrNull { it.profileType == this }
        ?: ProfileCharacter.PROFILE_1
}

@Composable
private fun ProfileImageSelectorWithEdit(
    selectedProfile: ProfileCharacter,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profileDrawableRes = selectedProfile.getDrawableRes(
        isSelected = true,
        isDarkMode = false
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(ProfileImageOverlapOffset, Alignment.Start),
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = profileDrawableRes),
            contentDescription = null,
            modifier = Modifier.size(176.dp),
            tint = Color.Unspecified
        )

        Box(
            modifier = Modifier
                .size(56.dp)
                .border(5.dp, LocalFeelinColors.current.gray00, CircleShape)
                .clip(CircleShape)
                .background(LocalFeelinColors.current.brandPrimary)
                .clickable { onEditClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = WritingIcon,
                contentDescription = "프로필 이미지 변경",
                tint = LocalFeelinColors.current.gray00,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun CompleteButton(text: String, enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LocalFeelinColors.current.systemActivate,
            disabledContainerColor = LocalFeelinColors.current.systemDisable
        )
    ) {
        Text(
            text = text,
            style = FeelinTypography.title2,
            color = LocalFeelinColors.current.gray00
        )
    }
}

@Preview
@Composable
private fun EditProfileScreenPreview() {
    FeelinTheme {
        EditProfileScreen(
            onBackClick = {},
            onCompleteClick = { _, _ -> },
            initialNickname = "샘플유저",
            initialProfileType = ProfileType.BRAIDED_HAIR,
        )
    }
}
