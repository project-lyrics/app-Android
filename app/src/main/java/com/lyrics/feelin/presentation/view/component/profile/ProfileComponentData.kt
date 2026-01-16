package com.lyrics.feelin.presentation.view.component.profile

import androidx.annotation.DrawableRes
import com.lyrics.feelin.R

enum class ProfileType {
    SHORT_HAIR,
    BRAIDED_HAIR,
    PARTED_HAIR,
    POOP_HAIR,
}

/**
 * 프로파일 컴포넌트의 데이터 클래스
 *
 * @property type 프로파일 아이콘 타입
 * @property isEnable 활성화 아이콘 표시 여부 (아직 미 사용)
 * @property enableAsset 활성화 시 표시할 에셋
 * @property disableLightAsset 비활성화+라이트모드 시 표시할 에셋
 * @property disableDarkAsset 비활성화+다크모드 시 표시할 에셋
 */
data class ProfileComponentData(
    val type: ProfileType,
    val isEnable: Boolean,
    @get:DrawableRes val enableAsset: Int,
    @get:DrawableRes val disableLightAsset: Int,
    @get:DrawableRes val disableDarkAsset: Int,
) {
    companion object Companion {
        fun fromProfileType(profileType: ProfileType): ProfileComponentData {
            when (profileType) {
                ProfileType.SHORT_HAIR ->
                    return ProfileComponentData(
                        type = ProfileType.SHORT_HAIR,
                        isEnable = true,
                        enableAsset = R.drawable.profile_1_activated,
                        disableLightAsset = R.drawable.profile_1_light_inactive,
                        disableDarkAsset = R.drawable.profile_1_dark_inactive,
                    )

                ProfileType.BRAIDED_HAIR ->
                    return ProfileComponentData(
                        type = ProfileType.BRAIDED_HAIR,
                        isEnable = true,
                        enableAsset = R.drawable.profile_2_activated,
                        disableLightAsset = R.drawable.profile_2_light_inactive,
                        disableDarkAsset = R.drawable.profile_2_dark_inactive,
                    )

                ProfileType.PARTED_HAIR ->
                    return ProfileComponentData(
                        type = ProfileType.PARTED_HAIR,
                        isEnable = true,
                        enableAsset = R.drawable.profile_3_activated,
                        disableLightAsset = R.drawable.profile_3_light_inactive,
                        disableDarkAsset = R.drawable.profile_3_dark_inactive,
                    )

                ProfileType.POOP_HAIR ->
                    return ProfileComponentData(
                        type = ProfileType.POOP_HAIR,
                        isEnable = true,
                        enableAsset = R.drawable.profile_4_activated,
                        disableLightAsset = R.drawable.profile_4_light_inactive,
                        disableDarkAsset = R.drawable.profile_4_dark_inactive,
                    )
            }
        }
    }
}
