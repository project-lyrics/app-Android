package com.lyrics.feelin.presentation.util

import com.lyrics.feelin.core.domain.enum.NoteTopic

val NoteTopic.label: String
    get() = when (this) {
        NoteTopic.ALL -> "전체노트"
        NoteTopic.INTERPRETATION -> "해석공유"
        NoteTopic.FREE -> "자유"
        NoteTopic.QUESTION -> "질문"
    }
