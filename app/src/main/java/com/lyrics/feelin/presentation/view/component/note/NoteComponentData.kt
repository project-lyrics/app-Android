package com.lyrics.feelin.presentation.view.component.note

import com.lyrics.feelin.presentation.view.component.profile.ProfileType
import kotlinx.datetime.LocalDateTime

/**
 * 노트 컴포넌트의 데이터 클래스
 * 
 * @property id 서버에서 받은 id
 * @property content 노트 내용
 * @property status 게시 상태
 * @property createdAt 노트 작성 시간, 받을 때는 "yyyy-MM-dd HH:mm:ss" 형식
 * @property lyrics 가사 데이터 (nullable)
 * @property publisher 게시자 정보
 * @property song 곡 정보
 * @property commentsCount 댓글 수
 * @property likesCount 좋아요 수
 * @property isLiked 좋아요 여부
 * @property isBookmarked 북마크 여부
 */
data class NoteComponentData(
    val id: Long,
    val content: String,
    val status: String,
    val createdAt: LocalDateTime,
    val lyrics: NoteComponentLyricsData?,
    val publisher: NoteComponentUserData,
    val song: NoteComponentSongData,
    val commentsCount: Int,
    val likesCount: Int,
    val isLiked: Boolean,
    val isBookmarked: Boolean,
) {
    companion object {
        fun empty(): NoteComponentData {
            return NoteComponentData(
                id = -1L,
                content = "",
                status = "",
                createdAt = LocalDateTime.parse(input = "1970-01-01T00:00:00"),
                lyrics = NoteComponentLyricsData.empty(),
                publisher = NoteComponentUserData.empty(),
                song = NoteComponentSongData.empty(),
                commentsCount = 0,
                likesCount = 0,
                isLiked = false,
                isBookmarked = false,
            )
        }

        fun sample(): NoteComponentData {
            return NoteComponentData(
                id = 1L,
                content = "세자갈이어 펜노솥혼이 옹니웅어가 흐텔에서 한시아져지흑자에서 이기소로 즌다두, 즐쇡스거안다고 쥰전댄사다 안업까지, 고며나이고브. 됴오가드는 자기파당을 알븍막아 키시느 한답가시에서. 티이세가 그하다 로를 왔거욕런은 영우배다. 로엉삼아룐의 덱짱트엘언뜨 츴던오니, 어매니꺼, 간한죤낸젼다 머아햐닌을. 쑨오라고 그쫀히 첼기두지 븡텨오 갑바, 짇너랭긴을. 우윽으로써 안바는 얼아믁엄에서 죄이머가 윽주가 엇어념젔옴낀바븜은 제가가 돔앺츠일을 네소와 째보도이히고 다제므너로.",
                status = "PUBLISHED",
                createdAt = LocalDateTime.parse(input = "2024-01-15T14:30:00"),
                lyrics = NoteComponentLyricsData.sample(),
                publisher = NoteComponentUserData.sample(),
                song = NoteComponentSongData.sample(),
                commentsCount = 5,
                likesCount = 12,
                isLiked = false,
                isBookmarked = false,
            )
        }

        fun sampleNoLyrics(): NoteComponentData{
            return NoteComponentData(
                id = 1L,
                content = "세자갈이어 펜노솥혼이 옹니웅어가 흐텔에서 한시아져지흑자에서 이기소로 즌다두, 즐쇡스거안다고 쥰전댄사다 안업까지, 고며나이고브. 됴오가드는 자기파당을 알븍막아 키시느 한답가시에서. 티이세가 그하다 로를 왔거욕런은 영우배다. 로엉삼아룐의 덱짱트엘언뜨 츴던오니, 어매니꺼, 간한죤낸젼다 머아햐닌을. 쑨오라고 그쫀히 첼기두지 븡텨오 갑바, 짇너랭긴을. 우윽으로써 안바는 얼아믁엄에서 죄이머가 윽주가 엇어념젔옴낀바븜은 제가가 돔앺츠일을 네소와 째보도이히고 다제므너로.",
                status = "PUBLISHED",
                createdAt = LocalDateTime.parse(input = "2024-01-15T14:30:00"),
                lyrics = null,
                publisher = NoteComponentUserData.sample(),
                song = NoteComponentSongData.sample(),
                commentsCount = 5,
                likesCount = 12,
                isLiked = false,
                isBookmarked = false,
            )
        }
    }
}

enum class LyricsBackground {
    DEFAULT,
    SKYBLUE,
    BLUE,
    LAVENDER,
    MINT,
    BLACK,
    BEIGE,
    PINKGREEN,
    RED,
    WHITE,
    RAINBOW,
}

/**
 * 노트 컴포넌트의 가사 데이터 클래스
 * 
 * @property content 가사 내용
 * @property background 가사 배경 테마
 */
data class NoteComponentLyricsData(val content: String, val background: LyricsBackground) {
    companion object Companion {
        fun empty(): NoteComponentLyricsData {
            return NoteComponentLyricsData(content = "", background = LyricsBackground.DEFAULT)
        }

        fun sample(): NoteComponentLyricsData {
            return NoteComponentLyricsData(
                content = "우리가 길을 헤메이는 시퍼런봄의\n날들은 아직 한가운데",
                background = LyricsBackground.MINT,
            )
        }
    }
}

/**
 * 노트 컴포넌트의 사용자 데이터 클래스
 * 
 * @property id 서버에서 받는 id
 * @property nickname 사용자 닉네임
 * @property profileCharacterType 프로필 캐릭터 타입
 */
data class NoteComponentUserData(
    val id: Long,
    val nickname: String,
    val profileCharacterType: ProfileType,
) {
    companion object Companion {
        fun empty(): NoteComponentUserData {
            return NoteComponentUserData(
                id = -1L,
                nickname = "",
                profileCharacterType = ProfileType.SHORT_HAIR,
            )
        }

        fun sample(): NoteComponentUserData {
            return NoteComponentUserData(
                id = 1L,
                nickname = "샘플유저",
                profileCharacterType = ProfileType.POOP_HAIR,
            )
        }
    }
}

/**
 * 노트 컴포넌트의 곡 데이터 클래스
 * 
 * @property id 서버에서 받는 id
 * @property name 곡 이름
 * @property imageUrl 이미지 URL
 * @property artist 아티스트 정보
 */
data class NoteComponentSongData(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val artist: NoteComponentArtistData,
) {
    companion object Companion {
        fun empty(): NoteComponentSongData {
            return NoteComponentSongData(
                id = -1L,
                name = "",
                imageUrl = "",
                artist = NoteComponentArtistData.empty(),
            )
        }

        fun sample(): NoteComponentSongData {
            return NoteComponentSongData(
                id = 982L,
                name = "시퍼런 봄",
                imageUrl = "https://i.scdn.co/image/ab67616d0000b2730b1e2a5d990c3e198effa85b",
                artist = NoteComponentArtistData.sample(),
            )
        }
    }
}

/**
 * 노트 컴포넌트의 아티스트 데이터 클래스
 * 
 * @property id 서버에서 받는 id
 * @property name 아티스트 이름
 */
data class NoteComponentArtistData(val id: Long, val name: String) {
    companion object {
        fun empty(): NoteComponentArtistData {
            return NoteComponentArtistData(id = -1L, name = "")
        }

        fun sample(): NoteComponentArtistData {
            return NoteComponentArtistData(id = 15, name = "쏜애플")
        }
    }
}
