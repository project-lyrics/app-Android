package com.lyrics.feelin.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.lyrics.feelin.R

/**
 * 프로필 이미지 리소스 선택기
 *
 * 프로필 ID, 선택 상태, 다크모드 여부에 따라 올바른 drawable 리소스를 반환
 */
object FeelinProfileImageSelector {

    /**
     * 프로필 이미지 리소스 ID를 반환
     *
     * @param profileId 프로필 번호 (1-4)
     * @param isSelected 선택된 상태 여부
     * @param isDarkMode 다크모드 여부
     * @return Drawable 리소스 ID
     */
    @DrawableRes
    fun getProfileDrawableRes(
        profileId: Int,
        isSelected: Boolean,
        isDarkMode: Boolean
    ): Int {
        return when {
            isSelected -> getActivatedDrawable(profileId)
            isDarkMode -> getDarkInactiveDrawable(profileId)
            else -> getLightInactiveDrawable(profileId)
        }
    }

    @DrawableRes
    private fun getActivatedDrawable(profileId: Int): Int {
        return when (profileId) {
            PROFILE_ID_1 -> R.drawable.profile_1_activated
            PROFILE_ID_2 -> R.drawable.profile_2_activated
            PROFILE_ID_3 -> R.drawable.profile_3_activated
            PROFILE_ID_4 -> R.drawable.profile_4_activated
            else -> R.drawable.profile_1_activated // 기본값
        }
    }

    @DrawableRes
    private fun getDarkInactiveDrawable(profileId: Int): Int {
        return when (profileId) {
            PROFILE_ID_1 -> R.drawable.profile_1_dark_inactive
            PROFILE_ID_2 -> R.drawable.profile_2_dark_inactive
            PROFILE_ID_3 -> R.drawable.profile_3_dark_inactive
            PROFILE_ID_4 -> R.drawable.profile_4_dark_inactive
            else -> R.drawable.profile_1_dark_inactive
        }
    }

    @DrawableRes
    private fun getLightInactiveDrawable(profileId: Int): Int {
        return when (profileId) {
            PROFILE_ID_1 -> R.drawable.profile_1_light_inactive
            PROFILE_ID_2 -> R.drawable.profile_2_light_inactive
            PROFILE_ID_3 -> R.drawable.profile_3_light_inactive
            PROFILE_ID_4 -> R.drawable.profile_4_light_inactive
            else -> R.drawable.profile_1_light_inactive
        }
    }

    companion object {
        const val PROFILE_ID_1 = 1
        const val PROFILE_ID_2 = 2
        const val PROFILE_ID_3 = 3
        const val PROFILE_ID_4 = 4
    }
}

/**
 * Composable 확장 함수로 프로필 이미지 리소스를 가져옴
 */
@Composable
fun getProfileImageRes(
    profileId: Int,
    isSelected: Boolean,
    isDarkMode: Boolean
): Int {
    return FeelinProfileImageSelector.getProfileDrawableRes(
        profileId = profileId,
        isSelected = isSelected,
        isDarkMode = isDarkMode
    )
}
