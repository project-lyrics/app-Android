package com.lyrics.feelin.core.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class Gender {
    MALE,
    FEMALE;

    companion object {
        fun fromString(value: String): Gender? {
            return Gender.entries.find { it.name.equals(value, ignoreCase = true) }
        }
    }
}
