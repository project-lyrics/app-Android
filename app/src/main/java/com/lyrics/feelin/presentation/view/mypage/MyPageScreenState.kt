package com.lyrics.feelin.presentation.view.mypage

import com.lyrics.feelin.core.designsystem.component.FilterButtonData
import com.lyrics.feelin.presentation.view.component.note.NoteComponentData
import com.lyrics.feelin.presentation.view.component.profile.ProfileType

enum class MyPageScreenStatus {
    INITIAL,
    LOADING,
    SUCCESS_LOAD,
    ERROR,
}

enum class MyPageTabScreenStatus {
    INITIAL,
    LOADING,
    SUCCESS_LOAD,
    ERROR,
}

data class MyPageScreenState(
    val status: MyPageScreenStatus,
    val tabStatus: MyPageTabScreenStatus,
    val user: MyPageUserData?, // null일 때는 로그아웃 상태
    val filterArtists: List<FilterButtonData>,
    val notes: List<NoteComponentData>?,
) {
    companion object {
        fun initial(): MyPageScreenState {
            return MyPageScreenState(
                status = MyPageScreenStatus.INITIAL,
                tabStatus = MyPageTabScreenStatus.INITIAL,
                user = MyPageUserData.empty(),
                filterArtists = emptyList(),
                notes = emptyList(),
            )
        }
    }
}

// TODO(@이대근): 추후 사용자의 도메인 모델을 사용할 수도 있음 2025.11.19.
/**
 * 마이페이지 화면의 사용자 데이터 클래스
 *
 * @property id 서버에서 받는 id - 추후 프로필 변경 화면으로 넘겨줄 시 필요
 * @property nickname 사용자 닉네임
 * @property profileCharacterType 프로필 캐릭터 타입
 */
data class MyPageUserData(
    val id: Long,
    val nickname: String,
    val profileCharacterType: ProfileType,
) {
    companion object Companion {
        fun empty(): MyPageUserData {
            return MyPageUserData(
                id = -1L,
                nickname = "",
                profileCharacterType = ProfileType.SHORT_HAIR,
            )
        }

        fun sample(): MyPageUserData {
            return MyPageUserData(
                id = 1L,
                nickname = "샘플유저",
                profileCharacterType = ProfileType.BRAIDED_HAIR,
            )
        }
    }
}

