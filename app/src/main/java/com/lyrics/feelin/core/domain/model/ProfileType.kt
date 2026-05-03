package com.lyrics.feelin.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ProfileType {
    @SerialName("shortHair")
    SHORT_HAIR,

    @SerialName("braidedHair")
    BRAIDED_HAIR,

    @SerialName("partedHair")
    PARTED_HAIR,

    @SerialName("poopHair")
    POOP_HAIR,
}
