package com.lyrics.feelin.core.designsystem.component

import androidx.annotation.DrawableRes
import com.lyrics.feelin.R
import com.lyrics.feelin.core.domain.model.ProfileType

/**
 * 프로필 캐릭터
 */
enum class ProfileCharacter(
    val id: Int,
    val profileType: ProfileType,
    @DrawableRes val activatedRes: Int,
    @DrawableRes val darkInactiveRes: Int,
    @DrawableRes val lightInactiveRes: Int
) {
    PROFILE_1(
        id = 1,
        profileType = ProfileType.SHORT_HAIR,
        activatedRes = R.drawable.profile_1_activated,
        darkInactiveRes = R.drawable.profile_1_dark_inactive,
        lightInactiveRes = R.drawable.profile_1_light_inactive
    ),
    PROFILE_2(
        id = 2,
        profileType = ProfileType.BRAIDED_HAIR,
        activatedRes = R.drawable.profile_2_activated,
        darkInactiveRes = R.drawable.profile_2_dark_inactive,
        lightInactiveRes = R.drawable.profile_2_light_inactive
    ),
    PROFILE_3(
        id = 3,
        profileType = ProfileType.PARTED_HAIR,
        activatedRes = R.drawable.profile_3_activated,
        darkInactiveRes = R.drawable.profile_3_dark_inactive,
        lightInactiveRes = R.drawable.profile_3_light_inactive
    ),
    PROFILE_4(
        id = 4,
        profileType = ProfileType.POOP_HAIR,
        activatedRes = R.drawable.profile_4_activated,
        darkInactiveRes = R.drawable.profile_4_dark_inactive,
        lightInactiveRes = R.drawable.profile_4_light_inactive
    );

    companion object {
        fun fromId(id: Int): ProfileCharacter = entries.find { it.id == id } ?: PROFILE_1
    }

    @DrawableRes
    fun getDrawableRes(isSelected: Boolean, isDarkMode: Boolean): Int = when {
        isSelected -> activatedRes
        isDarkMode -> darkInactiveRes
        else -> lightInactiveRes
    }
}
