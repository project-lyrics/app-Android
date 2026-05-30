package com.lyrics.feelin.presentation.view.component.comment

import androidx.compose.runtime.Immutable
import com.lyrics.feelin.core.domain.model.ProfileType
import kotlinx.datetime.LocalDateTime

/**
 * 댓글 컴포넌트의 데이터 클래스입니다.
 *
 * @property id 서버에서 받은 댓글 id
 * @property content 댓글 내용
 * @property createdAt 댓글 작성 시간
 * @property writer 댓글 작성자 정보
 * @property isMine 내 댓글 여부
 */
@Immutable
data class CommentComponentData(
    val id: Long,
    val content: String,
    val createdAt: LocalDateTime,
    val writer: CommentWriterData,
    val isMine: Boolean,
) {
    companion object {
        fun sample(
            id: Long = 1L,
            isMine: Boolean = false
        ): CommentComponentData {
            return CommentComponentData(
                id = id,
                content = "전 T + Tik Tak Tok도 좋더라고요~",
                createdAt = LocalDateTime.parse(input = "2024-01-15T14:30:00"),
                writer = CommentWriterData.sample(),
                isMine = isMine,
            )
        }
    }
}

/** 댓글 작성자 정보입니다. */
@Immutable
data class CommentWriterData(
    val nickname: String,
    val profileCharacterType: ProfileType,
) {
    companion object {
        fun sample(): CommentWriterData {
            return CommentWriterData(
                nickname = "자경단1호",
                profileCharacterType = ProfileType.POOP_HAIR,
            )
        }
    }
}
